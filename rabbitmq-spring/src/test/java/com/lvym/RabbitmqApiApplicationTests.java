package com.lvym;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvym.entity.Order;
import com.lvym.entity.Packaged;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApiApplicationTests {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Test
	public void testSendMessage() throws Exception {
		//1 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.getHeaders().put("desc", "信息描述..");
		messageProperties.getHeaders().put("type", "自定义消息类型..");
		Message message = new Message("Hello RabbitMQ".getBytes(), messageProperties);
		
		rabbitTemplate.convertAndSend("topic01", "spring.amqp", message, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				System.err.println("------添加额外的设置---------");
				message.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
				message.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
				return message;
			}
		});
	}
	@Test
	public void contextLoads() {
		MessageProperties messageProperties=new MessageProperties();
		messageProperties.setContentType("text/plain");
		Message message=new Message("mq消息---".getBytes(), messageProperties);
		rabbitTemplate.send("topic01", "spring.kk", message);
		rabbitTemplate.convertAndSend("topic01", "spring.amqp", "hello object message send01!");
		rabbitTemplate.convertAndSend("topic02", "rabbit.abc", "hello object message send02!");
	}
	
	@Test
	public void contextLoadsr() {
		MessageProperties messageProperties=new MessageProperties();
		messageProperties.setContentType("text/plain");
		Message message=new Message("mq 消息123456".getBytes(), messageProperties);
		rabbitTemplate.send("topic01", "spring.abc", message);
		rabbitTemplate.send("topic02", "rabbit.abc", message);
	}
	@Test
	public void testSendJsonMessage() throws Exception {
		
		Order order = new Order();
		order.setId("001");
		order.setName("消息订单");
		order.setContent("描述信息");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json);
		
		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		Message message = new Message(json.getBytes(), messageProperties);
		
		rabbitTemplate.send("topic01", "spring.order", message);
	}
	@Test
	public void testSendJavaMessage() throws Exception {
		
		Order order = new Order();
		order.setId("001");
		order.setName("订单消息");
		order.setContent("订单描述信息");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json);
		
		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		messageProperties.getHeaders().put("__TypeId__", "com.lvym.entity");
		Message message = new Message(json.getBytes(), messageProperties);
		
		rabbitTemplate.send("topic01", "spring.order", message);
	}
	@Test
	public void testSendMappingMessage() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		Order order = new Order();
		order.setId("001");
		order.setName("订单消息");
		order.setContent("订单描述信息");
		
		String json1 = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json1);
		
		MessageProperties messageProperties1 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties1.setContentType("application/json");
		messageProperties1.getHeaders().put("__TypeId__", "order");
		Message message1 = new Message(json1.getBytes(), messageProperties1);
		rabbitTemplate.send("topic001", "spring.order", message1);
		
		Packaged pack = new Packaged();
		pack.setId("002");
		pack.setName("包裹消息");
		pack.setDescription("包裹描述信息");
		
		String json2 = mapper.writeValueAsString(pack);
		System.err.println("pack 4 json: " + json2);

		MessageProperties messageProperties2 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties2.setContentType("application/json");
		messageProperties2.getHeaders().put("__TypeId__", "packaged");
		Message message2 = new Message(json2.getBytes(), messageProperties2);
		rabbitTemplate.send("topic01", "spring.pack", message2);
	}
}