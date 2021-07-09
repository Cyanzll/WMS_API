package com.example.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin // 解决前端跨域问题
@RestController
@RequestMapping(path = "/api")
public class ApiController {
    // 此处为对象间的依赖关系，必须用**私有属性**来引用
    private final ApiService apiService;

    // constructor
    // 依赖注入 - 通过构造函数的参数传入
    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    // 登录
    @GetMapping(path = "/login")
    public @ResponseBody String loginStatus (
        @RequestParam String username,
        @RequestParam String password
    ) {
        String status = apiService.handleLogin(username, password);
        switch (status) {
            case "200": return "success";
            case "201": return "password error";
            default: return "unknown username";
        }
    }

    // 注册
    @GetMapping(path = "/reg")
    public @ResponseBody String RegisterStatus (
        @RequestParam String username,
        @RequestParam String password
    ) {
        String status = apiService.handleRegister(username, password);
        switch (status) {
            case "200": return "success";
            case "201": return "duplicate username";
            default: return "failed";
        }
    }

}
