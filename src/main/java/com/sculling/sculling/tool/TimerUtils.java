package com.sculling.sculling.tool;


import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


@Slf4j
public class TimerUtils {

    public static void reset(int port){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    log.info("定时任务开启");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    timer.cancel();
                    System.out.println("结束");
                }
            }
        };
        long time = System.currentTimeMillis()+10*60*1000;//延时函数，单位毫秒，这里是延时了num分钟
        Date date = new Date();
        date.setTime(time);
        //启动定时器
        timer.schedule(task, date);
    }
}
