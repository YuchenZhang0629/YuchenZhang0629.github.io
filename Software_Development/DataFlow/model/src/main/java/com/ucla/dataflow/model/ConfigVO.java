package com.ucla.dataflow.model;

import javax.sql.DataSource;
import java.util.List;

public class ConfigVO {
    private Integer projectId;
    private String projectName;
    private List<DataSource> datasources;
    private List<TaskVO> tasks;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<DataSource> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<DataSource> datasources) {
        this.datasources = datasources;
    }

    public List<TaskVO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskVO> tasks) {
        this.tasks = tasks;
    }

    class DataSource{
        private String url;
        private String userName;
        private String passWord;
        private String driver;
        private String name;
    }
}
