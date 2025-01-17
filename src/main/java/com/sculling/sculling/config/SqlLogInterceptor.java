package com.sculling.sculling.config;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.ly.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.*;

/**
 * 用于输出每条 SQL 语句及其执行时间
 *
 * @author hubin nieqiurong TaoYu
 * @since 2016-07-07
 */
@Slf4j
@Intercepts({
		@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
		@Signature(type = StatementHandler.class, method = "update", args = Statement.class),
		@Signature(type = StatementHandler.class, method = "batch", args = Statement.class)
})
public class SqlLogInterceptor implements Interceptor {


	@Autowired
	private SqlLogTransactionSynchronization sqlLogTransactionSynchronization;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Statement statement = getStatement(invocation.getArgs()[0]);
		String originalSql = getOriginalSql(statement);

		// 计算执行 SQL 耗时
		long start = SystemClock.now();
		Object result = invocation.proceed();
		long timing = SystemClock.now() - start;

		// SQL 打印执行结果
		Object target = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(target);
		MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

		String sqlLog = StringUtils.format(
				"\n==============  Sql Start  ==============" +
						"\nExecute ID  ：{}" +
						"\nExecute SQL ：{}" +
						"\nExecute Time：{} ms" +
						"\nExecute Result：{}" +  // 添加执行结果的输出
						"\n==============  Sql  End   ==============\n",
				ms.getId(), originalSql, timing, JSON.toJSONString(result)
		);

		// 添加 SQL 日志到事务同步器
		if (TransactionSynchronizationManager.isActualTransactionActive()) {
			sqlLogTransactionSynchronization.addSqlLog(sqlLog);
		} else {
			log.info(sqlLog); // 如果没有事务，直接记录日志
		}

		return result;
	}

	private Statement getStatement(Object firstArg) throws Exception {
		if (Proxy.isProxyClass(firstArg.getClass())) {
			Field handlerField = Proxy.class.getDeclaredField("h");
			handlerField.setAccessible(true);
			InvocationHandler handler = (InvocationHandler) handlerField.get(firstArg);
			Field statementField = handler.getClass().getDeclaredField("statement");
			statementField.setAccessible(true);
			return (Statement) statementField.get(handler);
		} else {
			return (Statement) firstArg;
		}
	}

	private String getOriginalSql(Statement statement) {
		return statement.toString().replaceAll("[\\s]+", StringPool.SPACE).substring(indexOfSqlStart(statement.toString()));
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}


	private int indexOfSqlStart(String sql) {
		String upperCaseSql = sql.toUpperCase();
		Set<Integer> set = new HashSet<>();
		set.add(upperCaseSql.indexOf("SELECT "));
		set.add(upperCaseSql.indexOf("UPDATE "));
		set.add(upperCaseSql.indexOf("INSERT "));
		set.add(upperCaseSql.indexOf("DELETE "));
		set.remove(-1);
		if (CollectionUtils.isEmpty(set)) {
			return -1;
		}
		List<Integer> list = new ArrayList<>(set);
		list.sort(Comparator.naturalOrder());
		return list.get(0);
	}
}
