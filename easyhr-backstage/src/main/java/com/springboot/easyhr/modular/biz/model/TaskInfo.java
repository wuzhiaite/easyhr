package com.springboot.easyhr.modular.biz.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description="定时任务信息实体类",value="TaskInfo")
@Data
public class TaskInfo {


    private static final long serialVersionUID = -8054692082716173379L;
    private int id = 0;

    /**任务名称*/
    @ApiModelProperty(name="jobName",value="定时任务名称",required = true)
    private String jobName;

    /**任务分组*/
    @ApiModelProperty(name="jobName",value="任务分组",required = true)
    private String jobGroup;

    /**任务描述*/
    @ApiModelProperty(name="jobDescription",value="定时任务描述",required = true)
    private String jobDescription;

    /**任务状态*/
    @ApiModelProperty(name="jobStatus",value="任务状态",required = true)
    private String jobStatus;

    /**任务表达式*/
    @ApiModelProperty(name="cronExpression",value="任务表达式",required=true)
    private String cronExpression;

    @ApiModelProperty(name="cronExpression",value="创建时间",required=false)
    private String createTime;




}
