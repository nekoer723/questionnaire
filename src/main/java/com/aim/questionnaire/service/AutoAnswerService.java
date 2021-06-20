package com.aim.questionnaire.service;

import com.aim.questionnaire.dao.AutoAnswerEntityMapper;
import com.aim.questionnaire.dao.entity.AutoAnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoAnswerService {
    @Autowired
    private AutoAnswerEntityMapper autoAnswerEntityMapper;

    public List<AutoAnswerEntity> selectAll() {
        return autoAnswerEntityMapper.selectAll();
    }

}
