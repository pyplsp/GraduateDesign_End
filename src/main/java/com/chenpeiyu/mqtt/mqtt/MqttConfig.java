package com.chenpeiyu.mqtt.mqtt;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chenpeiyu.mqtt.dao.LiftMapper;
import com.chenpeiyu.mqtt.domain.Lift;
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
import java.util.Objects;

@Configuration
public class MqttConfig {
    @Autowired
    private LiftMapper liftMapper;

    @Resource
    private MqttGateway mqttGateway;

    /**
     * 创建MqttPahoClientFactory，设置MQTT Broker连接属性，如果使用SSL验证，也在这里设置。
     * @return factory
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        // 设置代理端的URL地址，可以是多个
        options.setServerURIs(new String[]{"tcp://211.159.225.217:1883"});
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
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("consumerClient-paho", mqttClientFactory(), "TOPIC_COMMON");
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
            if(Objects.equals(dataType, "alarm")){
                // 告警数据里的设备代码
                String liftIDNo = JSON.parseObject(payload).getString("liftIDNo");
                // 通过设备代码查询用户id
                LambdaQueryWrapper<Lift> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Lift::getLiftCode,liftIDNo);
                try {
                    // 将消息发送出去 ALARM/用户id
                    Integer userId = liftMapper.selectOne(lambdaQueryWrapper).getUserId();
                    mqttGateway.sendToMqtt("ALARM/" + userId,payload);

                    // 保存告警记录
                    // ...


                }catch (Exception e){

                }
            } else if (Objects.equals(dataType, "realTime")) {
                // 将消息发送出去 REALTIME/消息携带的 wsSessionId
                mqttGateway.sendToMqtt("REALTIME/" + JSON.parseObject(payload).getString("wsSessionId"),payload);
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
