package com.sculling.sculling.util.signatureUtil.demo;

import com.ly.utils.signatureUtil.AbstractSignatureUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author sean
 * @Date 2024/12/14 13:34
 * @desc
 */
public class TestSubmitClient extends AbstractSignatureUtil<String> {



    public String payRequest() throws Exception {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("param","123");
        paramMap.put("param2","456");
        return this.request(paramMap, String.class);
    }

    @Override
    public String getInterPath() {
        return "/ly-member/test/testRes";
    }
}
