package com.example.mybatis.mybatisdemo.trycatch;

import com.example.mybatis.mybatisdemo.check.ParamException;
import com.example.mybatis.mybatisdemo.util.ResultDTO;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Aspect
@Order(1024)
@Component
public class TryCatchAspect {

    @Around("@annotation(com.example.mybatis.mybatisdemo.trycatch.TryCatch)")
    public Object aroundAdviceForAnnotation(ProceedingJoinPoint joinPoint) {

        return aroundAdvice(joinPoint);
    }

    private Object aroundAdvice(ProceedingJoinPoint joinPoint) {
        String simpleClassName = joinPoint.getTarget().getClass().getSimpleName();
        MethodSignature methodSignature = (MethodSignature)(joinPoint.getSignature());
        Class returnType = methodSignature.getReturnType();
        String methodName = methodSignature.getName();
        Object result = null;
        boolean recordResponse = false;
        try {
            Method declaredMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(),
                methodSignature.getMethod().getParameterTypes());
            TryCatch tryCatch = declaredMethod.getAnnotation(TryCatch.class);
            if (tryCatch.recordRequest()) {
                logRequest(joinPoint);
            }
            recordResponse = tryCatch.recordResponse();
            result = joinPoint.proceed();
        } catch (ParamException paramException) {
            log.error("paramException", paramException);
            if (returnType.equals(ResultDTO.class)) {
                result = new ResultDTO<>().build(paramException.getCode(), paramException.getMessage());
            }
        } catch (Throwable throwable) {
            log.error("throwable", throwable);
            if (returnType.equals(ResultDTO.class)) {
                result = new ResultDTO<>().build("FAIL", throwable.getMessage());
            }
        } finally {
            if (recordResponse) {
                log.info("{}#{}() response:{}", simpleClassName, methodName, result);
            }
        }
        return result;
    }

    private void logRequest(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)(joinPoint.getSignature());
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(parameterNames)) {
            List<Object> paramNamesValueList = new ArrayList<>(parameterNames.length);
            for (int i = 0; i < parameterNames.length; i++) {
                paramNamesValueList.add(parameterNames[i] + ":" + args);
            }
            String request = Joiner.on(",").join(paramNamesValueList);
            log.info("{}#{}(), request:{}", joinPoint.getTarget().getClass().getSimpleName(), signature.getName(),
                request);
        }
    }

}
