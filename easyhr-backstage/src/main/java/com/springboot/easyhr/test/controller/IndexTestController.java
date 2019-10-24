package com.springboot.easyhr.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexTestController {

        @RequestMapping("/indexPage")
        public String indexPage(){
            return "this is the indexPage";
        }

        @RequestMapping("/middleMethod")
        public void middleMethod(){
            System.out.println(" this is a middleMethod ++++ ");
        }




}
