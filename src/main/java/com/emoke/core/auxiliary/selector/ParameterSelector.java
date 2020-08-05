package com.emoke.core.auxiliary.selector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ParameterSelector {
    public Object selectParameter(Object[] args, Method method, Annotation ann){
        if (args != null) {
            for (Object o : args) {
                if (null == o) {
                    continue;
                }
                Annotation[][] annotations=   method.getParameterAnnotations();
                for (Annotation[] annotation : annotations) {
                    Annotation parameter = annotation[0];
                    System.out.println("parameter======"+parameter.getClass().getName());
                    System.out.println("ann======"+ann.getClass().getName());
//                    if (parameter instanceof cls) {
//                        if (o instanceof Map) {
//                            return (Map<String, Object>) o;
//                        }
//                    }
                }

            }
        }
        return null;
    }
}
