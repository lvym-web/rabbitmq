package com.lvym.limit;



import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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
		channel.confirmSelect();
		//5.声明交换机，路由
		String exchangeName="test-qos-exchange";
		String routingKey="qos.kk.save";
		//6.发送
		String msg="hello-qos模式";
		for (int i = 0; i < 6; i++) {
			channel.basicPublish(exchangeName, routingKey, null,msg.getBytes());
		}
		
		channel.addConfirmListener(new ConfirmListener() {
			
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("----------------no-ack-------");
				
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("----------------ack-------");
			}
		});
		
	}
}
