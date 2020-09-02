package com.lvym;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lvym.entiy.Order;
import com.lvym.procuder.RabbitMQSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private RabbitMQSender send;
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	@Test
	public void contextLoads() throws Exception {
		 Map<String, Object> propertise=new HashMap<>();
		 propertise.put("num","12354");
		 propertise.put("send_time", simpleDateFormat.format(new Date()));
		send.send("springboot2.0---hai", propertise);
	}
	@Test
	public void contextLoa() throws Exception {
		Order order=new Order("02","007");
		send.sendOrder(order);
	}
}
