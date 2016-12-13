package com.xy.common.json;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

/**
 * alibaba fastjson utils
 */
public class FastJsonUtils {
	
	/**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(FastJsonUtils.class);
	
	/**
	 * 对象转json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		try {
			if (null == object) {
				return null;
			}
			return JSONObject.toJSONString(object);
		} catch (Exception e) {
			logger.error("toJSONString error：", e);
			return null;
		}
	}
	
	/**
	 * json字符串转list
	 * 
	 * @param text
	 * @param clazz
	 * @return
	 */
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
    	try {
    		if (StringUtils.isEmpty(text) || null == clazz) {
    			return null;
    		}
    		return JSONObject.parseArray(text, clazz);
		} catch (Exception e) {
			logger.error("parseArray error：", e);
			return null;
		}
    }  
  
    /**
     * json字符串转对象
     * 
     * @param text
     * @param clazz
     * @return
     */
	public static <T> T parseObject(String text, Class<T> clazz) {
		try {
			if (StringUtils.isEmpty(text) || null == clazz) {
				return null;
			}
			return JSONObject.parseObject(text, clazz);
		} catch (Exception e) {
			logger.error("parseObject error：", e);
			return null;
		}
	}
	
	/**
	 * json字符串转复杂对象 使用 TypeReference
	 * 
	 * @param text
	 * @param typeReference
	 * @return
	 */
	public static <T> T parseObject(String text, TypeReference<T> typeReference) {
		try {
			if (StringUtils.isEmpty(text) || null == typeReference) {
				return null;
			}
			return JSONObject.parseObject(text, typeReference);
		} catch (Exception e) {
			logger.error("parseObject error：", e);
			return null;
		}
	}
	
	/**
	 * json字符串转复杂对象 使用 TypeReference和features
	 * 
	 * @param text
	 * @param typeReference
	 * @param features
	 * @return
	 */
	public static <T> T parseObject(String text, TypeReference<T> typeReference, Feature... features) {
		try {
			if (StringUtils.isEmpty(text) || null == typeReference) {
				return null;
			}
			return JSONObject.parseObject(text, typeReference, features);
		} catch (Exception e) {
			logger.error("parseObject error：", e);
			return null;
		}
	}
}
