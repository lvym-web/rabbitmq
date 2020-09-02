package com.lvym.consumer;

import java.io.IOException;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class RabbitMQConsumer {

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "springboot-queue",durable="true"),
			exchange = @Exchange(value = "springboot-exchange", durable="true", type= "topic", ignoreDeclarationExceptions = "true"),
			key = "springboot.#"
			))
	@RabbitHandler
	public void onMessage(Message message,Channel channel) throws Exception {
		System.out.println("------RabbitMQConsumer----------");
		System.out.println("消费----："+message.getPayload());
		Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		//手工ACK
		channel.basicAck(deliveryTag, false);
		
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",durable="${spring.rabbitmq.listener.order.queue.durable}"),
			exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}", durable="${spring.rabbitmq.listener.order.exchange.durable}", type= "${spring.rabbitmq.listener.order.exchange.type}", ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
			key = "${spring.rabbitmq.listener.order.key}"
			))
	@RabbitHandler
	public void onMessage(@Payload com.lvym.entiy.Order order,Channel channel,@Headers Map<String,Object> headers) throws Exception {
		System.out.println("------RabbitMQConsumer----------");
		System.out.println("消费----："+order.getId()+"name"+order.getName());
		Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
		//手工ACK
		channel.basicAck(deliveryTag, false);
		
	}
}
