package com.example.demo.api;

import com.example.demo.dao.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

// 业务层
// 把它变成bean，交给Spring IoC容器管理
@Service
public class ApiService {

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
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(password.equals(u.getPassword())) {
            return "200";
        } else return "201";
    }

    // 注册服务
    public String handleRegister(String username, String password) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        // 验重
        if(mapper.getUserByUsername(username) != null) {
            return "201";
        }
        mapper.insertUser(username, password);
        sqlSession.commit(); // 提交事务
        sqlSession.close();
        return "200";
    }

}
