package com.sculling.sculling.controller;

import cn.hutool.core.date.DateUtil;
import com.sculling.sculling.domain.MeterData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author hpdata
 * @DATE 2023/5/2317:31
 */
@RestController
@Slf4j
public class MeterController {


    @GetMapping("/DCCS/openapi/v1/access_token")
    public Map<String,Object>  token(){
        // 获取token
        Map<String,Object> res = new HashMap<>();
        res.put("ErrNo",0);
        res.put("ErrMsg","成功");
        res.put("access_token","1234567");
        return res;
    }

    @GetMapping("/DCCS/openapi/v1/elemeters_read")
    public Map<String,Object>  data(@RequestParam("uuid") String uuid){
        String[] arr = uuid.split(",");
        Map<String,Object> res = new HashMap<>();
        res.put("ErrNo",0);
        res.put("ErrMsg","成功");
        List<Map<String,Object>> datas = new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            String sn  = arr[i];
            int x = (int)(Math.random()*100+1);
            Map<String,Object> data = new HashMap<>();
            MeterData meterData = new MeterData();
            data.put("meterCode",sn);
            meterData.setReadingdt(DateUtil.now());
            meterData.setAccumulateFlux(DateUtil.format(new Date(),"MMddHH"));
            meterData.setPositiveElec(DateUtil.format(new Date(),"MMddHH"));
            if(x < 5){
                data.put("ErrMsg","内部错误-(设备离线)");
                data.put("ErrNo","15000");
                data.put("data","提示信息");
                data.put("remake","测试仪表");
            }else{
                data.put("ErrMsg","成功");
                data.put("ErrNo","0");
                data.put("data",meterData);
                data.put("remake","测试仪表");
            }
            datas.add(data);
        }
        res.put("data",datas);
        return res;
    }

    public static void main(String[] args) {
        try{

            DataSource dataSource = DataSourceBuilder.create().url("jdbc:").build();
            Connection connection = dataSource.getConnection();
        }catch (Exception e){
            log.info("432");
        }


    }



}


