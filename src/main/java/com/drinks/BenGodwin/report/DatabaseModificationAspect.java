package com.drinks.BenGodwin.report;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class DatabaseModificationAspect {

    private final SqlDumpUtil sqlDumpUtil;
    private final ExecutorService executorService;

    @Autowired
    public DatabaseModificationAspect(SqlDumpUtil sqlDumpUtil) {
        this.sqlDumpUtil = sqlDumpUtil;
        this.executorService = Executors.newFixedThreadPool(1); // Create a single-threaded executor
    }


    @Pointcut("execution(* com.drinks.BenGodwin.service..*(..))")
    public void serviceMethods() {}

    @AfterReturning("serviceMethods()")
    public void afterReturningServiceMethods() {
        log.info("Database modification detected, creating SQL dumps for each table.");

        // Run the backup process asynchronously
        executorService.submit(() -> {
            sqlDumpUtil.createSqlDump("brand");
            sqlDumpUtil.createSqlDump("batch");
            sqlDumpUtil.createSqlDump("customer");
            sqlDumpUtil.createSqlDump("role");
            sqlDumpUtil.createSqlDump("sale");
            sqlDumpUtil.createSqlDump("transaction");
            sqlDumpUtil.createSqlDump("transaction_item");
            sqlDumpUtil.createSqlDump("users");
            sqlDumpUtil.createSqlDump("users_roles");
        });
    }
}