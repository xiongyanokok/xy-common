package com.xy.common.response;

/**
 * 请求返回结果
 * 
 * @author xiongyan
 * @date 2016年12月9日 上午11:16:31
 */
public class Result<T> {

    /**
     * 编码
     */
    private char code;
    
    /**
     * 错误码
     */
    private ErrorCode errorCode;
    
    /**
     * 数据
     */
    private T data;

	public char getCode() {
		return code;
	}

	public void setCode(char code) {
		this.code = code;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * 成功消息
	 * 
	 * @return
	 */
	public static <T> Result<T> success() {
		return success(null);
	}

	/**
	 * 成功消息
	 * 
	 * @param data
	 * @return
	 */
	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<T>();
		result.setCode('Y');
		result.setErrorCode(ErrorCode.Success);
		result.setData(data);
		return result;
	}
	
	/**
	 * 失败消息
	 * 
	 * @param errorCode
	 * @return
	 */
	public static <T> Result<T> fail(ErrorCode errorCode) {
		return fail(errorCode, null);
	}
	
	/**
	 * 失败消息
	 * 
	 * @param errorCode
	 * @param data
	 * @return
	 */
	public static <T> Result<T> fail(ErrorCode errorCode, T data) {
		Result<T> result = new Result<T>();
		result.setCode('N');
		result.setErrorCode(errorCode);
		result.setData(data);
		return result;
	}
	
}
