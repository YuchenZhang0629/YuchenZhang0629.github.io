package com.ucla.oneflow;

import com.ucla.oneflow.common.utils.ConfigUtil;
import com.ucla.oneflow.executor.service.HiveService;
import com.ucla.oneflow.model.ConfigVO;
import com.ucla.oneflow.model.StepVO;
import com.ucla.oneflow.model.TaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication
public class Application {


    @Autowired
    private ConfigUtil configUtil;
    @Autowired
    private HiveService hiveService;

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
            appLogger.info("Get task" + taskId + " 's Config");
            taskVO.getSteps().forEach(stepVO -> {
                appLogger.info("Get step" + stepVO.getOrder() + " 's Config");
                String type = stepVO.getType();
                if (type.equals("hive")) {
                    appLogger.info("Run Hive");
                    hiveService.runHql(stepVO.getPath(),stepVO.getHiveParam());
                }
            });
        });
    }
}
