package com.sculling.sculling.util.signatureUtil.data;

import lombok.Builder;
import lombok.Data;

/**
 * packageName.className com.ly.pay.rsa.VstResponseData
 *
 * @author alaric
 * @version JDK 17
 * @date 2024-12-12 16:14
 * @description TODO
 */
@Builder
@Data
public class VstResponseData<T> {
    private String encodeData;
    private String sign;
    private String originStr;
    private T t;
}