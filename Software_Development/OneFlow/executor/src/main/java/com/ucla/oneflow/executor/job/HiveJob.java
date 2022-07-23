package com.ucla.oneflow.executor.job;

import com.ucla.oneflow.Application;
import com.ucla.oneflow.executor.service.HiveService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HiveJob implements Job{
    private static final Logger appLogger = LoggerFactory.getLogger(HiveJob.class);
    @Autowired
    private HiveService hiveService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap paramMap = jobExecutionContext.getMergedJobDataMap();
        appLogger.info("Run Hive");
        hiveService.runHql(paramMap.getString("path"), (Map<String, Object>) paramMap.get("hiveParam"));
    }
}
