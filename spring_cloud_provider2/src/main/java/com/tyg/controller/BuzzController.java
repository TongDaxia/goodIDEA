package com.tyg.controller;

import com.tyg.service.BuzzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuzzController {

    @Autowired
    private BuzzService buzzService;

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
}
