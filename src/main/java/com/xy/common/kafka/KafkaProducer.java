package com.xy.common.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka消息队列 生产者
 */
public class KafkaProducer {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

	/**
	 * kafka 地址列表
	 */
	private String hostPortList;
	
	/**
	 * 生产者
	 */
	private Producer<String, String> producer;
	
	/**
	 * 初始化生产者
	 */
	public void init() {
		Properties props = new Properties();
		//kafka ip和端口号
		props.put("metadata.broker.list", hostPortList);
		//value的系列化类
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		//key的系列化类
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		//触发acknowledgement机制
		//1，意味着在leader replica已经接收到数据后，producer会得到一个ack
		props.put("request.required.acks", "1");
		//线程异步发送
		props.put("producer.type", "async");
		
		producer = new Producer<String, String>(new ProducerConfig(props));
		logger.info("hostPortList【{}】， kafka生产者初始化成功！", hostPortList);
	}
	
	/**
	 * 关闭生产者连接
	 */
	public void close() {
		if (null != producer) {
			producer.close();
			producer = null;
			logger.info("hostPortList【{}】， kafka生产者关闭成功！", hostPortList);
		}
	}
	
	/**
	 * 发送单条消息
	 * 
	 * @param topic
	 * @param value
	 */
	public void send(final String topic, final String value) {
		producer.send(new KeyedMessage<String, String>(topic, value));
	}
	
	/**
	 * 发送批量消息
	 * 
	 * @param messages
	 */
	public void send(final Map<String, String> map) {
		if (null != map) {
			List<KeyedMessage<String, String>> messages = new ArrayList<KeyedMessage<String, String>>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				messages.add(new KeyedMessage<String, String>(entry.getKey(), entry.getValue()));
			}
			producer.send(messages);
		}
	}

	public String getHostPortList() {
		return hostPortList;
	}

	public void setHostPortList(String hostPortList) {
		this.hostPortList = hostPortList;
	}
	
	public static void main(String[] args) {
		KafkaProducer producer = new KafkaProducer();
		producer.setHostPortList("10.0.202.63:9092");
		producer.init();
		
		producer.send("match_stock_buy", "熊焱测试新大赛使用kafka队列1");
		producer.send("match_stock_sell", "熊焱测试新大赛使用kafka队列2");
		
		producer.close();
	}
}
