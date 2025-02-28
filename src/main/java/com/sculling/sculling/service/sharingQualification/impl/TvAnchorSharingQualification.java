package com.sculling.sculling.service.sharingQualification.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.constant.YesOrNoEnum;
import com.ly.member.entity.Member;
import com.ly.member.entity.SharingRecord;
import com.ly.member.entity.SyncOrder;
import com.ly.member.service.IMemberService;
import com.ly.member.service.ISharingRecordService;
import com.ly.member.service.impl.sharingQualification.SharingQualification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author sean
 * @Date 2025/1/10 17:25
 * @desc 个人主播奖励分佣 TV_ANCHOR
 * 无需进行实际分佣，直接创建分佣记录，然后状态为已分佣
 */
@Service
@AllArgsConstructor
public class TvAnchorSharingQualification implements SharingQualification {

    private final IMemberService memberService;

    private final ISharingRecordService sharingRecordService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void sharing(SyncOrder syncOrder) {
        Member member = memberService.getOne(new LambdaQueryWrapper<Member>().eq(Member::getReferralCode, syncOrder.getReferralCode()));
        SharingRecord sharingRecord = new SharingRecord();
        sharingRecord.setSourceMemberId(member.getId());
        sharingRecord.setSourceReferralCode(member.getReferralCode());
        sharingRecord.setBeneficiaryMemberId(member.getId());
        sharingRecord.setBeneficiaryReferralCode(member.getReferralCode());
        sharingRecord.setAmount(syncOrder.getAmount());
        sharingRecord.setBillNo(syncOrder.getBillNo());
        sharingRecord.setBillType(syncOrder.getOperationType());
        sharingRecord.setStatus(YesOrNoEnum.YES.getCode());
        sharingRecordService.save(sharingRecord);
    }
}
