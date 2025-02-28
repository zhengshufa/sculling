package com.sculling.sculling.service.sharingQualification;

import com.ly.member.constant.BillTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author sean
 * @Date 2024/12/24 09:38
 * @desc
 */
@Component
@AllArgsConstructor
public class SharingQualificationFactory {

    private final ApplicationContext applicationContext;

    public SharingQualification creator(BillTypeEnum billTypeEnum) {
        return applicationContext.getBean(billTypeEnum.getClazz());
    }
}
