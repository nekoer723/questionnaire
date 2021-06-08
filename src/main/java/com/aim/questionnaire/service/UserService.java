package com.aim.questionnaire.service;

import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserEntityMapper userEntityMapper;


    public UserEntity selectAllByName(String username) {
        UserEntity userEntity = userEntityMapper.selectAllByName(username);
        return userEntity;
    }
}
