package com.xy.common.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OkHttp 日志监控
 */
public class LogInterceptor implements Interceptor {

	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	/**
	 * 拦截器
	 */
	public Response intercept(Chain chain) throws IOException {
		 Request request = chain.request();
		 logger.info("OkHttp 请求数据【{}】", request.toString());

         long startTime = System.currentTimeMillis();
         Response response = chain.proceed(request);
         long endTime = System.currentTimeMillis();

         logger.info("OkHttp 请求用时【{}】秒", (endTime - startTime) / 1000.000);

         MediaType mediaType = response.body().contentType();
         String content = response.body().string();
         logger.info("OkHttp 响应数据【{}】", content);

         return response.newBuilder()
                 .body(ResponseBody.create(mediaType, content))
                 .build();
	}

}
