package com.sculling.sculling.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
@Author sean
@Date 2025/2/8 15:53 
@desc
*/
@Aspect
@Component
@AllArgsConstructor
@Slf4j
public class TransactionConfig {

    private final SqlLogTransactionSynchronization sqlLogTransactionSynchronization;

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        // 确保事务已激活
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            log.info("Transaction synchronization is active");
            TransactionSynchronizationManager.registerSynchronization(sqlLogTransactionSynchronization);
        } else {
            log.warn("Transaction synchronization is not active");
        }

        // 执行方法
        return joinPoint.proceed();
    }
}
