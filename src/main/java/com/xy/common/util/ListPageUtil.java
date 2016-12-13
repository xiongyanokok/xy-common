package com.xy.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 集合分页
 */
public class ListPageUtil {

	/**
	 * 计算页数
	 * 
	 * @param totalRows  总个数
	 * @param pageRows   每页显示个数
	 * @return
	 */
	public static int getTotalPages(int totalRows, int pageRows) {
		if (totalRows <= 0 || pageRows <= 0) {
			return 0;
		}
		if (totalRows % pageRows == 0) {
			return totalRows / pageRows;
		} else {
			return totalRows / pageRows + 1;
		}
    }
	
	/**
	 * 集合分页，返回多个集合
	 * 
	 * @param list      原始集合
	 * @param pageRows  每页显示个数
	 * @return
	 */
	public static <T> List<List<T>> listPage(List<T> list, int pageRows) {
		if (null == list || list.size() == 0) {
			return null;
		}

		// 如果集合个数<=每页显示个数 直接返回该集合
		if (list.size() <= pageRows) {
			return Arrays.asList(list);
		}

		// 总个数
		int totalRows = list.size();
		// 页数
		int totalPages = getTotalPages(totalRows, pageRows);
		List<List<T>> pageList = new ArrayList<List<T>>();
		for (int i = 0; i < totalPages; i++) {
			int fromIndex = pageRows * i; // 开始索引位置
			int toIndex = pageRows * (i + 1) > totalRows ? totalRows : pageRows * (i + 1); // 结束索引位置
			pageList.add(list.subList(fromIndex, toIndex));
		}
		return pageList;
	}
}
