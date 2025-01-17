package com.sculling.sculling.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author sean
 * @Date 2025/1/17 15:18
 * @desc 事务拦截，如果事务不提交则不输出日志
 */
@Component
@Slf4j
public class SqlLogTransactionSynchronization implements TransactionSynchronization {

    private final List<String> sqlLogs = new ArrayList<>();

    public void addSqlLog(String sqlLog) {
        sqlLogs.add(sqlLog);
    }

    @Override
    public void afterCommit() {
        for (String sqlLog : sqlLogs) {
            log.info(sqlLog);
        }
        sqlLogs.clear();
    }

    @Override
    public void afterCompletion(int status) {
        if (status != STATUS_COMMITTED) {
            sqlLogs.clear();
        }
    }
}