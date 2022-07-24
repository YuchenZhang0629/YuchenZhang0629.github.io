package com.ucla.oneflow.executor.service.impl;

import com.ucla.oneflow.executor.service.HiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.data.hadoop.hive.HiveScript;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// @Service is necessary for the class to be handled by Spring
@Service
public class HiveServiceImpl implements HiveService {
    private static final Logger appLogger = LoggerFactory.getLogger(HiveServiceImpl.class);
    @Autowired
    private JdbcTemplate hiveJdbcTemplate;
    @Autowired
    private HiveTemplate hiveTemplate;
    @Override
    public void execute(String sql) {
        appLogger.info("Run Hive Execute");
        appLogger.info(sql);
        hiveJdbcTemplate.execute("select * from employee");
        appLogger.info("Hive SQL Executed");
    }
    @Override
    public List<Map<String,Object>> queryForList(String sql){
        appLogger.info("Run Hive QueryForList");
        List<Map<String,Object>> resMap = hiveJdbcTemplate.queryForList(sql);
        appLogger.info("Hive SQL Executed");
        return resMap;
    }
    @Override
    public Map<String, Object> queryForMap(String sql){
        appLogger.info("Run Hive QueryForMap");
        Map<String,Object> resMap = hiveJdbcTemplate.queryForMap(sql);
        appLogger.info("Hive SQL Executed");
        return resMap;
    }
    @Override
    public void runHql(String filePath, Map<String, Object> paramMap) {
        appLogger.info("Run Hive RunHql");
        HiveScript hiveScript = new HiveScript(new PathResource(filePath),paramMap);
        hiveTemplate.executeScript(hiveScript);
        appLogger.info("Hql Run Success");
    }
}
