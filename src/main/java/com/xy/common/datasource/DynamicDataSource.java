package com.xy.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * 
 * @author xiongyan
 * @date 2016年6月12日 上午10:55:28
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	/**
	 * 获取数据源
	 */
	protected Object determineCurrentLookupKey() {
		return DBContextHolder.getDB();
	}

}
