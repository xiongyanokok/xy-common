package com.xy.common.http;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OkHttp 工具类
 */
public class OkHttpUtils {

	/**
	 * logger
	 */
    private static Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    /**
     * OkHttpClient
     */
    private static OkHttpClient client = null;
	static {
		client = new OkHttpClient.Builder()
		.connectTimeout(10, TimeUnit.SECONDS)
		.readTimeout(10, TimeUnit.SECONDS)
		.addInterceptor(new LogInterceptor())
		.build();
	}

	/**
	 * get 请求
	 *
	 * @param url
	 * @return
	 */
	public static String doGet(final String url) {
		try {
			if (StringUtils.isEmpty(url)) {
				return null;
			}
			Request request = new Request.Builder().url(url).get().build();
			Response response = client.newCall(request).execute();
			if (null != response && response.isSuccessful()) {
				return response.body().string();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("url【{}】，执行失败", url, e);
			return null;
		}
	}

	/**
	 * 异步 get 请求
	 *
	 * @param url
	 * @param callback
	 */
	public static void doAsynGet(final String url, final Callback callback) {
		try {
			if (StringUtils.isEmpty(url)) {
				return;
			}
			Request request = new Request.Builder().url(url).get().build();
			client.newCall(request).enqueue(callback);
		} catch (Exception e) {
			logger.error("url【{}】，异步执行失败", url, e);
		}
	}

	/**
	 * post 请求
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(final String url, final Map<String, String> params) {
		try {
			if (StringUtils.isEmpty(url)) {
				return null;
			}
			Builder fb = new FormBody.Builder();
			if (null != params) {
				for (Map.Entry<String, String> en : params.entrySet()) {
					fb.add(en.getKey(), en.getValue());
				}
			}
			RequestBody formBody = fb.build();
			Request request = new Request.Builder().url(url).post(formBody).build();
			Response response = client.newCall(request).execute();
			if (null != response && response.isSuccessful()) {
				return response.body().string();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("url【{}】，参数【{}】，执行失败", url, params, e);
			return null;
		}
	}

	/**
	 * 异步 post 请求.
	 *
	 * @param url
	 * @param params
	 * @param callback
	 * @return
	 */
	public static void doAsynPost(final String url, final Map<String, String> params, final Callback callback) {
		try {
			if (StringUtils.isEmpty(url)) {
				return;
			}
			Builder fb = new FormBody.Builder();
			if (null != params) {
				for (Map.Entry<String, String> en : params.entrySet()) {
					fb.add(en.getKey(), en.getValue());
				}
			}
			RequestBody formBody = fb.build();
			Request request = new Request.Builder().url(url).post(formBody).build();
			client.newCall(request).enqueue(callback);
		} catch (Exception e) {
			logger.error("url【{}】，参数【{}】，异步执行失败", url, params, e);
		}
	}

}
