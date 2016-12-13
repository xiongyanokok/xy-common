package com.xy.common.datasource;

/**
 * 上下文DbContextHolder为一线程安全的ThreadLocal
 * 
 * @author xiongyan
 * @date 2016年6月12日 上午10:51:11
 */
public class DBContextHolder {

	/**
	 * 可读写数据源
	 */
	public static final String WRITE_DATA_SOURCE = "writeDataSource";

	/**
	 * 只读数据源
	 */
	public static final String READ_DATA_SOURCE = "readDataSource";

	/**
	 * 本地线程局部变量
	 */
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	/**
	 * 切换只读数据源
	 */
	public static void setReadDB() {
		contextHolder.set(READ_DATA_SOURCE);
	}

	/**
	 * 是否只读数据源
	 * @return
	 */
	public static boolean isReadDB() {
		return READ_DATA_SOURCE.equals(getDB());
	}

	/**
	 * 切换可读写数据源
	 */
	public static void setWriteDB() {
		contextHolder.set(WRITE_DATA_SOURCE);
	}
	
	/**
	 * 是否可读写数据源
	 * @return
	 */
	public static boolean isWriteDB() {
		return WRITE_DATA_SOURCE.equals(getDB());
	}

	/**
	 * 获取数据源
	 * 
	 * @return
	 */
	public static String getDB() {
		return contextHolder.get();
	}

	/**
	 * 设置为null
	 */
	public static void reset() {
		contextHolder.set(null);
	}
}
