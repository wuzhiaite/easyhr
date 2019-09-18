package com.springboot.easyhr.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * 分布式任务管理配置
 *
 */
@Configuration
public class QuartzConfig {


    @Autowired
    DataSource dataSource;


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(QuartJobFactory myJobFactory) throws Exception {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        //使job实例支持spring 容器管理
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(myJobFactory);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        // 延迟10s启动quartz
        schedulerFactoryBean.setStartupDelay(10);

        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws IOException, SchedulerException {
//		SchedulerFactory schedulerFactory = new StdSchedulerFactory(quartzProperties());
//		Scheduler scheduler = schedulerFactory.getScheduler();
//		scheduler.start();//初始化bean并启动scheduler
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * 设置quartz属性
     * @return
     */
    private Properties quartzProperties() {
        Properties prop = new Properties();
        prop.put("quartz.scheduler.instanceName", "ServerScheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
        prop.put("org.quartz.scheduler.instanceId", "NON_CLUSTERED");
        prop.put("org.quartz.scheduler.jobFactory.class", "org.quartz.simpl.SimpleJobFactory");
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        prop.put("org.quartz.jobStore.isClustered", "true");
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "5");

//		prop.put("org.quartz.dataSource.quartzDataSource.driver", druidProperties.getDriverClassName());
//		prop.put("org.quartz.dataSource.quartzDataSource.URL", druidProperties.getUrl());
//		prop.put("org.quartz.dataSource.quartzDataSource.user", druidProperties.getUsername());
//		prop.put("org.quartz.dataSource.quartzDataSource.password", druidProperties.getPassword());
//		prop.put("org.quartz.dataSource.quartzDataSource.maxConnections", druidProperties.getMaxActive());
        return prop;
    }


}
