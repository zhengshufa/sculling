package com.sculling.sculling.domain;

import lombok.Data;

@Data
public class Message {


    int code;   //0:成功，1:失败

    String msg;

    String data;

    public Message(int code,String msg,String data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString(){
        return "{code:" + code+",msg:"+msg+",data:"+data+"}";
    }

}
