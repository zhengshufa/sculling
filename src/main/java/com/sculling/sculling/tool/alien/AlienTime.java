package com.sculling.sculling.tool.alien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlienTime {

    //初始时间：2804年18月30日34时42分88秒

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;


}
