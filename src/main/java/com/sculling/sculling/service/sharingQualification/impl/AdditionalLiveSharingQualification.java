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
 * @desc 额外主播奖励分佣 ADDITIONAL_LIVE
 * 只给自己分5%
 */
@Service
@AllArgsConstructor
public class AdditionalLiveSharingQualification implements SharingQualification {

    private final IMemberService memberService;

    private final ISharingRecordService sharingRecordService;

    private final SharingConfig sharingConfig;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void sharing(SyncOrder syncOrder) {
        String[] sharingProportion = sharingConfig.getAdditionalLiveSharingProportion().split(",");

        // 额外直播打赏分佣
        /*
          a) 本人分5%，即金额 * 5%
         */
        // 进行分佣判定 1.判断是否是会员  2.判定直系数量是否有分佣资格 只给自己分  5%
        Member sourceMember = memberService.getOne(new LambdaQueryWrapper<Member>().eq(Member::getReferralCode, syncOrder.getReferralCode()));
        //从自己开始分
        //先赋值全部金额,再减去需要分佣的金额,就是利润,这样就不会因为四舍五入而对不上
        syncOrder.setNonSharingAmount(syncOrder.getAmount());
        // 这一层需要分佣的金额
        BigDecimal amount = syncOrder.getAmount().multiply(new BigDecimal(sharingProportion[0]));
        if (!Objects.isNull(sourceMember)) {
//            if ((DateUtil.compare(syncOrder.getCreateTime(), sourceMember.getExpireTime()) < 0)) {
            if(sourceMember.getIsMember() == YesOrNoEnum.YES.getCode()){
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
                sharingRecord.setBeneficiaryMemberId(sourceMember.getId());
                sharingRecord.setBeneficiaryReferralCode(sourceMember.getReferralCode());
                sharingRecord.setAmount(amount);
                sharingRecord.setBillNo(syncOrder.getBillNo());
                sharingRecord.setBillType(syncOrder.getOperationType());
                sharingRecordService.save(sharingRecord);
                //已分佣金额
                syncOrder.setSharingAmount(syncOrder.getSharingAmount().add(sharingRecord.getAmount()));
                //减去该层需要分佣的金额
                syncOrder.setNonSharingAmount(syncOrder.getNonSharingAmount().subtract(amount));
            }
        }
        //分佣代数  0表示自己
        syncOrder.setSharingGeneration(0);
    }
}
