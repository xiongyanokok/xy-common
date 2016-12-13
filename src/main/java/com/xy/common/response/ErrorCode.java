package com.xy.common.response;

import java.util.HashMap;

/**
 * 错误编码描述
 * 
 * @author xiongyan
 * @date 2016年11月23日 上午9:56:56
 */
public class ErrorCode {
	
	/**
	 * 编码
	 */
	private int value;
	
	/**
	 * 描述
	 */
	private String description;
	
	private static HashMap<Integer, ErrorCode> errorCodeMap = new HashMap<Integer, ErrorCode>();

	private ErrorCode(int value) {
		this.value = value;
	}

	private ErrorCode(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		return this.value + ":" + this.description;
	}
	
	public static ErrorCode valueOf(int value) {
	    return errorCodeMap.get(value);
	}

	public static ErrorCode valueOf(int code, String reason) {
		ErrorCode result = errorCodeMap.get(code);
		if (result == null) {
			result = new ErrorCode(code, reason);
			errorCodeMap.put(code, result);
		}
		return result;
	}

	public static ErrorCode UnknowError = valueOf(-1, "未知错误");
	public static ErrorCode Success = valueOf(0, "成功");
	public static ErrorCode SystemError = valueOf(10001, "系统错误");
	public static ErrorCode NotLogin = valueOf(10002, "未登录");
	public static ErrorCode NotBlank = valueOf(10003, "参数不能为空");
	public static ErrorCode NoAccess = valueOf(10004, "无权访问");
	public static ErrorCode NoData = valueOf(10005, "无数据");
	
}