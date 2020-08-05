package com.emoke.core.auxiliary.proxy;

public class Proxy {
    public static Object createProxy(Class<?> cls) {
        return java.lang.reflect.Proxy.newProxyInstance(
                cls.getClassLoader(),
                new Class[]{cls},
                new HttpInvocationHandler());
    }
}