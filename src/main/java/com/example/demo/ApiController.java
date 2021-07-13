package com.example.demo;

import com.example.demo.api.ApiService;
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
        @RequestParam String password,
        @RequestParam String email,
        @RequestParam String phone
        ) {
        String status = apiService.handleRegister(username, password, email, phone);
        switch (status) {
            case "200": return "success";
            case "201": return "duplicate username";
            default: return "failed";
        }
    }

    // 密码重置 - 邮箱验证
    @GetMapping(path = "/validate")
    public @ResponseBody String ValidateEmail (
            @RequestParam String username,
            @RequestParam String email
    ) {
        String status = apiService.handleValidate(username, email);
        switch (status) {
            case "200": return "success";
            default: return "wrong email";
        }
    }

    // 发送邮件验证码
    @GetMapping(path = "/email")
    public @ResponseBody String sendEmail (
            @RequestParam String to // 目的邮箱
    ) {
        String checkCode = apiService.sendEmailCode(to);
        return checkCode;
    }

    // 发送邮件验证码
    @GetMapping(path = "/email_reset")
    public @ResponseBody String sendEmailReset (
            @RequestParam String username
    ) {
        String checkCode = apiService.sendEmailCodeReset(username);
        return checkCode;
    }

    // 重置密码
    @GetMapping(path = "/reset")
    public @ResponseBody String resetPwd (
            @RequestParam String username,
            @RequestParam String password
    ) {
        String status = apiService.handlePwdReset(username, password);
        switch (status) {
            case "200": return "success";
            default: return "failed";
        }
    }
}
