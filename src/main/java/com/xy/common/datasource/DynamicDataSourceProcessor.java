package com.xy.common.datasource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 动态数据源处理器
 * 
 * @author xiongyan
 * @date 2016年6月12日 上午10:50:33
 */
public class DynamicDataSourceProcessor implements BeanPostProcessor {
	
	/**
	 * logger
	 */
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceProcessor.class);
    
	/**
     * 只读方法缓存
     */
    private Map<String, Boolean> readMethodMap = new HashMap<String, Boolean>();

	/**
     * 初始化之前执行
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
	/**
	 * 初始化之后执行
	 */
    @SuppressWarnings("unchecked")
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (!(bean instanceof NameMatchTransactionAttributeSource)) {
            return bean;
        }
        
        try {
			NameMatchTransactionAttributeSource transactionAttributeSource = (NameMatchTransactionAttributeSource) bean;
            Field nameMapField = ReflectionUtils.findField(NameMatchTransactionAttributeSource.class, "nameMap");
            nameMapField.setAccessible(true);
			Map<String, TransactionAttribute> nameMap = (Map<String, TransactionAttribute>) nameMapField.get(transactionAttributeSource);
            
			for (Entry<String, TransactionAttribute> entry : nameMap.entrySet()) {
				RuleBasedTransactionAttribute attr = (RuleBasedTransactionAttribute) entry.getValue();

				// 仅对read-only=true的处理
				if (!attr.isReadOnly()) {
					continue;
				}

				String methodName = entry.getKey();
				if (!"*".equals(methodName)) {
					Boolean isRead = Boolean.TRUE;
					logger.info("read/write transaction process  method:{} force read:{}", methodName, isRead);
					readMethodMap.put(methodName, isRead);
				}
            }
        } catch (Exception e) {
        	logger.error("process read/write transaction error", e);
            throw new RuntimeException("process read/write transaction error", e);
        }
        return bean;
    }
    
    
    /**
     * 确定数据源
     * @param pjp
     * @return
     * @throws Throwable
     */
	public Object determineReadOrWriteDB(ProceedingJoinPoint pjp) throws Throwable {
		if (isChoiceReadDB(pjp.getSignature().getName())) {
			// 只读
			DBContextHolder.setReadDB();
		} else {
			// 可读写
			DBContextHolder.setWriteDB();
		}

		try {
			// 执行方法
			return pjp.proceed();
		} finally {
			// 还原
			//DBContextHolder.reset();
		}

	}

	/**
	 * 是否选择可读写数据库
	 * @param methodName
	 * @return
	 */
	private boolean isChoiceReadDB(String methodName) {
		// 如果之前选择了写库 现在还选择 写库
		if (DBContextHolder.isWriteDB()) {
			return false;
		}
		
		String bestNameMatch = null;
		for (String mappedName : this.readMethodMap.keySet()) {
			if (isMatch(methodName, mappedName)) {
				bestNameMatch = mappedName;
				break;
			}
		}

		Boolean isRead = readMethodMap.get(bestNameMatch);
		// 表示应该选择 读库
		if (null != isRead && isRead == Boolean.TRUE) {
			return true;
		}
		
		// 默认 写库
		return false;
	}

	/**
	 * 是否符合
	 * @param methodName
	 * @param mappedName
	 * @return
	 */
	private boolean isMatch(String methodName, String mappedName) {
		return PatternMatchUtils.simpleMatch(mappedName, methodName);
	}

}

