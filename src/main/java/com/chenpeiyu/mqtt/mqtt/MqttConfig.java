package com.chenpeiyu.mqtt.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chenpeiyu.mqtt.dao.AlarmMapper;
import com.chenpeiyu.mqtt.dao.LiftMapper;
import com.chenpeiyu.mqtt.domain.Alarm;
import com.chenpeiyu.mqtt.domain.Lift;
import com.chenpeiyu.mqtt.utils.BaseUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@Configuration
public class MqttConfig {
    @Autowired
    private LiftMapper liftMapper;

    @Autowired
    private AlarmMapper alarmMapper;

    @Resource
    private MqttGateway mqttGateway;

    @Autowired
    private BaseUtils baseUtils;

    /**
     * 创建MqttPahoClientFactory，设置MQTT Broker连接属性，如果使用SSL验证，也在这里设置。
     * @return factory
    */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        // 设置代理端的URL地址，可以是多个
        options.setServerURIs(new String[]{"tcp://175.178.47.147:1883"});
        options.setUserName("admin");
        options.setPassword("admin".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 入站通道
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * 入站
     */
    @Bean
    public MessageProducer inbound() {
        // Paho客户端消息驱动通道适配器，主要用来订阅主题
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "springBoot_PY",
                mqttClientFactory(),
                "TOPIC_COMMON");
        adapter.setCompletionTimeout(5000);

        // Paho消息转换器
        DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
        // 按字节接收消息
        // defaultPahoMessageConverter.setPayloadAsBytes(true);

        adapter.setConverter(defaultPahoMessageConverter);
        // adapter.setQos(1); // 设置QoS
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    // ServiceActivator注解表明：当前方法用于处理MQTT消息，inputChannel参数指定了用于消费消息的channel。
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String payload = message.getPayload().toString();
            String dataType = JSON.parseObject(payload).getString("dataType");

            // 接收摄像头告警数据
            if(Objects.equals(dataType, "alarm")){
                // 告警数据里的设备代码
                JSONObject pojo = JSON.parseObject(payload);
                String liftIDNo = pojo.getString("liftIDNo");
                // 通过设备代码查询用户id
                LambdaQueryWrapper<Lift> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Lift::getLiftCode,liftIDNo);
                try {
                    // 将消息发送出去 ALARM/用户id
                    Integer userId = liftMapper.selectOne(lambdaQueryWrapper).getUserId();
                    mqttGateway.sendToMqtt("ALARM/" + userId,2,payload);

                    // 保存告警记录
                    if(pojo.getIntValue("alarmStatus") == 0){
                        // 告警解除，更新数据，逻辑：报解除时，解除当天的同样类型的告警
                        LambdaUpdateWrapper<Alarm> updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper
                                .eq(Alarm::getAlarmTypeCode,pojo.getString("alarmTypeCode"))
                                .gt(Alarm::getAlarmTime, LocalDate.now());
                         Alarm alarm = new Alarm();
                         alarm.setAlarmStatus(0);
                         alarm.setAlarmRemoveTime(Timestamp.valueOf(pojo.getString("alarmTime")));
                         alarmMapper.update(alarm,updateWrapper);
                    }else{
                        // 告警产生,增加数据
                        Lift alarmLift = liftMapper.selectOne(new LambdaQueryWrapper<Lift>().eq(Lift::getLiftCode,pojo.getString("liftIDNo")));
                        Alarm alarm = new Alarm();
                        alarm.setLiftId(alarmLift.getId());
                        alarm.setAlarmTypeName(pojo.getString("alarmTypeName"));
                        alarm.setAlarmTime(Timestamp.valueOf(pojo.getString("alarmTime")));
                        alarm.setAlarmStatus(pojo.getIntValue("alarmStatus"));
                        alarm.setPersonNum(pojo.getIntValue("personNum"));
                        alarm.setCurrFloor(pojo.getIntValue("currFloor"));
                        alarm.setIfFlat(pojo.getIntValue("ifFlat"));
                        alarm.setAlarmTypeCode(pojo.getString("alarmTypeCode"));
                        alarmMapper.insert(alarm);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (Objects.equals(dataType, "realTime")) {
                // 接收摄像头实时数据
                // 将消息发送出去 REALTIME/消息携带的 wsSessionId
                mqttGateway.sendToMqtt("REALTIME/" + JSON.parseObject(payload).getString("wsSessionId"),0,payload);
            }else if (Objects.equals(dataType, "online")){
                // 接收摄像头在线数据
                // 在线数据里的设备代码
                String liftIDNo = JSON.parseObject(payload).getString("liftIDNo");
                // 根据协议，接收到在线数据时要进行时间校准
                JSONObject obj = new JSONObject();
                obj.put("method","prooftime");
                obj.put("prooftime", baseUtils.nowTime());
                mqttGateway.sendToMqtt(liftIDNo,0,obj.toJSONString());

                // 收到在线情况，应该对该设备进行更新
                String status = JSON.parseObject(payload).getString("status");
                LambdaUpdateWrapper<Lift> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(Lift::getLiftCode,liftIDNo).set(Lift::getInternetStatus,status);
                liftMapper.update(null,lambdaUpdateWrapper);
            }

        };
    }   

    // 发送消息

    /**
     * 出站通道
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * 出站
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler outbound() {

        // 发送消息和消费消息Channel可以使用相同MqttPahoClientFactory
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("publishClient", mqttClientFactory());
        messageHandler.setAsync(true); // 如果设置成true，即异步，发送消息时将不会阻塞。
        messageHandler.setDefaultTopic("command");
        messageHandler.setDefaultQos(1); // 设置默认QoS

        // Paho消息转换器
        DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();

        // defaultPahoMessageConverter.setPayloadAsBytes(true); // 发送默认按字节类型发送消息
        messageHandler.setConverter(defaultPahoMessageConverter);
        return messageHandler;
    }

}
