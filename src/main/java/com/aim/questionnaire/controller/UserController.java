package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.entity.UserEntity;
import com.aim.questionnaire.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userLogin",method = RequestMethod.POST,headers="Accept=application/json")
    public HttpResponseEntity userLogin(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        UserEntity hasUser = userService.selectAllByName(username);
        if(password.equals(hasUser.getPassword())){
            httpResponseEntity.setData(hasUser);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage("登陆成功");
        }else {
            httpResponseEntity.setData(hasUser);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage("登陆失败");
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/addUserInfo",method = RequestMethod.POST)
    public HttpResponseEntity createNewUser(@RequestBody UserEntity userEntity) throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        int code = userService.insertUser(userEntity);
        if(code == 1){
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        }else if(code == -1){
            httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
            httpResponseEntity.setMessage("插入失败，用户名已经存在");
        } else  {
            httpResponseEntity.setMessage("插入异常");
            httpResponseEntity.setCode("777");
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "queryUserList")
    public HttpResponseEntity queryUserList(){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Object> result = userService.queryUserList();
        Map<String,List<Object>> res = new HashMap<>();
        res.put("list",result);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(res);
        return httpResponseEntity;
    }


}
