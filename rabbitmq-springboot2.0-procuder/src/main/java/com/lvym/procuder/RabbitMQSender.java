package com.lvym.procuder;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.lvym.entiy.Order;
@Component
public class RabbitMQSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	
	//发送消息方法调用: 构建Message消息
	public void send(Object message,Map<String,Object> propertise) throws Exception  {
	MessageHeaders headers=new MessageHeaders(propertise);
	Message msg=MessageBuilder.createMessage(message,headers);
	rabbitTemplate.setConfirmCallback(confirmCallback);
	rabbitTemplate.setReturnCallback(returnCallback);
	//id + 时间戳 全局唯一 
	CorrelationData correlationData=new CorrelationData("123456");
	rabbitTemplate.convertAndSend("springboot-exchange","springboot.hello", msg,correlationData);
	
	
	}
	//回调函数: confirm确认
	final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
		
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause) {
			System.err.println("correlationData: " + correlationData);
			System.err.println("ack: " + ack);
			if(!ack){
				System.err.println("异常处理....");
			}else {
				System.out.println("更新数据库");
			}
			
		}
	};
	
	final ReturnCallback returnCallback=new RabbitTemplate.ReturnCallback() {
		
		@Override
		public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
				String exchange, String routingKey) {
			System.err.println("return exchange: " + exchange + ", routingKey: " 
					+ routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
			}
	};
	//发送消息方法调用: 构建Message消息
		public void sendOrder(Order order) throws Exception  {
		
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		//id + 时间戳 全局唯一 
		CorrelationData correlationData=new CorrelationData("123456");
		rabbitTemplate.convertAndSend("springboot-exchange02","springboot.hi", order,correlationData);
		
		
		}
}
