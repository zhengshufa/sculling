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
        Message msg = scullingService.list(data);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("sculling")
    public String sculling(int size) throws Exception{
        Message msg = scullingService.sculling(size);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("list2")
    public String list2(String req) throws Exception{
        String data = RSAUtils.decryptByPrivateKey(req.replaceAll(" ","+"));
        log.info("data: "+data);
        Message msg = scullingService.list2(data);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("sculling2")
    public String sculling2(int size) throws Exception{
        Message msg = scullingService.sculling2(size);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("list3")
    public String list3(String req) throws Exception{
        String data = RSAUtils.decryptByPrivateKey(req.replaceAll(" ","+"));
        log.info("data: "+data);
        Message msg = scullingService.list3(data);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("sculling3")
    public String sculling3(int size) throws Exception{
        Message msg = scullingService.sculling3(size);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("list4")
    public String list4(String req) throws Exception{
        String data = RSAUtils.decryptByPrivateKey(req.replaceAll(" ","+"));
        log.info("data: "+data);
        Message msg = scullingService.list4(data);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("sculling4")
    public String sculling4(int size) throws Exception{
        Message msg = scullingService.sculling4(size);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("list5")
    public String list5(String req) throws Exception{
        String data = RSAUtils.decryptByPrivateKey(req.replaceAll(" ","+"));
        log.info("data: "+data);
        Message msg = scullingService.list5(data);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }

    @GetMapping("sculling5")
    public String sculling5(int size,String bookId) throws Exception{
        Message msg = scullingService.sculling5(size,bookId);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }


    @GetMapping("search")
    public String search(String name,int page) throws Exception{
        Message msg = scullingService.search(name,page);
        return RSAUtils.encryptByPublicKey(msg.toString());
    }







}
