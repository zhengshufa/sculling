package com.sculling.sculling.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: JsonUtils
* @Description: JSON工具类
* @author 李博强  liboqiang@bonc.com.cn
* @date 2016年3月21日 下午8:48:02
*
 */
public class JsonUtils {
	
	/**
	 * 
	* @Title: java2json
	* @Description: Java对象转Json
	* @param obj
	* @return
	 */
	public static String java2json(Object obj){
		ObjectMapper mapper = new ObjectMapper(); 
		String json="";
		try {
			json = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} 
		return json;
	} 
	

	/**
	 *
	* @Title: json2Java
	* @Description: Json对象转Java对象
	* @param json
	* @return
	 */
	public static Object json2Java(String json){
		ObjectMapper mapper = new ObjectMapper();
		Object javaObj=new Object();
		try {
			javaObj= mapper.readValue(json, Object.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return javaObj;
	}

	/**
	 *
	 * @Title: jsonArray2Java
	 * @Description: Json数组对象转list集合
	 * @param json
	 * @return
	 */
	public static List<Map<String,String>> jsonArray2Java(String json){

		List<Map<String,String>> list = new ArrayList<>();
		JSONArray jsonArr = JSON.parseArray(json);
		for (Object obj:jsonArr) {
			JSONObject jsonObj = (JSONObject) obj;
			String jsonStr = jsonObj.toString();
			Map<String, String> prmMap = new HashMap<String, String>();
			if (json != null && json.length() > 0) {
				prmMap = (HashMap<String, String>) json2Java(jsonStr);

			}
			System.out.println("weight>"+prmMap.get("weight"));
			list.add(prmMap);
		}
		return list;
	}

	public static int getPageTotal(int size,int rows){
		int totalPageNum = 1;
		if (size%rows > 0 ){
			totalPageNum = size/rows + 1;
		}else{
			totalPageNum = size/rows;
		}
		return totalPageNum;
	}

}
