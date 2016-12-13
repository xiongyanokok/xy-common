package com.xy.common.kafka;

/**
 * kafka队列回调
 */
public interface KafkaCallback {

	/**
	 * 接收消息
	 * 
	 * @param message
	 */
	public void receive(String message);
}
