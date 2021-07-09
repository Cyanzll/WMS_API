package com.example.demo;

import com.example.demo.dao.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.util.List;

// 测试通过
public class MyBatisTest {

    @Test
    @Rollback(value = false)
    public void testSelectById() throws IOException {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        List<User> userList = mapper.getUserList();
        User u = mapper.getUserByUsername("Time");
        System.out.println(u == null);
//        for (User user : userList) {
//            System.out.println(user);
//        }
    }

}
