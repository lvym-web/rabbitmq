package com.lvym.quickstart;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {

	public static void main(String[] args) throws Exception {
		//创建连接工厂
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("192.168.168.128");
		factory.setPort(5672);
		factory.setVirtualHost("/");
		//连接工厂
    Connection newConnection = factory.newConnection();
		//创建通道
    Channel createChannel = newConnection.createChannel();
    //声明队列
    String queueName="test01";
    createChannel.queueDeclare(queueName, true, false, false, null);
    //创建消费者
    QueueingConsumer queueingConsumer = new QueueingConsumer(createChannel);
    //设置createChannel
    createChannel.basicConsume(queueName, true,queueingConsumer);
    //获取消息
    while (true) {
		Delivery nextDelivery = queueingConsumer.nextDelivery();
		String msg=new String(nextDelivery.getBody());
		System.out.println("消费："+msg);
		
	}
	}
}
