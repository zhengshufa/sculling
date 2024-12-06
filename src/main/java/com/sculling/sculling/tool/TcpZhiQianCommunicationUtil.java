package com.sculling.sculling.tool;


import java.io.*;

import java.net.ServerSocket;

import java.net.Socket;

public class TcpZhiQianCommunicationUtil {


    public static OutputStream outputStream;

    public static void oneServer(int port) throws IOException {
        //1、a)创建一个服务器端Socket，即SocketService
        //搭建服务器端
        ServerSocket server = new ServerSocket(port);//开始监听客户端的请求，并阻塞
        Socket socket=server.accept();//请求收到后，自动建立连接。通过IO流进行数据传输
        System.out.println("连接建立成功");
        InputStream is = socket.getInputStream();
        outputStream = socket.getOutputStream();
        byte[] bytes = new byte[15];
        String repeat = "";
//        is.read(bytes);
        while(is.read(bytes) != 0) {
            System.out.println("["+System.currentTimeMillis() +"]:"+ bytes2HexString(bytes));
            //继电器状态修改后上报数据
            String str = bytes2HexString(bytes);
            if(!repeat.equals(str)){
                repeat = str;
                System.out.println(str);
                //判断是否为进场口关闸
//                if(0 == 0){
//                    DeviceCache.cacheStatus = 1;
//                    if(Integer.valueOf(DeviceCache.systemBaseInfo.get("sign")) == 1){
//                        DeviceRunBridgeServiceNew deviceRunWaterService = (DeviceRunBridgeServiceNew) SpringContextUtil.getBean("deviceRunBridgeServiceNew");
//                        deviceRunWaterService.checkDataToUpload();
//                    }
//                }else{
//                    //判断是否为出场的进出口开闸
//                    DeviceCache.cacheStatus = 0;
//                }
            }
        }
        socket.shutdownInput();//socket.shutdownOutput();
        socket.close();
        server.close();
    }

    public static void sendServer(byte[] bytes) {
        PrintWriter printWriter = null;
        try{
            Socket socket = new Socket("192.168.11.223",1030);
            System.out.println("连接建立成功");
             printWriter = new PrintWriter(new OutputStreamWriter((socket.getOutputStream())));
            printWriter.println(bytes);
            printWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            printWriter.close();
        }
    }

    public static String bytes2HexString(byte[] b) {
        String r = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase()+" ";
        }
        return r;
    }


}
