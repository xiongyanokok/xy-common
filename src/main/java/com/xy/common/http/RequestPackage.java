package com.xy.common.http;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RequestPackage
 */
public class RequestPackage {

	private static Logger logger = LoggerFactory.getLogger(RequestPackage.class);
	/**
	 * 请求url
	 */
	private String url;
	/**
	 * 请求方法
	 */
	private String method;
	/**
	 * 请求头
	 */
	private Map<String, String> headers;
	/**
	 * 请求的cookie
	 */
	private Map<String, String> cookies;
	/**
	 * keyValueMap 的数据
	 */
	private List<NameValuePair> nameValuePairs;
	/**
	 * post的内容
	 */
	private String postContent;
	/**
	 * accept
	 */
	private String accept;
	/**
	 * user agent
	 */
	private String userAgent;
	/**
	 * 默认编码 utf8
	 */
	private String charset = "UTF-8";

	public String getUrl() {
		return url;
	}

	public RequestPackage setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getMethod() {
		return method;
	}

	public RequestPackage setMethod(String method) {
		this.method = method;
		return this;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public RequestPackage setHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public RequestPackage setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
		return this;
	}

	public String getPostContent() {
		return postContent;
	}

	public RequestPackage setPostContent(String postContent) {
		this.postContent = postContent;
		return this;
	}

	public String getAccept() {
		return accept;
	}

	public RequestPackage setAccept(String accept) {
		this.accept = accept;
		return this;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public RequestPackage setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	public List<? extends NameValuePair> getNameValuePairs() {
		return nameValuePairs;
	}

	public RequestPackage setNameValuePairs(List<NameValuePair> nameValuePairs) {
		this.nameValuePairs = nameValuePairs;
		return this;
	}

	/**
	 * 直接设置 post
	 * 
	 * @param map
	 * @return
	 */
	public RequestPackage post(Map<?, ?> map) {
		this.method = "POST";
		setNameValuePairs(map);
		return this;
	}

	/**
	 * 设置请求的data
	 * 
	 * @param map
	 * @return
	 */
	public RequestPackage setNameValuePairs(Map<?, ?> map) {
		if (map != null && !map.isEmpty()) {
			this.nameValuePairs = new ArrayList<NameValuePair>();
			for (Map.Entry<?, ?> kv : map.entrySet()) {
				if (kv.getKey() != null) {
					String value = kv.getValue() == null ? "" : kv.getValue().toString();
					BasicNameValuePair nvp = new BasicNameValuePair(kv.getKey().toString(), value);
					this.nameValuePairs.add(nvp);
				}
			}
		}
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public RequestPackage setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 获取response
	 * 
	 * @return
	 * @throws Exception
	 */
	public ResponsePackage getResponse() {
		try {
			return HttpClientUtils.exec(this);
		} catch (Exception ex) {
			logger.error("get response error", ex);
			return null;
		}
	}

	/************* 简化语法,创建实例 ***************/

	/**
	 * 直接创建 request package get
	 * 
	 * @param url
	 * @return
	 */
	public static RequestPackage get(String url) {
		RequestPackage requestPackage = new RequestPackage().setUrl(url).setMethod("GET");
		return requestPackage;
	}

	/**
	 * 直接创建 request package post
	 * 
	 * @param url
	 * @return
	 */
	public static RequestPackage post(String url, Map<?, ?> data) {
		RequestPackage requestPackage = new RequestPackage().setUrl(url).setMethod("POST").setNameValuePairs(data);
		return requestPackage;
	}
}
