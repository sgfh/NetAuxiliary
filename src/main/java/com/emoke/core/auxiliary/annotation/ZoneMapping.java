package com.emoke.core.auxiliary.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZoneMapping {
    /**
     * 域名
     * */
    String url();
}
