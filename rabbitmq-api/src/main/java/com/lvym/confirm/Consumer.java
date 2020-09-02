package com.lvym.confirm;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

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
				String exchangeName="test-confirm-exchange";
				String routingKey="confirm.#";
				String queueName="test-confirm-queue";
				//5.声明交换机，队列，绑定
				channel.exchangeDeclare(exchangeName, "topic",true);
				channel.queueDeclare(queueName, true, false, false, null);
				channel.queueBind(queueName, exchangeName, routingKey);
				//6.创建消费者
				QueueingConsumer consumer = new QueueingConsumer(channel);
				channel.basicConsume(queueName, true, consumer);
				while (true) {
					Delivery delivery = consumer.nextDelivery();
					String msg=new String(delivery.getBody());
					System.out.println("消费----："+msg);
					
				}
	}

}
