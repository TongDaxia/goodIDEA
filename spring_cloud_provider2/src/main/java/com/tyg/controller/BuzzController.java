package com.tyg.controller;

import com.tyg.service.BuzzBaiduSerivce;
import com.tyg.service.BuzzSougouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuzzController {

    @Autowired
    private BuzzBaiduSerivce buzzService;
  @Autowired
    private BuzzSougouService buzzSougouService;

    @GetMapping(value = "/buzz/update")
    public String add() {
        try {
            buzzService.insertBang();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "true";
    }
    @GetMapping(value = "/buzz/updateSG")
    public String addSG() {
        try {
            buzzSougouService.insert();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "true";
    }
}
