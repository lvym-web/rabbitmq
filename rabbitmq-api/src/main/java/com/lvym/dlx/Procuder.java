package com.lvym.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Procuder {

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
		
		//5.声明交换机，路由
		String exchangeName="test-dlx-exchange";
		String routingKey="dlx.kk.save";
		//6.发送
				
			AMQP.BasicProperties properties=new AMQP.BasicProperties().builder()
					.deliveryMode(2)
					.contentEncoding("utf-8")
					.expiration("10000")
					.build();
			
			String msg="hello-dlx模式";
			channel.basicPublish(exchangeName, routingKey, properties,msg.getBytes());
		
		
		
	}
}
