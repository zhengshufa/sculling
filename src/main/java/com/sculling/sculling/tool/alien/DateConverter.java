package com.sculling.sculling.tool.alien;

import java.util.HashMap;
import java.util.Map;

public class DateConverter {
    private static final Map<Integer, Integer> monthDaysMap = new HashMap<>();
    static {
        monthDaysMap.put(1, 44);
        monthDaysMap.put(2, 42);
        monthDaysMap.put(3, 48);
        monthDaysMap.put(4, 40);
        monthDaysMap.put(5, 48);
        monthDaysMap.put(6, 44);
        monthDaysMap.put(7, 40);
        monthDaysMap.put(8, 44);
        monthDaysMap.put(9, 42);
        monthDaysMap.put(10, 40);
        monthDaysMap.put(11, 40);
        monthDaysMap.put(12, 42);
        monthDaysMap.put(13, 44);
        monthDaysMap.put(14, 48);
        monthDaysMap.put(15, 42);
        monthDaysMap.put(16, 40);
        monthDaysMap.put(17, 44);
        monthDaysMap.put(18, 38);
    }

    public static void main(String[] args) {
        EarthDate earthDate = new EarthDate(1970, 1, 1, 0, 0, 0);
        VirtualDate virtualDate = convertEarthToVirtualDate(earthDate);
        System.out.println(virtualDate);

        earthDate = convertVirtualToEarthDate(virtualDate);
        System.out.println(earthDate);
    }

    private static VirtualDate convertEarthToVirtualDate(EarthDate earthDate) {
        EarthDate referenceEarthDate = new EarthDate(1970, 1, 1, 0, 0, 0);
        long earthSeconds = earthDate.toSeconds() - referenceEarthDate.toSeconds();
        long virtualSeconds = earthSeconds * 2;

        int virtualSecondsInMinute = 90;
        int virtualMinutesInHour = 90;
        int virtualHoursInDay = 36;

        long totalVirtualMinutes = virtualSeconds / virtualSecondsInMinute;
        long totalVirtualHours = totalVirtualMinutes / virtualMinutesInHour;
        long totalVirtualDays = totalVirtualHours / virtualHoursInDay;

        long virtualSecondsRemaining = virtualSeconds % virtualSecondsInMinute;
        long virtualMinutesRemaining = totalVirtualMinutes % virtualMinutesInHour;
        long virtualHoursRemaining = totalVirtualHours % virtualHoursInDay;

        int year = 2804;
        int month = 1;
        int day = 1;

        while (totalVirtualDays > 0) {
            int daysInCurrentMonth = monthDaysMap.get(month);
            if (totalVirtualDays >= daysInCurrentMonth) {
                totalVirtualDays -= daysInCurrentMonth;
                month++;
                if (month > 18) {
                    month = 1;
                    year++;
                }
            } else {
                day += totalVirtualDays;
                totalVirtualDays = 0;
            }
        }

        return new VirtualDate(year, month, day, (int) virtualHoursRemaining, (int) virtualMinutesRemaining, (int) virtualSecondsRemaining);
    }

    private static EarthDate convertVirtualToEarthDate(VirtualDate virtualDate) {
        EarthDate referenceEarthDate = new EarthDate(1970, 1, 1, 0, 0, 0);

        int virtualSecondsInMinute = 90;
        int virtualMinutesInHour = 90;
        int virtualHoursInDay = 36;

        long virtualSeconds = virtualDate.getSecond() + virtualDate.getMinute() * virtualSecondsInMinute +
                virtualDate.getHour() * virtualSecondsInMinute * virtualMinutesInHour +
                (virtualDate.getDay() - 1) * virtualSecondsInMinute * virtualMinutesInHour * virtualHoursInDay;

        for (int month = 1; month < virtualDate.getMonth(); month++) {
            virtualSeconds += monthDaysMap.get(month) * virtualSecondsInMinute * virtualMinutesInHour * virtualHoursInDay;
        }

        virtualSeconds += (virtualDate.getYear() - 2804) * 18 * 365 * virtualSecondsInMinute * virtualMinutesInHour * virtualHoursInDay;
        long earthSeconds = virtualSeconds / 2;

        return referenceEarthDate.plusSeconds(earthSeconds);
    }
}

class EarthDate {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public EarthDate(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public long toSeconds() {
        long daysInYear = (year - 1970) * 365 + (year - 1969) / 4;
        long daysInMonth = (month - 1) * 30;
        long daysInDay = day - 1;
        return ((daysInYear + daysInMonth + daysInDay) * 24 + hour) * 3600 + minute * 60 + second;
    }

    public EarthDate plusSeconds(long seconds) {
        int totalSeconds = (int) (toSeconds() + seconds);
        int newSecond = totalSeconds % 60;
        int totalMinutes = totalSeconds / 60;
        int newMinute = totalMinutes % 60;
        int totalHours = totalMinutes / 60;
        int newHour = totalHours % 24;
        int totalDays = totalHours / 24;

        int newYear = 1970;
        while (totalDays >= 365) {
            totalDays -= 365;
            newYear++;
            if (newYear % 4 == 0) totalDays--;
        }

        int newMonth = 1;
        while (totalDays >= 30) {
            totalDays -= 30;
            newMonth++;
        }

        int newDay = totalDays + 1;

        return new EarthDate(newYear, newMonth, newDay, newHour, newMinute, newSecond);
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02dT%02d:%02d:%02d", year, month, day, hour, minute, second);
    }
}

class VirtualDate {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public VirtualDate(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02dT%02d:%02d:%02d", year, month, day, hour, minute, second);
    }
}
