package com.emoke.core.auxiliary.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Header {
    /**
     * 请求头content-type
     * */
    String contentType();

    /**
     * 设置多个head的值,指向map
     * */
    String headMapName() default "";

}