package com.sculling.sculling.retile;


import java.net.URLEncoder;

import java.util.Scanner;


public class Sculling {


    //101.34.13.141 11000
//    static String baseUrl = "https://www.biqusa.org";

    static String baseUrl = "";




    static String serverIp = "http://101.34.13.141:11000";

    public static void main(String[] args) throws Exception {
        String params = URLEncoder.encode(baseUrl + "/133_133715/");
        String res = HttpUtils.getHttpRequestData(serverIp+"/list","req="+RSAUtils.encryptByPublicKey(params));
        System.out.println(RSAUtils.decryptByPrivateKey(res));
        Scanner scanner = new Scanner(System.in);

        int index = 0;
        while (true){
           String text = scanner.nextLine();
           if(isNum(text) ){
               index = Integer.parseInt(text);
               printOut(index);
           }else if("exit".equals(text)){
               break;
           }else{
               index ++;
               System.out.println("index:" + index);
               printOut(index);
           }
        }
    }

    public static void printOut(int index) throws Exception {
        String params = "size=" + index;
        String res = HttpUtils.getHttpRequestData(serverIp+"/sculling",params);
//        Map<String,Object> map = JSON.parseObject(RSAUtils.decryptByPrivateKey(res),Map.class);
//        if((Integer)map.get("code") == 0){
            String text = RSAUtils.decryptByPrivateKey(res);
            while (text.length() > 70){
                System.out.println(text.substring(0,70));
                text = text.substring(70);
            }
            System.out.println(text);
//        }else{
//            System.out.println(map.get("data"));
//        }
    }

    public static boolean isNum(String text){
        try{
            Integer.valueOf(text);
            return true;
        }catch (Exception e){
            return false;
        }
    }



}
