package com.ucla.oneflow.executor.listener;

import com.ucla.oneflow.Application;
import org.quartz.*;
import org.quartz.listeners.JobChainingJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class OrderListener extends JobChainingJobListener {

    private static final Logger appLogger = LoggerFactory.getLogger(OrderListener.class);
    private Queue<JobDetail> jobDetailQueue;
    private Map<String,String> statusMap;
    private List<String> runningJobList;

    public OrderListener(String name) {super(name);}

    public Queue<JobDetail> getJobDetailQueue() {
        return jobDetailQueue;
    }

    public void setJobDetailQueue(Queue<JobDetail> jobDetailQueue) {
        this.jobDetailQueue = jobDetailQueue;
    }

    public Map<String, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<String, String> statusMap) {
        this.statusMap = statusMap;
    }

    public List<String> getRunningJobList() {
        return runningJobList;
    }

    public void setRunningJobList(List<String> runningJobList) {
        this.runningJobList = runningJobList;
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        JobDetail curJobDetail = jobExecutionContext.getJobDetail();
        String curStepName = curJobDetail.getJobDataMap().getString("stepName");
        if (curStepName == null) return;
        // After the task is done, update the status in the hash map to complete
        statusMap.put(curStepName,"complete");
        appLogger.info(curStepName + " is done");
        runningJobList.remove(curStepName);
        // Only when all tasks of the same order hierarchy are complete can we move to next order
        if (runningJobList.isEmpty()){
            try {
                if (!jobDetailQueue.isEmpty()){
                    JobDetail nextJobDetail = jobDetailQueue.peek();
                    String nextOrder = nextJobDetail.getJobDataMap().getString("order");
                    // See whether a task leads to more than one task with the same order hierarchy
                    while (!jobDetailQueue.isEmpty() && jobDetailQueue.peek().getJobDataMap().getString("order").equals(nextOrder)){
                        nextJobDetail = jobDetailQueue.poll();
                        String nextStepName = nextJobDetail.getJobDataMap().getString("stepName");
                        appLogger.info(nextStepName + " is fetched and running");
                        jobExecutionContext.getScheduler().addJob(nextJobDetail,true);
                        runningJobList.add(nextStepName);
                        jobExecutionContext.getScheduler().triggerJob(nextJobDetail.getKey());
                    }
                } else {
                    appLogger.info("All jobs are done");
                }
            } catch (Exception ex) {
                appLogger.error("Error: ", ex);
            }
        } else {
            appLogger.info("Waiting for other tasks to be completed");
        }

    }
}
