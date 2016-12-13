package com.xy.common.json;

import java.io.IOException;
import java.text.DateFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Json序列化工具类
 */
public class JsonUtils {

    /**
     * xml
     */
    public static final String JSON = "JSON";

    /**
     * xml
     */
    public static final String XML = "XML";

    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Jackson 映射器API，用以实现 JSON 和 Object 之间的转换
     */
    public ObjectMapper objectMapper;

    /**
     * 构造器 默认JSON
     */
    public JsonUtils() {
        this(null);
    }

    /**
     * 构造器
     *
     * @param jsonOrXml mapper的类型
     */
    public JsonUtils(String jsonOrXml) {
        if (StringUtils.isBlank(jsonOrXml)) {
            jsonOrXml = JSON;
        }
        if (jsonOrXml.equalsIgnoreCase(JSON)) {
            objectMapper = new ObjectMapper();
        } else {
            objectMapper = new XmlMapper();
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Object --> JSON
     * 忽略 Object 对象中为 null 的属性
     */
    public JsonUtils ignoreEmpty() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return this;
    }

    /**
     * 格式化输出 Json Text
     */
    public JsonUtils indentOutput() {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return this;
    }

    /**
     * 设置转换时间格式
     *
     * @param dateFormat dateFormat
     */
    public JsonUtils dateFormat(DateFormat dateFormat) {
        objectMapper.setDateFormat(dateFormat);
        return this;
    }

    /**
     * Object --> JSON 方法
     *
     * @param object targetObject instance
     * @return jsonText
     */
    public String serialize(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("toJSON JsonProcessingException：{}", e.getMessage(), e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * JSON --> Object 方法
     *
     * @param source  json字符串
     * @param classes 映射Java Class
     * @param <T>     target Object Type
     * @return target Object Type
     */
    public <T> T deserialize(String source, Class<T> classes) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            return objectMapper.readValue(source, classes);
        } catch (IOException e) {
            logger.error("toObject IOException：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 构造复杂对象使用 TypeReference 描述
     *
     * @param source        json text or xml text
     * @param typeReference TypeReference instance
     * @param <T>           target object type
     * @return target object instance
     */
    public <T> T deserialize(String source, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            return objectMapper.readValue(source, typeReference);
        } catch (IOException e) {
            logger.error("toObject IOException：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 使用 JSON 中的值，更新一个 Java 对象
     *
     * @param original original Object
     * @param source   json or xml Data
     * @param <T>      object type
     * @return target object instance
     */
    public <T> T update(T original, String source) {
        if (original == null) {
            return null;
        }
        try {
            objectMapper.readerForUpdating(original).readValue(source);
            return original;
        } catch (IOException e) {
            logger.error("updateObject IOException：{}", e.getMessage(), e);
            return null;
        }
    }
}
