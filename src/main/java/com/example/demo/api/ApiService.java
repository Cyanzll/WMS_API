package com.example.demo.api;

import com.example.demo.dao.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.Random;

// 业务层
// 把它变成bean，交给Spring IoC容器管理
@Service
public class ApiService {

    private JavaMailSender mailSender;

    @Autowired
    public ApiService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 登录服务
    public String handleLogin(String username, String password) {
        // Accessing Data
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User u = mapper.getUserByUsername(username);
        System.out.println(u);
        sqlSession.close();
        // 不存在该用户
        if(u == null) {
            return "404";
        }
        // 密码比对
        // password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(password.equals(u.getPassword())) {
            return "200";
        } else return "201";
    }

    // 注册服务
    public String handleRegister(String username, String password, String email, String phone) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        // 过滤
        if(username.equals("") || username == null) {
            return "202";
        }
        // 验重
        if(mapper.getUserByUsername(username) != null) {
            return "201";
        }
        mapper.insertUser(username, password, email, phone);
        sqlSession.commit(); // 提交事务
        sqlSession.close();
        return "200";
    }

    // 发送邮件验证码
    public String sendEmailCode(String toEmail) {
        //生成随机验证码
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String username = "1162830359@qq.com";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(toEmail);
        message.setSubject("学生服务系统 - 电子邮箱验证码");
        message.setText("欢迎注册学生服务系统，您本次的验证码为：" + checkCode + "\n 如非本人操作，请忽略。");
        mailSender.send(message);
        return checkCode; // 返回验证码
    }

    // 发送邮件验证码
    public String sendEmailCodeReset(String username) {
        // 通过用户名取得邮箱地址
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User u = mapper.getUserByUsername(username);
        String toEmail = u.getEmail();
        sqlSession.close();

        //生成随机验证码
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String fromEmail = "1162830359@qq.com";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("学生服务系统 - 电子邮箱验证码");
        message.setText("以下验证码用于重置用户密码： " + checkCode + "\n 如非本人操作，请忽略。");
        mailSender.send(message);
        return checkCode; // 返回验证码
    }

    // 重置密码 - 邮箱验证
    public String handleValidate(String username, String email) {
        // Accessing Data
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User u = mapper.getUserByUsername(username);
        sqlSession.close();
        // 不存在该用户
        if(u == null) {
            return "404";
        }
        // 密码比对
        if(email.equals(u.getEmail())) {
            return "200";
        } else return "201";
    }

    // 重置密码
    public String handlePwdReset(String username, String password) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUserPwd(username, password);
        sqlSession.commit(); // 提交事务
        sqlSession.close();
        return "200";
    }

}
