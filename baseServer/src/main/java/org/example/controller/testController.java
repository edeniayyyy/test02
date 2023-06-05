package org.example.controller;

import org.example.annotation.logTest;
import org.example.common.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @GetMapping("/test")
    @logTest(test = "ddd")
    public ResultResponse test() {
        return ResultResponse.success();
    }
}
