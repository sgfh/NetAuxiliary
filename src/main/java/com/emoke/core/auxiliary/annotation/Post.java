package com.emoke.core.auxiliary.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Post {
    /**
     * 请求地址
     * */
    String path();

}