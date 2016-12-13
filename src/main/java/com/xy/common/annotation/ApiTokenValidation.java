package com.xy.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记需要进行Token有效性验证的方法
 *
 * @author xiongyan
 * @date 2016年9月24日 上午11:15:47
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiTokenValidation {

}
