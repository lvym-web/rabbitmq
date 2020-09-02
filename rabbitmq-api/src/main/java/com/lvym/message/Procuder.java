package com.lvym.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
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
    Map<String,Object> headers=new HashMap<>();
    headers.put("k1", "v1");
    AMQP.BasicProperties properties=new AMQP.BasicProperties.Builder()
    		.deliveryMode(2)   //2=持久化 ， 1=反之
    		.contentEncoding("utf-8")
    		.expiration("10000")  //过期时间    10秒
            .headers(headers)    //自定义
            .build();
    
    //发送数据
    for (int i = 0; i < 5; i++) {
		String msg="hello RabbitMQ";
		createChannel.basicPublish("", "test01", properties, msg.getBytes());
	}
		//关闭
    createChannel.close();
    newConnection.close();
		
	}

}
