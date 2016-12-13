package com.xy.common.task;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;

/**
 * spring容器初始化完成后执行
 * 
 * @author xiongyan
 * @date 2016年9月18日 上午9:51:36
 */
public class SpringInitializingBean implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * 注册中心
	 */
	private ZookeeperRegistryCenter registryCenter;

	public ZookeeperRegistryCenter getRegistryCenter() {
		return registryCenter;
	}

	public void setRegistryCenter(ZookeeperRegistryCenter registryCenter) {
		this.registryCenter = registryCenter;
	}

	
	/**
	 * 当spring容器初始化完成后执行该方法
	 */
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 项目加载完成后，从注册中心获取所有的任务列表
		TaskShard.setJobList(registryCenter.getChildrenKeys("/"));
	}

}
