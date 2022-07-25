package com.ucla.oneflow;

import com.ucla.oneflow.common.utils.ConfigUtil;
import com.ucla.oneflow.executor.executorjob.ExecutorJob;
import com.ucla.oneflow.executor.job.HiveJob;
import com.ucla.oneflow.executor.listener.OrderListener;
import com.ucla.oneflow.executor.service.HiveService;
import com.ucla.oneflow.model.JobDescriptor;
import com.ucla.oneflow.model.VO.ConfigVO;
import com.ucla.oneflow.model.VO.StepVO;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.sql.Driver;
import java.util.*;

@SpringBootApplication
public class Application {


    @Autowired
    private ConfigUtil configUtil;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private JdbcTemplate hiveJdbcTemplate;
    @Autowired
    private HiveTemplate hiveTemplate;

    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @PostConstruct
    public void run() throws FileNotFoundException {
        // Adding a logger to track the pipeline's operation
        // We also have debug, warning, and error
        appLogger.info("OneFlow starting to operate");
        ConfigVO configVO = configUtil.getConfigVO();
        appLogger.info("Get ConfigVO");
        List<ConfigVO.DataSource> dataSources = configVO.getDatasources();
        appLogger.info("Get Data Source Config");
        configVO.getTasks().forEach(taskVO -> {
            String taskId = taskVO.getTaskId();
            String taskName = taskVO.getTaskName();
            String cron = taskVO.getCron();
            String group = taskVO.getGroup();
            JobDescriptor jobDescriptor = new JobDescriptor();
            jobDescriptor.setGroup(group);
            jobDescriptor.setName(taskName);
            jobDescriptor.setJobClazz(ExecutorJob.class);
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("taskName",taskName);
            paramMap.put("taskId",taskId);
            paramMap.put("cron ",cron);
            paramMap.put("taskVO ",taskVO);
            paramMap.put("group",group);
            paramMap.put("dataSourceMap",getDataSourceMap(dataSources));
            jobDescriptor.setDataMap(paramMap);
            JobDetail executorJobDetail = jobDescriptor.buildJobDetail();

            Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(taskName,group).
                    withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();

            try {
                scheduler.scheduleJob(executorJobDetail,jobTrigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
    }
    public Map<String,Object> getDataSourceMap(List<ConfigVO.DataSource> dataSources){
        Map<String,Object> dataSourceMap = new HashMap<>();
        dataSources.forEach(dataSource -> {
            if (dataSource.getName().equals("hive")) {
                dataSourceMap.put("hive",hiveJdbcTemplate);
                dataSourceMap.put("hiveTemplate",hiveTemplate);
            } else {
                SimpleDriverDataSource dS = new SimpleDriverDataSource();
                Class<?> cls = null;
                try {
                    // Also perform some boundary checks here
                    cls = Class.forName(dataSource.getDriver());
                    dS.setDriverClass((Class<? extends Driver>) cls);
                    dS.setUrl(dataSource.getUrl());
                    dS.setUsername(dataSource.getUsername());
                    dS.setPassword(dataSource.getPassword());
                    JdbcTemplate jtm = new JdbcTemplate();
                    dataSourceMap.put(dataSource.getName(),jtm);
                } catch (Exception e) {
                    appLogger.error("Error",e);
                }
            }
        });
        return dataSourceMap;
    }
}
