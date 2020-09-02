package com.lvym.confirm;


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
		//4.确认消息模式
		channel.confirmSelect();//
		//5.声明交换机，路由
		String exchangeName="test-confirm-exchange";
		String routingKey="confirm.save";
		//6.发送
		String msg="hello-confirm模式";
		channel.basicPublish(exchangeName, routingKey, null,msg.getBytes());
		//7.确认监听
		channel.addConfirmListener(new ConfirmListener() {
			
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
			System.out.println("--------no,ack------");
				
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("--------ack------");
				
			}
		});
		
	}
}
