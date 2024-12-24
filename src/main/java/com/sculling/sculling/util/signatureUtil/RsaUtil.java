package com.sculling.sculling.util.signatureUtil;


import com.sculling.sculling.util.signatureUtil.rsa.RSACoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class RsaUtil {


    private static final String vstPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCo+loIDo/WOc7G2wGx2Vni5deJ2xj9w8GP/lNhhH3TqFyaAAin3BUnHesrGtCzEMAcas3RxWbbu5LX6Ga6qbspeSVjLEQLrAuJmvNbkbUccMz3F0QlIFh8MXBOwkrsCf4aDuNfuMev5y7TLYMUNRqeZvoKV/T/2NWrDuM1nOueQwIDAQAB";
    private static final String vstPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKj6WggOj9Y5zsbbAbHZWeLl14nbGP3DwY/+U2GEfdOoXJoACKfcFScd6ysa0LMQwBxqzdHFZtu7ktfoZrqpuyl5JWMsRAusC4ma81uRtRxwzPcXRCUgWHwxcE7CSuwJ/hoO41+4x6/nLtMtgxQ1Gp5m+gpX9P/Y1asO4zWc655DAgMBAAECgYA28eDgicuffwCLjJ8Kc4BU1SO2wbVonoSMC8iVBv3bGv2xXZ1y549BPus/AuMltqahFuGB+kwt2toutnTg8VvX4J/STOE7VxOUkrmQaDKqsZeJHW2+oTd40Wcv1e+wbCpknhhgDPQfEySPH4Vvg/FesxJ1WETks64iSSneBkDeEQJBAKsKq3B38Qu5Y29BQjXcj2/4+/KIAc9KMedStuIVNwBnbXytYS17GPjh1/nu1XNRzW8UtkQtmX77XZh84nAyP/UCQQD86UKxcpRW9SXIWGtdAqJV4NnCaLfPvsNGfNGMpvBP4TG4ClY5XhSSt4jcBUa9vMV6fR8XMBIVfOmwlupxVRpXAkAwNN6i4Tsvyb1rsuHdWl+W+H7SGWEhMlEkWFyxFbedxojGNfuInQQpyUVc7OJ+ERUsdJL2Xj+/2UrE2pXbd14tAkAkDCc6XYdxIX3iIWAkIKT8spC3Ge/hB2KT4GSJtJ2Z9RH+FlMV2Cf8hXZXTdU4Y/iNrdnJl0tsjqJMTiZaQvP/AkEAiGvzyQH7vqFc3MFDwXytA0ysCsybNS4I36Y8YyDitrX+ENuud/OuBhzzO8yiTY2qLtmOOptAYxYQ4i13jXouPw==";


    private static final String merPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyWm+4D/uI/VJrVpC8NZMvNAKEd/PPqEBIhji2cMqialWAmKmkH/dd9GdaVwXN7B6O3vUragTZylSzSRh9+Pks0gr81vCX0NCcI7LCIodKLB5Je0wBlWQPbhhYkp3Ro66kANVKJwb1aL3rv2kN2Yke1XXM8/6/BQ5gR9dQTqoe5wIDAQAB";
    private static final String merPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALJab7gP+4j9UmtWkLw1ky80AoR388+oQEiGOLZwyqJqVYCYqaQf9130Z1pXBc3sHo7e9StqBNnKVLNJGH34+SzSCvzW8JfQ0JwjssIih0osHkl7TAGVZA9uGFiSndGjrqQA1UonBvVoveu/aQ3ZiR7Vdczz/r8FDmBH11BOqh7nAgMBAAECgYAPvdcByjoA452ZpwtL6/fwq54pQ6O7nqBbMQA3wglwf6106y8rMuhqB39JGZ3ZM0+Ye+nsX1rMP4UYSd76h2Cz7nLpBvPycRT6W0FEW1mq/1oBl5Jbr0pGtNru2z1osliBBAwxdSeayWMEmWEKpmG/A1L34QegNXVaFmaaEWAYiQJBAMGfOYwLK/ZYKz4yI/GfMFZVNpQPE3yVIdv2AOCWinpPqnPwxehWH+lpnBlXCVvP2gNJSZykfLCfyDAddEqq8S0CQQDrz/DTJr4crQOk2+EYhQmMJ9TjQc1COGTLbxzPgcVOZ9b9iI6DENiFoyYpcUXqF7227UZeh4nnub+nsdA28dTjAkAnuYlPCEO+yRwmKJdjTWvXaqhF/EndN6O4w2IusfQ7X/jyzKVuI61scDSMpmTPJ3ftUK3/q2fBvSaPaJuhbi0JAkEAj0+Xh2vTPDA1t7pdiX5IuCH48ogqC2WkdDI56y5tz+GxufGE0sipHkZxydvqZoM5K/P1wTtUJz0u6eZZqRq9iwJAVE/PmhlfHMd5NvX9g0251DBceJFCyH2e5KRxIIYso9xbHxyETbQgjV01HE1M3mLew5W4kh3SyWwVvJzBic8z7A==";


    /**
    * @author: sean
    * @date: 2024/12/14 13:18
    * @desc: vst公钥加密
    */

    public static String vstEncode(String inputStr) throws Exception {
        // 产生签名
        String sign = RSACoder.sign(inputStr.getBytes(StandardCharsets.UTF_8), vstPrivateKey);
        log.info("签名:\r" + sign);
        return sign;
    }


    /**
     * @author: sean
     * @date: 2024/12/14 13:18
     * @desc: vst私钥解密
     */
    public static Boolean verifyAndDecode(String sign,String jsonStr) throws Exception {
        // 验证签名
        return RSACoder.verify(jsonStr.getBytes(StandardCharsets.UTF_8), vstPublicKey, sign);
    }

    public static void main(String[] args) throws Exception{
        String jsonStr = "{\"userId\":\"1\",\"referralCode\":\"123456789\"}";
        String sign = vstEncode(jsonStr);
        Boolean b = verifyAndDecode(sign, jsonStr);
        System.out.println(b.toString());
    }
}
