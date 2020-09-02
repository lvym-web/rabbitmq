package com.lvym.exchange.direct;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProduceDirect {

	
	public static void main(String[] args) throws Exception {
		
		//创建连接工厂
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("192.168.168.128");
		factory.setPort(5672);
		factory.setVirtualHost("/");
		//连接工厂
    Connection connection = factory.newConnection();
		//创建通道
    Channel channel = connection.createChannel();
    //声明
    String exchangeName="test_dirext_exchange";
    String routingKey="test_direct";
    
    String msg="hello exchange direct";
    //发送
    channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
	
	}
}
