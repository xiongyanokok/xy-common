package com.xy.common.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;

/**
 * 任务分片
 * 
 * @author xiongyan
 * @date 2016年9月18日 下午5:36:05
 */
public final class TaskShard {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(TaskShard.class);
	
	private TaskShard() {
	}
	
	/**
	 * job列表
	 */
	private static List<String> jobList;

	public static void setJobList(List<String> jobList) {
		TaskShard.jobList = jobList;
	}
	
	/**
	 * 判断是否可在本机上运行
	 * 
	 * @param shardingContext
	 * @return
	 */
	public static boolean isThisRun(JobExecutionMultipleShardingContext shardingContext) {
		if (null == jobList || jobList.size() == 0) {
			logger.error("没有任务可执行");
			return false;
		}
		
		// 分片总数
		int shardingTotalCount = shardingContext.getShardingTotalCount();
		// 本作业服务器的分片序列号集合
		List<Integer> shardingItems = shardingContext.getShardingItems();
		if (shardingTotalCount == 1) {
			// 分片数为1的时候直接返回成功（没有分片）
			return true;
		}
		
		// 任务名称
		String jobName = shardingContext.getJobName();
		// 获取该任务在任务列表中的索引位置
		int index = jobList.indexOf(jobName);
		// 任务索引位置对分片总数取余得到分片号
		int shardNo = index % shardingTotalCount; 
		
		// 判断该分片号是否在本作业服务器的分片序列号集合中
		if (shardingItems.contains(shardNo)) {
			return true;
		}
		return false;
	}

}
