package com.xy.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记参数内Body对象的原始类型
 *
 * @author xiongyan
 * @date 2016年9月24日 上午11:18:39
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiBodyRawType {

	/**
	 * body序列化对象
	 * 
	 * @return
	 */
	Class<?> value();
	
}
