package com.lvym.quickstart;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Procuder {
	
	public static void main(String[] args) throws IOException, TimeoutException {
		//创建连接工厂
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("192.168.168.128");
		factory.setPort(5672);
		factory.setVirtualHost("/");
		//连接工厂
    Connection newConnection = factory.newConnection();
		//创建通道
    Channel createChannel = newConnection.createChannel();
    //发送数据
    for (int i = 0; i < 5; i++) {
		String msg="hello QabbitMQ";
		createChannel.basicPublish("", "test01", null, msg.getBytes());
	}
		//关闭
    createChannel.close();
    newConnection.close();
		
	}

}
