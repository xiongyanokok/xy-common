package com.xy.common.util;

import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;

/**
 * 将beancopier做成静态类，方便拷贝
 */
public class BeanUtils {

    /**
     * copier cache
     */
    private static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<String, BeanCopier>();

    /**
     * bean map cache
     */
    private static ConcurrentHashMap<Class<?>, BeanMap> beanMapMap = new ConcurrentHashMap<Class<?>, BeanMap>();

    /**
     * 将source的同名属性复制到target.适用于两个不同类型的对象
     *
     * @param a 资源类
     * @param b 目标类
     */
    public static void copyA2B(Object a, Object b) {
        BeanCopier copier = getBeanCopier(a, b);
        copier.copy(a, b, null);
    }

    /**
     * get beanCopies from source and target
     *
     * @param source source
     * @param target target
     * @return BeanCopies
     */
    private static BeanCopier getBeanCopier(Object source, Object target) {
        String key = source.getClass().toString() + target.getClass().toString();
        BeanCopier beanCopier = beanCopierMap.get(key);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(key, beanCopier);
        }
        return beanCopier;
    }

    /**
     * get bean map
     *
     * @param obj
     * @return
     */
    public static BeanMap getBeanMap(Object obj) {
        Class<?> clz = obj.getClass();
        BeanMap beanMap = beanMapMap.get(clz);
        if (beanMap == null) {
            beanMap = BeanMap.create(obj);
            beanMapMap.put(clz, beanMap);
        }
        return beanMap;
    }


    /**
     * 给target赋上source的非空属性值
     *
     * @param a
     * @param b
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T getDiffProperties(T a, T b) throws IllegalAccessException, InstantiationException {
        if (a == null || b == null) {
            return b;
        }
        BeanMap beanMap = getBeanMap(a);
        Object result = null;
        for (Object key : beanMap.keySet()) {
            Object targetValue = beanMap.get(b, key); //获取Target原值
            Object sourceValue = beanMap.get(a, key); //获取Source原值
            if (sourceValue != null && !sourceValue.equals(targetValue)) { //sourceValue不为空,两个值不相等
                if (targetValue == null || sourceValue.getClass().equals(targetValue.getClass())) {
                	if (null == result) {
                		result = a.getClass().newInstance();
                	}
                    beanMap.put(result, key, targetValue);
                }
            }
        }
        return (T) result;
    }

}