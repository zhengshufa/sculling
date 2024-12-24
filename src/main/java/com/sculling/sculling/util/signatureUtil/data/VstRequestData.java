package com.sculling.sculling.util.signatureUtil.data;

import lombok.Builder;

/**
 * packageName.className com.ly.pay.rsa.VstRequestData
 *
 * @author alaric
 * @version JDK 17
 * @date 2024-12-12 15:56
 * @description vst接口请求对象
 */
@Builder
public class VstRequestData {
    private String encodeData;
    private String sign;

    public String toString(){
        return "encodeData="+encodeData+"&sign="+sign;
    }
}