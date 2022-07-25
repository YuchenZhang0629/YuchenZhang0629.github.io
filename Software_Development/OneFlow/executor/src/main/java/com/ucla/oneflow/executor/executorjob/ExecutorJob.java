package com.ucla.oneflow.executor.executorjob;

import com.ucla.oneflow.executor.job.*;
import com.ucla.oneflow.executor.listener.OrderListener;
import com.ucla.oneflow.model.JobDescriptor;
import com.ucla.oneflow.model.VO.StepVO;
import com.ucla.oneflow.model.VO.TaskVO;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ExecutorJob implements Job {
    private static final Logger appLogger = LoggerFactory.getLogger(ExecutorJob.class);
    @Autowired
    private Scheduler scheduler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap paramMap = jobExecutionContext.getMergedJobDataMap();
        String taskName = paramMap.getString("taskName");
        String taskId = paramMap.getString("taskId");
        TaskVO taskVO = (TaskVO) paramMap.get("taskVO");
        String group = paramMap.getString("group");
        OrderListener orderListener = new OrderListener(taskName + " OrderListener");


        appLogger.info("Get task" + taskId + " 's Config");
        taskVO.getSteps().sort(new Comparator<StepVO>() {
            @Override
            public int compare(StepVO o1, StepVO o2) {
                return Integer.parseInt(o1.getOrder())-Integer.parseInt(o2.getOrder());
            }
        });
        Queue<JobDetail> jobDetailQueue = new LinkedList<>();
        // Use a hashmap to check whether we have finished step1
        // If step1 and step2 have the same order hierarchy
        Map<String,String> statusMap = new HashMap<>();
        taskVO.getSteps().forEach(stepVO -> {
            appLogger.info("Get step" + stepVO.getOrder() + " 's Config");
            statusMap.put(stepVO.getStepName(),"new");
            String type = stepVO.getType();
            JobDescriptor jobDescriptor = new JobDescriptor();
            jobDescriptor.setGroup(group);
            jobDescriptor.setName(taskName);
            jobDescriptor.setName(stepVO.getStepName());
            paramMap.put("order",stepVO.getOrder());
            paramMap.put("stepName",stepVO.getStepName());
            if (type.equalsIgnoreCase("hive")) {
                jobDescriptor.setJobClazz(HiveJob.class);
                paramMap.put("path",stepVO.getPath());
                paramMap.put("hiveParam", stepVO.getHiveParam());
            } else if (type.equalsIgnoreCase("spark")) {
                jobDescriptor.setJobClazz(ScriptJob.class);
                paramMap.put("path",stepVO.getPath());
                paramMap.put("hiveParam", stepVO.getParam());
                paramMap.put("mode", stepVO.getMode());
            } else if (type.equalsIgnoreCase("script")) {
                jobDescriptor.setJobClazz(SparkJob.class);
                paramMap.put("path",stepVO.getPath());
                paramMap.put("master", stepVO.getMaster());
                paramMap.put("deployMode", stepVO.getDeployMode());
                paramMap.put("className", stepVO.getClassName());
                paramMap.put("sparkLogPath",stepVO.getSparkLogPath());
            } else if (type.equalsIgnoreCase("HDFS")) {
                jobDescriptor.setJobClazz(HdfsJob.class);
                paramMap.put("mode",stepVO.getMode());
                paramMap.put("source", stepVO.getSource());
                paramMap.put("destination", stepVO.getDestination());
            } else if (type.equalsIgnoreCase("function")) {
                jobDescriptor.setJobClazz(FuncJob.class);
                paramMap.put("path",stepVO.getPath());
                paramMap.put("className", stepVO.getClassName());
                paramMap.put("methodName", stepVO.getMethodName());
                Map<String,String> funcParamMap = new HashMap<>();
                if (stepVO.getFuncParamList() != null) {
                    stepVO.getFuncParamList().forEach(str -> {
                        String[] tempArr = str.split("=");
                        funcParamMap.put(tempArr[0],tempArr[1]);
                    });
                    paramMap.put("funcParamList", funcParamMap);
                }
            } else if (type.equalsIgnoreCase("dataLoader")) {
                jobDescriptor.setJobClazz(DataLoaderJob.class);
                paramMap.put("sourceDataSource",stepVO.getSourceDatasource());
                paramMap.put("destDataSource",stepVO.getDestDatasource());
                paramMap.put("sourcePath", stepVO.getSourcePath());
                paramMap.put("sourceTables", stepVO.getSourceTables());
                paramMap.put("destTables",stepVO.getDestTables());
                paramMap.put("sourceCSV", stepVO.getSourceCSV());

            }
            jobDescriptor.setDataMap(paramMap);
            JobDetail jobDetail =jobDescriptor.buildJobDetail();
            jobDetailQueue.add(jobDetail);
        });
        // Add another queue to check whether there is another step
        // with the same order hierarchy
        List<String> runningJobList = new ArrayList<>();
        orderListener.setJobDetailQueue(jobDetailQueue);
        orderListener.setStatusMap(statusMap);
        orderListener.setRunningJobList(runningJobList);
        try {
            scheduler.getListenerManager().addJobListener(orderListener);
            if (jobDetailQueue.isEmpty()) {
                appLogger.warn(taskName + "'s Step List is empty");
                return;
            }
            while (!jobDetailQueue.isEmpty() && jobDetailQueue.peek().getJobDataMap().getString("order").equals("1")){
                // When the task start working, update the status to "processing"
                JobDetail initJobDetail = jobDetailQueue.poll();
                String stepName = initJobDetail.getJobDataMap().getString("stepName");
                runningJobList.add(stepName);
                statusMap.put(stepName,"processing");
                appLogger.info(stepName + " is running");
                scheduler.addJob(initJobDetail,true);
                scheduler.triggerJob(initJobDetail.getKey());
            }
        } catch (Exception e) {
            appLogger.error("Error",e);
        }
    }
}
