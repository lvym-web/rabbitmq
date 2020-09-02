package com.lvym.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class ConsumeDirect {
public static void main(String[] args) throws Exception {
	//创建连接工厂
	
			ConnectionFactory factory=new ConnectionFactory();
			factory.setHost("192.168.168.128");
			factory.setPort(5672);
			factory.setVirtualHost("/");
			
			factory.setAutomaticRecoveryEnabled(true);
			factory.setNetworkRecoveryInterval(3000);
			//连接工厂
	    Connection connection = factory.newConnection();
			//创建通道
	    Channel channel = connection.createChannel();
	    //声明
	 
	    String exchangeName="test_direct_exchange";
	    String exchangeType="direct";
	    String queueName="test_queue";
	    String routingKey="test_direct";
	    //声明交换机
	    channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
	    //声明队列
	    channel.queueDeclare(queueName, false, false, false, null);
	    //建立绑定
	    channel.queueBind(queueName, exchangeName, routingKey);
	    //创建消费者
	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    //
	   
	    channel.basicConsume(queueName, true, consumer);
	    while (true) {
			Delivery delivery = consumer.nextDelivery();
			String msg=new String(delivery.getBody());
			System.out.println("收到消息："+msg);
		}
}
}
