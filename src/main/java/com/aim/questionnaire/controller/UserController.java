package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.common.utils.Base64Util;
import com.aim.questionnaire.common.utils.ExcelUtil;
import com.aim.questionnaire.common.utils.GsonUtils;
import com.aim.questionnaire.common.utils.HttpUtil;
import com.aim.questionnaire.dao.entity.AutoAnswerEntity;
import com.aim.questionnaire.dao.entity.UserEntity;
import com.aim.questionnaire.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity userLogin(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        UserEntity hasUser = userService.selectAllByName(username);
        if (password.equals(hasUser.getPassword())) {
            httpResponseEntity.setData(hasUser);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage("登陆成功");
        } else {
            httpResponseEntity.setData(hasUser);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage("登陆失败");
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/addFace",method = RequestMethod.POST)
    public void addFace(@RequestBody Map<String,Object> maps){
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", maps.get("image"));
            map.put("group_id", "group_repeat");
            map.put("user_id", "user1");
            map.put("user_info", "abc");
            map.put("liveness_control", "NORMAL");
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.3792f22475dbfa977fbae4315e277eec.2592000.1626333769.282335-24371651";

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            //return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return null;
    }


    @RequestMapping(value = "/loginFace",method = RequestMethod.POST)
    public HttpResponseEntity loginFace(@RequestBody Map<String,Object> maps){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        UserEntity hasUser = userService.selectAllByName("admin");
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        Map<String,Object> pic1 = new HashMap<>();
        pic1.put("image", Base64Util.ImageToBase64("src/main/resources/static/images/me.png"));
        pic1.put("image_type","BASE64");
        List<Map<String,Object>> map = new ArrayList<>();
        map.add(maps);
        map.add(pic1);
        try {

            String param = GsonUtils.toJson(map);
            System.out.println(param);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.3792f22475dbfa977fbae4315e277eec.2592000.1626333769.282335-24371651";

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            //return result;
            JSONObject jsonObject = JSONObject.parseObject(result);
            double score = (jsonObject.getJSONObject("result").getDouble("score"));
            //System.out.println(score+"dsddsfffffffffffffff________________________________________");
            if(score>90){
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setData(hasUser);
                httpResponseEntity.setMessage("登录成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/addUserInfo", method = RequestMethod.POST)
    public HttpResponseEntity createNewUser(@RequestBody UserEntity userEntity) throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        int code = userService.insertUser(userEntity);
        if (code == 1) {
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        } else if (code == -1) {
            httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
            httpResponseEntity.setMessage("插入失败，用户名已经存在");
        } else {
            httpResponseEntity.setMessage("插入异常");
            httpResponseEntity.setCode("777");
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "queryUserList", method = RequestMethod.POST)
    public HttpResponseEntity queryUserList(@RequestBody(required = false) UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Map<String, List<Map<String, Object>>> res = new HashMap<>();
        List<Map<String, Object>> maps = userService.queryUserList(userEntity);
        res.put("list", maps);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(res);
        return httpResponseEntity;
    }

    @RequestMapping(value = "queryEachList",method = RequestMethod.POST)
    public HttpResponseEntity queryEachList(@RequestBody(required = false) UserEntity userEntity){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Map<String, Object> res = new HashMap<>();
        int total = userService.getUserTotal(userEntity);
        List<Map<String, Object>> maps = userService.queryEachList(userEntity);
        res.put("total",total);
        res.put("list", maps);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(res);
        return httpResponseEntity;
    }

    @RequestMapping(value = "selectUserListToExcel", method = RequestMethod.POST)
    public void batchExport(@RequestBody(required = false) UserEntity userEntity, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> maps = userService.queryUserList(userEntity);
        HSSFWorkbook tmp = new HSSFWorkbook();
        String[] title = new String[6];
        title[0] = "id";
        title[1] = "username";
        title[2] = "password";
        title[3] = "startTime";
        title[4] = "stopTime";
        title[5] = "status";
        HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook2("test", title, maps, tmp);
        OutputStream out = response.getOutputStream();
        try {
            hssfWorkbook.write(out);// 将数据写出去
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败");
        } finally {
            out.close();
        }
    }

    @RequestMapping(value = "addUserInfoList", method = RequestMethod.POST)
    public HttpResponseEntity batchInsert(@RequestBody Map<String, List<UserEntity>> map) {
        List<UserEntity> userEntities = map.get("userList");
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            userEntities.stream().forEach(userEntity -> {
                int code = userService.insertUser(userEntity);
                System.out.println(code);
            });
            httpResponseEntity.setCode("666");
        }catch (Exception e){
            e.printStackTrace();
        }
        httpResponseEntity.setCode("666");
        return httpResponseEntity;
    }


}
