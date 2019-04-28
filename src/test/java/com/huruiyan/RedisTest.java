package com.huruiyan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huruiyan.domain.User;
import com.huruiyan.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybatisApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserRepository userRepository;
    @Test
    public void test() throws JsonProcessingException {
        //1.从redis中获取数据 数据的形式json字符串
        String userListJson=redisTemplate.boundValueOps("user.findAll").get();
        //2.判断redis中是否存在数据
        if (null==userListJson){
            List<User> all = userRepository.findAll();
            //将list集合转换成json字符串 ，使用jackson进行一个转换
            ObjectMapper objectMapper = new ObjectMapper();
            userListJson = objectMapper.writeValueAsString(all);
            //将查询出的数据存储到redis缓存中
            redisTemplate.boundValueOps("user.findAll").set(userListJson);

            //3.如果不存在，从数据库查询
            System.out.println("========从数据库中查询到的user数据========");
        }else {

            //4.将数据在控制台打印一下
            System.out.println("=========从redis缓存中获取数据");
        }
        System.out.println(userListJson);

    }
}
