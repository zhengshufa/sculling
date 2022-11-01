package com.sculling.sculling.controller;


import com.sculling.sculling.domain.Message;
import com.sculling.sculling.service.ScullingService;
import com.sculling.sculling.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class ScullingController {



    @Autowired
    private ScullingService scullingService;

    @GetMapping("list")
    public String list(String req) throws Exception{
        String data = RSAUtils.decryptByPrivateKey(req.replaceAll(" ","+"));
        log.info("data: "+data);
        Message msg = scullingService.list3(data);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("sculling")
    public String sculling(int size) throws Exception{
        Message msg = scullingService.sculling3(size);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }
}
