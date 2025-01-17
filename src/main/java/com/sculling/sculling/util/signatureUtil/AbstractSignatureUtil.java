package com.sculling.sculling.util.signatureUtil;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Author sean
 * @Date 2024/12/14 13:13
 * @desc
 */
@Slf4j
public abstract class AbstractSignatureUtil<T> {


    public static final String requestHost = "http://192.168.1.3:9001";

    /**
     * 请求封装类
     * @param paramMap
     * @param tClass
     * @throws Exception
     */
    public T request(Map<String, String> paramMap, Class<T> tClass) throws Exception {
        TreeMap<String, String> treeMap = new TreeMap<>(paramMap);
        StringBuilder sb = new StringBuilder();
        treeMap.forEach((k,v)->{
            sb.append(k).append("=").append(v).append("&");
        });
        String sign = RsaUtil.vstEncode(sb.toString());
        log.info("req.sign:{}",sign);
//        String respStr = HttpUtils.sendPost(requestHost+getInterPath(),paramMap,sign);
//        return JSON.parseObject(respStr, tClass);
        return null;
    }


    public abstract String getInterPath();

}
