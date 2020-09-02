package com.lvym.limit;


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
				String exchangeName="test-qos-exchange";
				String routingKey="qos.#";
				String queueName="test-qos-queue";
				//5.声明交换机，队列，绑定
				channel.exchangeDeclare(exchangeName, "topic",true);
				channel.queueDeclare(queueName, true, false, false, null);
				channel.queueBind(queueName, exchangeName, routingKey);
				//6.
				//1 限流方式  第一件事就是 autoAck设置为 false
				               //  0 = 不限制      1 = 一次消费一条，不批量   
				channel.basicQos(0, 1, false);
				
				channel.basicConsume(queueName,false,new MyConsumer(channel));
			
	}

}
