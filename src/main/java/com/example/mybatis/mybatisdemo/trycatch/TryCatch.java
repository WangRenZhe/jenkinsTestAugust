package com.example.mybatis.mybatisdemo.trycatch;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TryCatch {

    /**
     * 默认打印response
     * 
     * @return
     */
    boolean recordResponse() default true;

    /**
     * 默认打印request
     * 
     * @return
     */
    boolean recordRequest() default true;
}
