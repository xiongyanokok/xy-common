package com.xy.common.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

/**
 * 任务的抽象类，所有任务都需要继承此类
 * 
 * @author xiongyan
 * @date 2016年9月18日 上午11:00:02
 */
public abstract class AbstractTask extends AbstractSimpleElasticJob {

	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractTask.class);

	/**
	 * 作业运行时上下文信息
	 */
	public ThreadLocal<JobExecutionMultipleShardingContext> jobContext = new ThreadLocal<JobExecutionMultipleShardingContext>();
	
	
	/**
	 * 执行作业
	 * 
	 * @param shardingContext  作业运行时多片分片上下文
	 */
	public void process(JobExecutionMultipleShardingContext shardingContext) {
		// 任务分片计算，是否在本服务器上执行
		boolean isRun = TaskShard.isThisRun(shardingContext);
		if (isRun) {
			// 任务开始时间
			long start = System.currentTimeMillis();
			try {
				// 设置值
				jobContext.set(shardingContext);
				logger.info("任务【{}】执行开始！", this.getTaskName());
				// 执行任务
				this.doExecute();
			} catch (Exception e) {
				logger.error("任务【{}】执行失败！", this.getTaskName(), e);
			} finally {
				// 任务结束时间
				long end = System.currentTimeMillis();
				logger.info("任务【{}】执行结束！用时：{}秒", this.getTaskName(), (end - start) / 1000.000);
				// 移除值
				jobContext.remove();
			}
		}
	}

	/**
	 * 任务名称
	 * 
	 * @return
	 */
	public abstract String getTaskName();

	/**
	 * 具体执行的任务
	 */
	public abstract void doExecute();

}
