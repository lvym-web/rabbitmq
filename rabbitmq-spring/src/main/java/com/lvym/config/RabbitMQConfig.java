package com.lvym.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.lvym.adapter.MessageDelegate;
import com.lvym.covert.ImageMessageConverter;
import com.lvym.covert.PDFMessageConverter;
import com.lvym.covert.TextMessageConverter;



@Configuration
@ComponentScan({"com.lvym.*"})
public class RabbitMQConfig {

	@Bean
	public ConnectionFactory factory() {
		CachingConnectionFactory factory=new CachingConnectionFactory();
		factory.setAddresses("192.168.168.128:5672");
		factory.setUsername("guest");
		factory.setPassword("guest");
        factory.setVirtualHost("/");
        return factory;
	}
	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
		RabbitAdmin rabbitAdmin=new RabbitAdmin(factory);
		rabbitAdmin.setAutoStartup(true);
		return rabbitAdmin;
	}
	
	  /**  
     * 针对消费者配置  
     * 1. 设置交换机类型  
     * 2. 将队列绑定到交换机  
        FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念  
        HeadersExchange ：通过添加属性key-value匹配  
        DirectExchange:按照routingkey分发到指定队列  
        TopicExchange:多关键字匹配  
     */  
	@Bean
	public TopicExchange exchange01() {
		return new TopicExchange("topic01", true, false);
	}
	@Bean
	public Queue queue01() {
		return new Queue("queue01", true);
	}
	@Bean
	public Binding binding01() {
		
		return BindingBuilder.bind(queue01()).to(exchange01()).with("spring.*");
	}
	@Bean
	public TopicExchange exchange02() {
		return new TopicExchange("topic02", true, false);
	}
	@Bean
	public Queue queue02() {
		return new Queue("queue02", true);
	}
	@Bean
	public Binding binding02() {
		
		return BindingBuilder.bind(queue02()).to(exchange02()).with("rabbit.*");
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
		return new RabbitTemplate(factory);
	}
	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory factory) {
		SimpleMessageListenerContainer container=new SimpleMessageListenerContainer(factory);
		container.setQueues(queue01(),queue02());//监控多个队列
		container.setConcurrentConsumers(1);//消费者数量
		container.setMaxConcurrentConsumers(6);//最大消费数
		container.setDefaultRequeueRejected(false);//是否重回队列
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);//签收策略
		container.setConsumerTagStrategy(new ConsumerTagStrategy() {
			
			@Override
			public String createConsumerTag(String queue) {
				// TODO Auto-generated method stub     标签策略
				return queue+"-"+UUID.randomUUID().toString();
			}
		});
		/*
		container.setMessageListener(new ChannelAwareMessageListener() {
			
			@Override
			public void onMessage(Message message, Channel channel) throws Exception {
				String msg=new String(message.getBody());
				System.err.println("----消费：--------"+msg);
			}
		});*/
		
		
    	/**
    	 * 1 适配器方式. 默认是有自己的方法名字的：handleMessage
    		// 可以自己指定一个方法的名字: consumeMessage
    		// 也可以添加一个转换器: 从字节数组转换为String*/
	/*	MessageListenerAdapter adapter=new MessageListenerAdapter(new MessageDelegate());
		adapter.setDefaultListenerMethod("consumeMessage");
		adapter.setMessageConverter(new TextMessageConverter());
		container.setMessageListener(adapter);
	
		*/
		/**
    	 * 2 适配器方式: 我们的队列名称 和 方法名称 也可以进行一一的匹配
    	 */
	/*	Map<String,String> queueOrTagToMethodName=new HashMap<String, String>();
		queueOrTagToMethodName.put("queue01","method1");
		queueOrTagToMethodName.put("queue02","method2");
		MessageListenerAdapter adapter=new MessageListenerAdapter(new MessageDelegate());
		adapter.setMessageConverter(new TextMessageConverter());
		adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
		container.setMessageListener(adapter);   
		*/
		   // 1.1 支持json格式的转换器
     /*   
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        
        container.setMessageListener(adapter);
		*/
     // 1.2 DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象转换
   /*
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        container.setMessageListener(adapter);*/
      //1.3 DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象多映射转换
        /**
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        
        Map<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
		idClassMapping.put("order", com.lvym.entity.Order.class);
		idClassMapping.put("packaged", com.bfxy.spring.entity.Packaged.class);
		
		javaTypeMapper.setIdClassMapping(idClassMapping);
		
		jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        container.setMessageListener(adapter);
        */
        
        //1.4 ext convert
        
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        
        //全局的转换器:
		ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();
		
		TextMessageConverter textConvert = new TextMessageConverter();
		convert.addDelegate("text", textConvert);
		convert.addDelegate("html/text", textConvert);
		convert.addDelegate("xml/text", textConvert);
		convert.addDelegate("text/plain", textConvert);
		
		Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
		convert.addDelegate("json", jsonConvert);
		convert.addDelegate("application/json", jsonConvert);
		
		ImageMessageConverter imageConverter = new ImageMessageConverter();
		convert.addDelegate("image/png", imageConverter);
		convert.addDelegate("image", imageConverter);
		
		PDFMessageConverter pdfConverter = new PDFMessageConverter();
		convert.addDelegate("application/pdf", pdfConverter);
        
		
		adapter.setMessageConverter(convert);
		container.setMessageListener(adapter);
		
        
		
		
		return container;
	}
	
}
