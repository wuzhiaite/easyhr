package com.springboot.easyhr.modular.biz.model;

import lombok.Data;

@Data
public class TaskInfo {


    private static final long serialVersionUID = -8054692082716173379L;
    private int id = 0;

    /**任务名称*/
    private String jobName;

    /**任务分组*/
    private String jobGroup;

    /**任务描述*/
    private String jobDescription;

    /**任务状态*/
    private String jobStatus;

    /**任务表达式*/
    private String cronExpression;

    private String createTime;




}
