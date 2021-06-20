package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.common.utils.GsonUtils;
import com.aim.questionnaire.common.utils.HttpUtil;
import com.aim.questionnaire.dao.entity.AutoAnswerEntity;
import com.aim.questionnaire.service.AutoAnswerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class CustomerController {

    @Autowired
    private AutoAnswerService autoAnswerService;

    @RequestMapping(value = "customerService",method = RequestMethod.POST)
    public HttpResponseEntity customerService(@RequestBody Map<String, String> maps){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<AutoAnswerEntity> database = autoAnswerService.selectAll();
        String url = "https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet";
        Map<String, String> map = new HashMap<>();
        map.put("text_1", maps.get("text"));
        String param;
        String accessToken = "24.fc66827a7271154a2d725ac734d42896.2592000.1626505082.282335-24387523";
        String result;
        double max = 0;
        String answer = "";
        JSONObject res;
        try {
            for (AutoAnswerEntity e: database) {
                System.out.println(e.getQuestion());
                map.put("text_2", e.getQuestion());
                param = GsonUtils.toJson(map);
                result = HttpUtil.postGeneralUrl(url + "?charset=UTF-8&access_token=" + accessToken, "application/json", param, "UTF-8");
                res = JSONObject.parseObject(result);
                if (res.getDouble("score") > max) {
                    max = res.getDouble("score");
                    answer = e.getAnswer();
                }
            }
            System.out.println(answer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (max < 0.6)
            answer = "对不起，没有明白您的意思，请您换个问题吧！";
        httpResponseEntity.setData(answer);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage("Good");
        return httpResponseEntity;
    }
}
