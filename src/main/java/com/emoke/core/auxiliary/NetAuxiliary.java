package com.emoke.core.auxiliary;

import com.emoke.core.auxiliary.proxy.Proxy;
import com.emoke.core.auxiliary.util.ReflectUtil;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络辅助核心类
 * */
public class NetAuxiliary {
    private final ConcurrentHashMap<String,Object> proxyObjMap =new ConcurrentHashMap<>();

    public NetAuxiliary()  {

    }
    public void setBasePackage(String basePackage) throws ClassNotFoundException {
        List<String> classes = ReflectUtil.getClazzName(basePackage, true);
        String[] beans = new String[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            Class<?> clsObj = Class.forName(classes.get(i));
            Object obj = Proxy.createProxy(clsObj);
            beans[i]=obj.getClass().getName();
            proxyObjMap.put(classes.get(i),obj);
        }
    }
    public <T> T getApi(Class<T> cls){
        return (T) proxyObjMap.get(cls.getName());
    }
}
