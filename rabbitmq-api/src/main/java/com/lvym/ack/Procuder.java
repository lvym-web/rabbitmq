package com.lvym.ack;



import java.util.HashMap;
import java.util.Map;

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
		String exchangeName="test-ack-exchange";
		String routingKey="ack.kk.save";
		//6.发送
		
		
		for (int i = 0; i < 6; i++) {
			Map<String,Object> headers=new HashMap<String, Object>();
			headers.put("num", i);
			AMQP.BasicProperties properties=new AMQP.BasicProperties().builder()
					.deliveryMode(2)
					.contentEncoding("utf-8")
					.headers(headers)
					.build();
			String msg="hello-ack模式"+i;
			channel.basicPublish(exchangeName, routingKey, properties,msg.getBytes());
		}
		
		
	}
}
