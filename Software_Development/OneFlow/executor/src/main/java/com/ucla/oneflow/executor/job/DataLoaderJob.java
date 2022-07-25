package com.ucla.oneflow.executor.job;

import com.ucla.oneflow.executor.handler.DataTransferHandler;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataLoaderJob implements Job {
    private static final Logger appLogger = LoggerFactory.getLogger(DataLoaderJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        appLogger.info("Begin to run DataLoader job");
        JobDataMap paramMap=jobExecutionContext.getMergedJobDataMap();
        Map<String,Object> dataSourceMap = (Map<String, Object>) paramMap.get("dataSourceMap");
        try {
            JdbcTemplate sourceJtm = (JdbcTemplate) dataSourceMap.get(paramMap.getString("sourceDataSource"));
            JdbcTemplate destJtm = (JdbcTemplate) dataSourceMap.get(paramMap.getString("destDataSource"));
            List<String> sourceTables = (List<String>) paramMap.get("sourceTables");
            List<String> destTables = (List<String>) paramMap.get("destTables");
            if (sourceTables == null || destTables == null || sourceTables.isEmpty() || destTables.isEmpty() || sourceTables.size() != destTables.size()) {
                throw new Exception("Source table list and dest table list must be non-empty and have the same size");
            }
            for (int i = 0; i < sourceTables.size();i++) {
                String sourceTable = sourceTables.get(i);
                String destTable = destTables.get(i);
                // If the database is really large, we cannot save all of them
                // Instead we do this
                DataTransferHandler dataTransferHandler = new DataTransferHandler(destJtm,destTable);
                sourceJtm.query("select * from " + sourceTable,dataTransferHandler);
                dataTransferHandler.saveRest();
            }
            appLogger.info("DataLoader job is done");
        } catch (Exception e) {
            appLogger.error("Error:",e);
        }
    }

}
