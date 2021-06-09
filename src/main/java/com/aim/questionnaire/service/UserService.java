package com.aim.questionnaire.service;

import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserEntityMapper userEntityMapper;


    public UserEntity selectAllByName(String username) {
        UserEntity userEntity = userEntityMapper.selectAllByName(username);
        return userEntity;
    }

    public int insertUser(UserEntity userEntity){
        if(userEntityMapper.selectAllByName(userEntity.getUsername())!=null){
            return -1;
        }
        if(userEntity.getUsername().length()<=0||
                userEntity.getPassword().length()<=0){
            return  0;
        }else if(userEntity.getStartTime() == null||userEntity.getStopTime() == null){
            return 0;
        }else {
            try {
                userEntityMapper.insertUser(userEntity);
            }catch (Exception exception){
               //exception.printStackTrace();
                return -2;
            }

        }
        return 1;
    }

    public List<Object> queryUserList() {
        List<Object> resultList = new ArrayList<Object>();
        List<Map<String,Object>> proResult = userEntityMapper.selectUsers();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Map<String,Object> userObj : proResult) {
            String start = ""; String stop = "";
            try {
                start = simpleDateFormat.format(userObj.get("startTime"));
                stop = simpleDateFormat.format(userObj.get("stopTime"));
            }catch (Exception e){
                start = "值为空！";
                stop = "值为空";
            }
            userObj.put("startTime",start);
            userObj.put("stopTime",stop);
            resultList.add(userObj);
        }
        return resultList;
    }
}
