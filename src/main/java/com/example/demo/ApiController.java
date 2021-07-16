package com.example.demo;

import com.example.demo.api.ApiService;
import com.example.demo.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
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

    // 处理登录请求 REST
    @GetMapping(path = "/login")
    public ResponseEntity login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        String status = apiService.handleLogin(username, password);
        switch (status) {
            case "success":
                return ResponseEntity.status(HttpStatus.OK).body(new Message("200", status));
            case "username not exist":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("404", status));
            case "password error":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message("401", status));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message("500", "INTERNAL SERVER ERROR"));
        }
    }

    // 处理注册请求 REST
    @GetMapping(path = "/register")
    public ResponseEntity register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String phone
    ) {
        String status = apiService.handleRegister(username, password, email, phone);
        switch (status) {
            case "success":
                return ResponseEntity.status(HttpStatus.CREATED).body(new Message("201", status));
            case "invalid username":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("400", status));
            case "duplicate username":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("400", status));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message("500", "INTERNAL SERVER ERROR"));
        }
    }

    // 处理邮箱地址校验 REST
    @GetMapping(path = "/validate_email")
    public ResponseEntity validateEmail (
            @RequestParam String username,
            @RequestParam String email
    ) {
        String status = apiService.handleValidate(username, email);
        switch (status) {
            case "success":
                return ResponseEntity.status(HttpStatus.CREATED).body(new Message("201", status));
            case "username not exist":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("404", status));
            case "email error":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message("401", status));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message("500", "INTERNAL SERVER ERROR"));
        }
    }

    // 处理注册发送邮件验证码请求 REST
    @GetMapping(path = "/email_auth_code_reg")
    public ResponseEntity emailAuthCodeReg (
            @RequestParam String to // 目的邮箱
    ) {
        String checkCode = apiService.sendEmailAuthCodeByEmail(to);
        checkCode = DigestUtils.md5DigestAsHex(checkCode.getBytes()); // MD5处理
        return ResponseEntity.status(HttpStatus.OK).body(new Message("200", checkCode));
    }

    // 处理密码重置发送邮件验证码请求 REST
    @GetMapping(path = "/email_auth_code_reg_reset_password")
    public ResponseEntity emailAuthCodePwdReset (
            @RequestParam String username
    ) {
        String checkCode = apiService.sendEmailAuthCodeByUsername(username);
        checkCode = DigestUtils.md5DigestAsHex(checkCode.getBytes()); // MD5处理
        return ResponseEntity.status(HttpStatus.OK).body(new Message("200", checkCode));
    }

    // 处理用户密码重置 REST
    @GetMapping(path = "/reset_password")
    public ResponseEntity resetPassword (
            @RequestParam String username,
            @RequestParam String password
    ) {
        String status = apiService.handlePwdReset(username, password);
        switch (status) {
            case "success":
                return ResponseEntity.status(HttpStatus.CREATED).body(new Message("201", status));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message("500", "INTERNAL SERVER ERROR"));
        }
    }
}
