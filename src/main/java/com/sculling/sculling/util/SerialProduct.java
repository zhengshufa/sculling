package com.sculling.sculling.util;

import com.alibaba.fastjson.JSONObject;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springblade.hopdata.device.lang.entity.BaseCommunicationEntity;
import org.springblade.hopdata.device.lang.entity.SerialEntity;
import org.springblade.hopdata.device.lang.utils.ProductUtil;
import org.springblade.hopdata.device.lang.utils.serial.SerialPortManager;
import org.springblade.hopdata.device.service.impl.DeviceServiceImpl;

import java.util.Map;

/**
 * @author 串口协议
 * * @date 2022/4/29
 */
@Slf4j
public class SerialProduct extends Thread {


	public static String serialResponse = null;
	public <T extends BaseCommunicationEntity> Map<String, Object> connectCommunication(T t) {
		String response = "";
		try{
			if(t == null){
				throw new NullPointerException("入参不能为空");
			}
			SerialEntity serialEntity = (SerialEntity) t;

			if(serialEntity.getPort() == null || "".equals(serialEntity.getPort())){
				throw new NullPointerException("串口号字段必传");
			}
			if(serialEntity.getBaudrate() == null || "".equals(serialEntity.getBaudrate())){
				throw new NullPointerException("波特率字段必传");
			}
			if(serialEntity.getDatebits() == null || "".equals(serialEntity.getDatebits())){
				serialEntity.setDatebits("8");
			}
			if(serialEntity.getStopbits() == null || "".equals(serialEntity.getStopbits())){
				serialEntity.setStopbits("1");
			}
			if(serialEntity.getParity() == null || "".equals(serialEntity.getParity())){
				serialEntity.setParity("0");
			}
//			List<String> portNames = SerialPortManager.findPorts();
//			if(portNames == null || portNames.size() <= 0 || portNames.indexOf(serialEntity.getPort()) == -1){
//				throw new NullPointerException("没有可用的串口号");
//			}
			SerialPort serialport = SerialPortManager.openPort(serialEntity);
			if (serialport == null) {
				return JSONObject.parseObject(response);
//				throw new OperationsException("串口打开不成功");
			}
			// 添加串口监听
			SerialPortManager.getListener(serialport);
			//等待数据连接正确
			for (int i = 0; i < 15; i++) {
				try {
					Thread.sleep(1000);
					if (null != serialResponse) {
						return JSONObject.parseObject(response);
					}
				} catch (InterruptedException e) {

				}
			}
			return JSONObject.parseObject(response);
		}
		catch (NullPointerException e){

			response = "{\"code\":404,\"msg\":\""+ e.getMessage() +"\",\"data\":null,\"success\":false}";
			DeviceServiceImpl.recordDeviceCommunicationResult(response);
			return JSONObject.parseObject(response);
		}
	}
	
	public Map<String, Object> analysisData(Map<String,Object> mappingData) {
		//获取数据的map
//		return ProductUtil.analysisData(response,mappingData);
		log.info("该方法已弃用");
		return null;
	}

	public static void recordDeviceCommunicationLog(int code,String message, String data){
		if(code == 200){
			serialResponse = data;
		} else {
			serialResponse = null;
		}
		//这里应该是记录设备连接过程中发生的变化,并调用外部接口存储到数据库中
		String response = "{\"code\":"+code+",\"msg\":\""+message+"\",\"data\":"+data+",\"success\":"+(code == 200 ? "true" : "false")+"}";
		DeviceServiceImpl.recordDeviceCommunicationResult(response);

	}

}
