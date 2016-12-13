package com.xy.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记请求访问时的特殊控制
 *
 * @author xiongyan
 * @date 2016年9月24日 上午11:16:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAccessControl {

	/**
	 * 最大并发个数
	 * 
	 * @return
	 */
	int maxConcurrentNum() default 100;
	
	/**
	 * 平均响应时间
	 * 
	 * @return
	 */
	int avgResponseTime() default 1000;
}
