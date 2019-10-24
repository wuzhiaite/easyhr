package com.springboot.easyhr.modular.biz.controller;

import com.alibaba.fastjson.JSON;
import com.springboot.easyhr.modular.biz.model.TaskInfo;
import com.springboot.easyhr.modular.biz.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="任务调度调用controller",tags={"TaskManageController"})
@Controller
public class TaskManageController {
    @Autowired(required=false)
    private TaskService taskService;

    /**
     * Index.jsp
     */
    @RequestMapping(value={"", "/", "index"})
    public String info(){
        return "index.html";
    }

    /**
     * 任务列表
     * @return
     */
    @ApiOperation(value="所有定时任务列表",notes="所有定时任务列表数据")
    @ApiImplicitParam(name="",value="",required=false,dataType="",paramType = "")//需要注入的参数
    @ResponseBody
    @RequestMapping(value="list")
    public String list(){
        Map<String, Object> map = new HashMap<>();
        List<TaskInfo> infos = taskService.list();
        map.put("rows", infos);
        map.put("total", infos.size());
        return JSON.toJSONString(map);
    }

    /**
     * 保存定时任务
     * @param info
     */
    @ApiOperation(value="保存定时任务信息",notes="定时任务的参数为对应的taskInfo")
    @ApiImplicitParam(name="taskInfo",value="对应的是taskInfo",required=false,dataType = "TaskInfo")
    @ResponseBody
        @RequestMapping(value="save", produces = "application/json; charset=UTF-8",method= {RequestMethod.POST,RequestMethod.GET})
        public String save(TaskInfo info){
        try {
            if(info.getId() == 0) {
                taskService.addJob(info);
            }else{
                taskService.edit(info);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "成功";
    }

    /**
     * 删除定时任务
     * @param jobName
     * @param jobGroup
     */
    @ResponseBody
    @RequestMapping(value="delete/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
    public String delete(@PathVariable String jobName, @PathVariable String jobGroup){
        try {
            taskService.delete(jobName, jobGroup);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "成功";
    }

    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     */
    @ResponseBody
    @RequestMapping(value="pause/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
    public String pause(@PathVariable String jobName, @PathVariable String jobGroup){
        try {
            taskService.pause(jobName, jobGroup);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "成功";
    }

    /**
     * 重新开始定时任务
     * @param jobName
     * @param jobGroup
     */
    @ResponseBody
    @RequestMapping(value="resume/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
    public String resume(@PathVariable String jobName, @PathVariable String jobGroup){
        try {
            taskService.resume(jobName, jobGroup);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "成功";
    }





}
