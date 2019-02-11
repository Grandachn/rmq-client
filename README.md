# rmq-client
基于注解的RocketMq客户端
使用示例：
rocketmq.properties中配置RocketMq地址
```
namesrvAddr = 192.168.29.132:9876
```

消息生产者示例：
```
@RocketMq
@Service
public class TestService {
    //使用该注解配置队列信息，调用该方法，会将返回的对象作为消息发送到消息队列
    @OutputMessage(topic = "TestHandler", tags = "TagA", producerGroup = "createMessage")
    public String receiveMessage2(Dog message){
        System.out.println("[dog2 reveive]:" + message);
        return  message.getName();
    }
}
```

消息消费者示例：
```
@RocketMq
@Service
public class TestService {
    //使用该注解会自动生成一个消费者订阅配置队列的消息，该消息作为入参传入消费，注解方法有且仅有一个参数，参数类型为消息反序列化后的类型
    @InputMessage(topic = "TestHandler", subExpression = "TagA", consumerGroup = "receiveDog3")
    public void receiveMessage3(String message){
        System.out.println("[message reveive]:" + message);
    }

}
```

事务消息示例：
```
@RocketMq
@Service
public class TestService {
   //注解方法被调用前，向RocketMq发送事务消息，事务消息为带@TransactionMessage的参数，方法正常执行结束，事务消息就会被确认
   @TransactionMethod(topic = "TestHandler", tags = "Dog1", producerGroup = "Dog")
    public String createDog(String t, @TransactionMessage Dog dog, String s) throws InterruptedException {
        System.out.println("doing create dog:" + s);
        return "tranaction";
    }
}
 
```
