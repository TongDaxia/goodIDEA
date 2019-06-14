package com.tyg.controller;

import com.tyg.service.BuzzService;
import com.tyg.task.BuzzProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuzzController {

    @Autowired
    private BuzzProcess buzzService;

    @GetMapping(value = "/buzz/update")
    public String add() {
        try {
            buzzService.init();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "true";
    }
}
