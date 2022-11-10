package com.sculling.sculling.controller;


import com.sculling.sculling.domain.Message;
import com.sculling.sculling.service.ScullingService;
import com.sculling.sculling.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RefreshScope
public class ScullingController {



    @Value("${data: 123}")
    public String data;

    @Autowired
    private ScullingService scullingService;

    @GetMapping("list")
    public String list(String req) throws Exception{
        String data = RSAUtils.decryptByPrivateKey(req.replaceAll(" ","+"));
        log.info("data: "+data);
        Message msg = scullingService.list(data);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("sculling")
    public String sculling(int size) throws Exception{
        Message msg = scullingService.sculling(size);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("get")
    public String get(){
        System.out.println(data);
        return "data";
    }
}
