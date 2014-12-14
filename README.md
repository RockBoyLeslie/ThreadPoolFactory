ThreadPoolFactory
=================

1. 功能
线程池配置工厂，  支持配置化方式创建和管理线程池， 并对运行中的线程池提供监控任务。目前支持的功能有
.  配置化生产线程池
.  线程池状态监控
.  线程组中的线程状态监控

用户可根据需求定制配置自己的监控任务。

2. 安装方法
checkout  
mvn clean install

3. 测试方法
.   测试文件 config/threadPool.xml 在定制自己的线程池和监控任务（需要新增的话)
.   运行 src/test/com.pingan.common.thread.pool.ThreadPoolContextTest

<?xml version="1.0" encoding="UTF-8"?>
<pools>
	<!-- 线程监控任务组，  目前提供线程池状态监控和线程池中的线程状态监控，  用于可以实现AbstractStateMonitor 并配置添加自己的监控任务 -->
	<monitors initialDelay="5" period="60">
		<monitor name="threadPoolMonitor"
			class="com.pingan.common.thread.pool.state.ThreadPoolStateMonitor" />
		<monitor name="threadMonitor"
			class="com.pingan.common.thread.pool.state.ThreadStateMonitor" />
	</monitors>

	<!-- 默认的业务线程池 -->
	<pool name="default">
		<attribute name="corePoolSize" value="10" />
		<attribute name="maxPoolSize" value="100" />
		<attribute name="keepAliveTime" value="15" />
		<attribute name="workQueueSize" value="100000" />
	</pool>

	<!-- amesb 基金服务线程池 -->
	<pool name="amesbFundService">
		<attribute name="corePoolSize" value="10" />
		<attribute name="maxPoolSize" value="30" />
		<attribute name="keepAliveTime" value="15" />
		<attribute name="workQueueSize" value="5000" />
	</pool>

</pools>
