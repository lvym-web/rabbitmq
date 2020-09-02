package com.lvym.ack;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {

	public static void main(String[] args) throws Exception{
		  //1.创建工厂
				ConnectionFactory factory=new ConnectionFactory();
				factory.setHost("192.168.168.128");
				factory.setPort(5672);
				factory.setVirtualHost("/");
				//2.连接工厂
				Connection connection = factory.newConnection();
				//3.创建通道
				Channel channel = connection.createChannel();
				//4.声明交换机，路由,队列
				String exchangeName="test-ack-exchange";
				String routingKey="ack.#";
				String queueName="test-ack-queue";
				//5.声明交换机，队列，绑定
				channel.exchangeDeclare(exchangeName, "topic",true);
				channel.queueDeclare(queueName, true, false, false, null);
				channel.queueBind(queueName, exchangeName, routingKey);
				//6.创建消费者
			
				channel.basicConsume(queueName,false,new MyConsumer(channel));
			
	}

}
