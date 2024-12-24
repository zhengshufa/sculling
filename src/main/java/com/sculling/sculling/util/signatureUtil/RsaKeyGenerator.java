package com.sculling.sculling.util.signatureUtil;


import com.sculling.sculling.util.signatureUtil.rsa.RSACoder;

import java.util.Map;

/**
 * @Author sean
 * @Date 2024/12/14 16:12
 * @desc
 */
public class RsaKeyGenerator {


    public static void main(String[] args) throws Exception{
        Map<String, Object> keyMap = RSACoder.initKey();

        String publicKey = RSACoder.getPublicKey(keyMap);
        String privateKey = RSACoder.getPrivateKey(keyMap);
        System.err.println("公钥: \n\r" + publicKey);
        System.err.println("私钥： \n\r" + privateKey);
    }

}
