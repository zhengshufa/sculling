package com.sculling.sculling.service.sharingQualification.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.constant.YesOrNoEnum;
import com.ly.member.config.SharingConfig;
import com.ly.member.entity.Member;
import com.ly.member.entity.SharingRecord;
import com.ly.member.entity.SyncOrder;
import com.ly.member.service.IMemberService;
import com.ly.member.service.ISharingRecordService;
import com.ly.member.service.impl.sharingQualification.SharingQualification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @Author sean
 * @Date 2025/1/10 17:25
 * @desc 算力充值分佣逻辑
 */
@Service
@AllArgsConstructor
public class ComputingSharingQualification implements SharingQualification {

    private final IMemberService memberService;

    private final ISharingRecordService sharingRecordService;

    private final SharingConfig sharingConfig;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void sharing(SyncOrder syncOrder) {
        String[] sharingProportion = sharingConfig.getDefaultSharingProportion().split(",");

        Member sourceMember = memberService.getOne(new LambdaQueryWrapper<Member>().eq(Member::getReferralCode, syncOrder.getReferralCode()));
        Long referralId = sourceMember.getReferralId();
        int count = 0;
        //先赋值全部金额,再减去需要分佣的金额,就是无需分佣的金额,这样就不会因为四舍五入而对不上
        syncOrder.setNonSharingAmount(syncOrder.getAmount());
        //算力分配佣金为订单的40%
        BigDecimal sharingAmount = syncOrder.getAmount().multiply(new BigDecimal("0.4"));
        while (count < sharingProportion.length) {
            Member beneficiaryMember = memberService.getById(referralId);
            BigDecimal amount;
            //这一层需要分佣的金额
                //按比例分
            amount = sharingAmount.multiply(new BigDecimal(sharingProportion[count]));

            if (Objects.isNull(beneficiaryMember)) {
                return;
            }
            referralId = beneficiaryMember.getReferralId();

//            if (DateUtil.compare(syncOrder.getCreateTime(), beneficiaryMember.getExpireTime()) < 0) {
            if(beneficiaryMember.getIsMember() == YesOrNoEnum.NO.getCode()){
                if (count == 0) {
                    count++;
                }
                continue;
            }
            long directCount = memberService.count(new LambdaQueryWrapper<Member>().eq(Member::getReferralId, beneficiaryMember.getId()));
            if (directCount < (count + 1) && Objects.equals(beneficiaryMember.getTenGenerations(), YesOrNoEnum.NO.getCode())) {
                if (count == 0) {
                    count++;
                }
                continue;
            }
            //记录舍去的小数点
            BigDecimal subtract = amount.subtract(amount.setScale(0, RoundingMode.FLOOR));
            if(subtract.compareTo(BigDecimal.ZERO) > 0){
                syncOrder.setDepositDecimalAmount(syncOrder.getDepositDecimalAmount().add(subtract));
            }
            amount = amount.setScale(0, RoundingMode.FLOOR);
                // 有分佣资格，生成分佣记录
            SharingRecord sharingRecord = new SharingRecord();
            sharingRecord.setSourceMemberId(sourceMember.getId());
            sharingRecord.setSourceReferralCode(sourceMember.getReferralCode());
            sharingRecord.setBeneficiaryMemberId(beneficiaryMember.getId());
            sharingRecord.setBeneficiaryReferralCode(beneficiaryMember.getReferralCode());
            sharingRecord.setAmount(amount);
            sharingRecord.setBillNo(syncOrder.getBillNo());
            sharingRecord.setBillType(syncOrder.getOperationType());
            sharingRecordService.save(sharingRecord);

            //已分佣金额
            syncOrder.setSharingAmount(syncOrder.getSharingAmount().add(amount));

            //减去该层需要分佣的金额
            syncOrder.setNonSharingAmount(syncOrder.getNonSharingAmount().subtract(amount));
            if (count == 0) {
                //1代分佣金额
                syncOrder.setSharingFirstGenerationAmount(sharingRecord.getAmount());
            }
            syncOrder.setSharingGeneration(count + 1);
            count++;
        }
    }
}
