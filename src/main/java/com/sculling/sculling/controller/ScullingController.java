package com.sculling.sculling.controller;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sculling.sculling.domain.Message;
import com.sculling.sculling.service.ScullingService;
import com.sculling.sculling.util.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScullingController {



    @Autowired
    private ScullingService scullingService;

    @GetMapping("list")
    public String list(String url,String bookId) throws Exception{
        Message msg = scullingService.list(url,bookId);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("sculling")
    public String sculling(int size) throws Exception{
        Message msg = scullingService.sculling(size);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }
}
