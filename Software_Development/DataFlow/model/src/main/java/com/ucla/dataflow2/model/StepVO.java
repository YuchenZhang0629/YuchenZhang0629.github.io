package com.ucla.dataflow2.model;

import java.util.List;
import java.util.Map;

public class StepVO {
    private String order;
    private String stepName;
    private String type;
    private String path;

    // hive
    private Map<String,String> hiveParam;

    // spark
    private String master;
    private String deployMode;
    private String className;


    // hdfs
    private String mode;
    private String source;
    private String destination;


    // script
    private String param;


    // dataloader
    private String sourceDatasource;
    private String destDatasource;
    private String sourcePath;
    private List<String> sourceTables;
    private List<String> destTables;
    private List<String> sourceCSV;


    // custom
    private String function;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getHiveParam() {
        return hiveParam;
    }

    public void setHiveParam(Map<String, String> hiveParam) {
        this.hiveParam = hiveParam;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDeployMode() {
        return deployMode;
    }

    public void setDeployMode(String deployMode) {
        this.deployMode = deployMode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getSourceDatasource() {
        return sourceDatasource;
    }

    public void setSourceDatasource(String sourceDatasource) {
        this.sourceDatasource = sourceDatasource;
    }

    public String getDestDatasource() {
        return destDatasource;
    }

    public void setDestDatasource(String destDatasource) {
        this.destDatasource = destDatasource;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public List<String> getSourceTables() {
        return sourceTables;
    }

    public void setSourceTables(List<String> sourceTables) {
        this.sourceTables = sourceTables;
    }

    public List<String> getDestTables() {
        return destTables;
    }

    public void setDestTables(List<String> destTables) {
        this.destTables = destTables;
    }

    public List<String> getSourceCSV() {
        return sourceCSV;
    }

    public void setSourceCSV(List<String> sourceCSV) {
        this.sourceCSV = sourceCSV;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}

