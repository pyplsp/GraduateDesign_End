# GraduateDesign_End
毕业设计后端
# 运行模式
![image](https://user-images.githubusercontent.com/70948774/224944973-b5e00676-d271-4253-95f9-767293742241.png)
# 数据库设计
电梯档案(lifts) 
（id,liftName,liftCode,describtion,positionX,positonY）

告警表(alarm)
（id,liftCode(外键),alarmType,alarmTime,alarmStatus,hasPerson,personNum,ifFlat,describtion）

救援记录表(save)
（id,uniqueNumber,savePositonX,savePositionY,ifSucsess,userId,staffName,staffNumber）

管理员表(user)
（id,account,password,name,ifAdmin，types）

员工表（staff）
（id,name,identification,phoneNumber,sex）

权限表(power)
（id,userId(外键),addlift,editorlift,addSaveInfo,editorSaveInfo）

