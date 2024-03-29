package com.sculling.sculling.util;

import cn.smallbun.screw.core.util.StringUtils;
import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @date: 2019年1月22日
 * @author: dongyinghua
 * @title: DateUtils
 * @version: 1.0
 * @description： 日期与时间公共类
 * update_version: update_date: update_author: update_note:
 */
public class DateUtils {

	public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd";
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_FORMAT_1 = "yyyyMMddHHmmss";
	public static final String DEFAULT_FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DEFAULT_FORMAT_YEAR = "yyyy";
	public static final String DEFAULT_FORMAT_MONTH = "MM";
	public static final String DEFAULT_FORMAT_MONTHID ="yyyyMM";

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: formatDate
	 * @param date
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 时间转字符串，格式为 yyyy-MM-dd
	 * update_version: update_date: update_author: update_note:
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
		String str = sdf.format(date);
		return str;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: formatDates
	 * @param date
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 时间转字符串，格式为yyyy-MM-dd HH:mm:ss
	 * update_version: update_date: update_author: update_note:
	 */
	public static String formatDates(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		String str = sdf.format(date);
		return str;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: formatDates1
	 * @param date
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 时间转字符串，格式为yyyyMMddHHmmss
	 * update_version: update_date: update_author: update_note:
	 */
	public static String formatDates1(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_1);
		String str = sdf.format(date);
		return str;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: formatDatess
	 * @param date
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 时间转字符串，格式为yyyy-MM-dd HH:mm:ss
	 * update_version: update_date: update_author: update_note:
	 */
	public static String formatDatess(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_TIMESTAMP);
		String str = sdf.format(date);
		return str;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: formatDates
	 * @param date
	 * @param datePatterns
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 时间转字符串，自定义格式
	 * update_version: update_date: update_author: update_note:
	 */
	public static String formatDates(Date date, String datePatterns) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePatterns);
		String str = sdf.format(date);
		return str;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: formatDates
	 * @param l
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 时间戳转字符串，格式为yyyy-MM-dd HH:mm:ss
	 * update_version: update_date: update_author: update_note:
	 */
	public static String formatDates(Long l) {
		Date date = new Date(l);
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		String str = sdf.format(date);
		return str;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: formatTime
	 * @param ms
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获得天时分秒
	 * update_version: update_date: update_author: update_note:
	 */
	public static String formatTimes(long ms) {

		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		String strDay = day < 10 ? "0" + day : "" + day; //天
		String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
		String strSecond = second < 10 ? "0" + second : "" + second;//秒
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
		StringBuilder sb = new StringBuilder();
		sb.append(strDay).append("天").append(strHour)
				.append("小时").append(strMinute).append("分钟").append(strSecond).append("秒");
		return sb.toString();
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getSysDate
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取系统时间
	 * update_version: update_date: update_author: update_note:
	 */
	public static Date getSysDate() {
		Date date = new Date();
		return date;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getSysDatesString
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取系统时间，格式为yyyy-MM-dd HH:mm:ss
	 * update_version: update_date: update_author: update_note:
	 */
	public static String getSysDatesString() {
		Date date = new Date();
		return formatDates(date);
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getSysDateString
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取系统时间，格式为yyyy-MM-dd
	 * update_version: update_date: update_author: update_note:
	 */
	public static String getSysDateString() {
		Date date = new Date();
		return formatDate(date);
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getSysDateLong
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取系统时间戳
	 * update_version: update_date: update_author: update_note:
	 */
	public static Long getSysDateLong() {
		Date date = new Date();
		return date.getTime();
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: parseDate
	 * @param dateStr
	 * @param datePattern
	 * @return
	 * @throws Exception
	 * @exception:
	 * @version: 1.0
	 * @description: 字符串转时间，自定义格式
	 * update_version: update_date: update_author: update_note:
	 */
	public static Date parseDate(String dateStr, String datePattern) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		Date date = sdf.parse(dateStr);
		return date;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: parseDate
	 * @param dateStr
	 * @return
	 * @throws Exception
	 * @exception:
	 * @version: 1.0
	 * @description: 字符串转date，字符串格格式为yyyy-MM-dd
	 * update_version: update_date: update_author: update_note:
	 */
	public static Date parseDate(String dateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
		Date date = sdf.parse(dateStr);
		return date;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: parseDates
	 * @param dateStr
	 * @return
	 * @throws Exception
	 * @exception:
	 * @version: 1.0
	 * @description: 字符串转date，字符串格格式为yyyy-MM-dd HH:mm:ss
	 * update_version: update_date: update_author: update_note:
	 */
	public static Date parseDates(String dateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		Date date = sdf.parse(dateStr);
		return date;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: parseDates1
	 * @param dateStr
	 * @return
	 * @throws Exception
	 * @exception:
	 * @version: 1.0
	 * @description: 字符串转date，字符串格格式为yyyyMMddHHmmss
	 * update_version: update_date: update_author: update_note:
	 */
	public static Date parseDates1(String dateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_1);
		Date date = sdf.parse(dateStr);
		return date;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: parseDatess
	 * @param dateStr
	 * @return
	 * @throws Exception
	 * @exception:
	 * @version: 1.0
	 * @description: 字符串转date，字符串格格式为yyyy-MM-dd HH:mm:ss:SSS
	 * update_version: update_date: update_author: update_note:
	 */
	public static Date parseDatess(String dateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_TIMESTAMP);
		Date date = sdf.parse(dateStr);
		return date;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: compareDate
	 * @param dateSrc 要比对的时间
	 * @param dateTarget 目标时间
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 时间比对大小
	 * update_version: update_date: update_author: update_note:
	 */
	public static Boolean compareDate(Date dateSrc, Date dateTarget) {
		if(dateSrc.getTime() > dateTarget.getTime()){
			return true;
		}
		return false;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: addDay
	 * @param date
	 * @param n
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 增加n天
	 * update_version: update_date: update_author: update_note:
	 */
	public static String addDay(String date,int n) {
		int year = Integer.valueOf(date.split("-")[0]);
		int month = Integer.valueOf(date.split("-")[1]);
		int day = Integer.valueOf(date.split("-")[2]);
		Calendar cc = new GregorianCalendar(year,month-1,day);
		cc.add(Calendar.DATE, n);
		int temp_month=(cc.get(Calendar.MONTH)+1);

		String str_Month="00";
		if(temp_month<10){
			str_Month="0"+temp_month;
		}else{
			str_Month=String.valueOf(temp_month);
		}

		int temp_day=cc.get(Calendar.DAY_OF_MONTH);
		String str_day="00";
		if(temp_day<10){
			str_day="0"+temp_day;
		}else{
			str_day=String.valueOf(temp_day);
		}
		return cc.get(Calendar.YEAR)+"-"+str_Month+"-"+str_day;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: addMonth
	 * @param date
	 * @param n
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 增加n月
	 * update_version: update_date: update_author: update_note:
	 */
	public static String addMonth(String date,int n) {
		int year = Integer.valueOf(date.split("-")[0]);
		int month = Integer.valueOf(date.split("-")[1]);
		int day = Integer.valueOf(date.split("-")[2]);
		Calendar cc = new GregorianCalendar(year,month-1,day);
		cc.add(Calendar.MONTH, n);
		return cc.get(Calendar.YEAR)+"-"+(cc.get(Calendar.MONTH)+1)+"-"+cc.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getYear
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取当前年份
	 * update_version: update_date: update_author: update_note:
	 */
	public static String getYear() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_YEAR);
		Date date = new Date();
		return sdf.format(date);
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getMonth
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取当前月份
	 * update_version: update_date: update_author: update_note:
	 */
	public static String getMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_MONTH);
		Date date = new Date();
		return sdf.format(date);
	}


	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getMonth
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取当前月份
	 * update_version: update_date: update_author: update_note:
	 */
	public static String getThisMonthId() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_MONTHID);
		Date date = new Date();
		return sdf.format(date);
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getWeekStartAndEnd
	 * @param pattern
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取一周的开始和结束
	 * update_version: update_date: update_author: update_note:
	 */
	public static String[] getWeekStartAndEnd(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		Calendar currentDate = new GregorianCalendar();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);

		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String start = sdf.format((Date)currentDate.getTime().clone());

		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String end = sdf.format((Date)currentDate.getTime().clone());

		//System.out.println("week:" + start + "///" + end);

		String[] times = {start, end};

		return times;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getDayStartAndEnd
	 * @param pattern
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取一天的开始和结束
	 * update_version: update_date: update_author: update_note:
	 */
	public static String[] getDayStartAndEnd(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		Calendar currentDate = new GregorianCalendar();

		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		String start = sdf.format((Date)currentDate.getTime().clone());

		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		String end = sdf.format((Date)currentDate.getTime().clone());

		//System.out.println("day:" + start + "///" + end);

		String[] times = {start, end};

		return times;
	}

	/**
	 * 获取指定月开始和结束
	 */
	public static String[] getMonthStartAndEnd(Date date, String pattern) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar currentDate=Calendar.getInstance();
		currentDate.setTime(date);
		currentDate.add(Calendar.MONTH, 0);
		currentDate.set(Calendar.DAY_OF_MONTH,1);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		String start = sdf.format(currentDate.getTime());

		currentDate.set(Calendar.DAY_OF_MONTH, currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		String end = sdf.format(currentDate.getTime());

		//System.out.println("month:" + start + "///" + end);

		String[] times = {start, end};

		return times;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getMonthStartAndEnd
	 * @param pattern
	 * @return
	 * @exception:
	 * @version: 1.0
	 * @description: 获取每月开始和结束
	 * update_version: update_date: update_author: update_note:
	 */
	public static String[] getMonthStartAndEnd(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		Calendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.MONTH, 0);
		currentDate.set(Calendar.DAY_OF_MONTH,1);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		String start = sdf.format(currentDate.getTime());

		currentDate.set(Calendar.DAY_OF_MONTH, currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		String end = sdf.format(currentDate.getTime());

		//System.out.println("month:" + start + "///" + end);

		String[] times = {start, end};

		return times;
	}

	/**
	 *
	 * @date: 2019年1月22日
	 * @author: dongyinghua
	 * @title: getCurrentLatelyDays
	 * @param num
	 * @return
	 * @throws Exception
	 * @exception:
	 * @version: 1.0
	 * @description: 根据当前时间计算近多少天的日期
	 * update_version: update_date: update_author: update_note:
	 */
	public static Date getCurrentLatelyDays(Integer num) throws Exception {
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, - 90);
		return calendar.getTime();
	}

	/**
	 * 日期处理 得到前一年的日期
	 * @return
	 * @throws ParseException
	 */
	public static String getOldDate(String strDate,int month) throws ParseException{
		//Calendar.MONTH 0-11
		if(StringUtils.isBlank(strDate) ) return strDate;
		String sdfStr = "yyyyMM";
		if(strDate.length() > 6) sdfStr = "yyyyMMdd";
		SimpleDateFormat sdf =   new SimpleDateFormat( sdfStr );
		int smonth = Integer.valueOf(strDate.substring(4,6))-1;
		String strMonth ="";
		if(smonth<10){
			strMonth = "0"+ smonth;
		}else{
			strMonth = ""+ smonth;
		}
		String strdate = strDate.substring(0, 4)+strMonth;
		if (strDate.length()>6) strdate = strdate+strDate.substring(6,8);
		Date date =  sdf.parse(strdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		String oleYear = String.valueOf(c.get(Calendar.YEAR));
		String oleMonth = String.valueOf(c.get(Calendar.MONTH)+1);
		String oleDay = String.valueOf(c.get(Calendar.DATE));
		String oleDate = "";
		if(oleMonth.length()==1){
			oleDate = oleYear + "0" + oleMonth;
		}else{
			oleDate = oleYear  + oleMonth;
		}
		if(oleDay.length()==1 && !"0".equals(oleDay) ){
			oleDate = oleDate + "0" + oleDay;
		}else if(oleDay.length()!=1){
			oleDate = oleDate  + oleDay;
		}
		return oleDate;
	}

	public static String getNowDateFormatCustom(String dateFormat) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}


	/**
	 * 将剩余秒数转为时间格式
	 * @param time
	 * @return
	 */
	public static String getTime(Long time) {
		String timeStr="";
		if (time==null){
			return null;
		}
		//时
		Long hour = time / 60 / 60;
		//分
		Long minutes = time / 60 % 60;
		//秒
		Long remainingSeconds = time % 60;
		//判断时分秒是否小于10……
		if (hour < 10){
			timeStr =  minutes + "分" + remainingSeconds+"秒";
		}else if (minutes < 10){
			timeStr =  minutes + "分" + remainingSeconds+"秒";
		}else if (remainingSeconds < 10){
			timeStr =  minutes + "分" + "0" + remainingSeconds+"秒";
		}else {
			timeStr =  minutes + "分" + remainingSeconds+"秒";
		}
		return timeStr;
	}

	public static String getNowDates(){
		return DateUtils.formatDates(new Date());
	}


	/**
	 * 根据账期起止时间获取区间内所有账期
	 * @param startMonthId
	 * @param endMonthId
	 * @return
	 */
	@SneakyThrows
	public static List<String> getMonthIdScope(String startMonthId, String endMonthId){
		List<String> res = new ArrayList<>();

		if (StringUtils.isBlank(endMonthId)){
			res.add(startMonthId);
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_MONTHID);
			// 转化成日期类型
			Date startDate = sdf.parse(startMonthId);
			Date endDate = sdf.parse(endMonthId);

			//用Calendar 进行日期比较判断
			Calendar calendar = Calendar.getInstance();
			while (startDate.getTime() <= endDate.getTime()) {

				// 把日期添加到集合
				res.add(sdf.format(startDate));

				// 设置日期
				calendar.setTime(startDate);

				//把月数增加 1
				calendar.add(Calendar.MONTH, 1);

				// 获取增加后的日期
				startDate = calendar.getTime();
			}
		}

		return res;
	}

	//获取指定月份之前的月份数据
	@SneakyThrows
	public static String getSomeMonthAgo(String time, int i) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_MONTHID);
		// 转化成日期类型
		Date Time = sdf.parse(time);
		//用Calendar 进行日期比较判断
		Calendar calendar = Calendar.getInstance();
		// 设置时间
		calendar.setTime(Time);

		//把月数减去指定月份
		calendar.add(Calendar.MONTH, -i);
		// 获取增加后的日期
		Time = calendar.getTime();
		return sdf.format(Time);
	}
}
