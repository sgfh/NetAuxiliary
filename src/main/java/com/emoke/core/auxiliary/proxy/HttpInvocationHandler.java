package com.emoke.core.auxiliary.proxy;

import com.emoke.core.auxiliary.annotation.*;
import com.emoke.core.auxiliary.http.HttpUtil;
import com.emoke.core.auxiliary.resp.HttpResp;
import com.emoke.core.auxiliary.selector.ParameterSelector;
import com.emoke.core.auxiliary.util.TextUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class HttpInvocationHandler implements InvocationHandler {
    private ParameterSelector parameterSelector = new ParameterSelector();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //已实现的具体类
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            //接口
        } else {
            Get get = method.getAnnotation(Get.class);
            Post post = method.getAnnotation(Post.class);
            Put put = method.getAnnotation(Put.class);
            Delete delete = method.getAnnotation(Delete.class);
            String url = "";
            if (null != get) {
                url = get.path();
            } else if (null != post) {
                url = post.path();
            } else if (null != put) {
                url = put.path();
            } else if (null != delete) {
                url = delete.path();
            } else {
                throw new RuntimeException("Request method comment not found");
            }
            long t1 = System.currentTimeMillis();
            Object o = run(method, args, get, post, put, delete);
            System.out.println("api:" + url + " response time:" + (System.currentTimeMillis() - t1) + "ms");
            return o;
        }
        return null;
    }

    public Object run(Method method, Object[] args, Get get, Post post, Put put, Delete delete) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        ZoneMapping zoneMapping = method.getDeclaringClass().getAnnotation(ZoneMapping.class);
        if (null == zoneMapping) {
            throw new RuntimeException("Domain name not found,please set ZoneMapping annotation");
        }

        if (null != get) {
            return HttpUtil.getInstance().get(parsePathVariable(zoneMapping.url() + get.path(), args, method), parseQuery(args, method), (Class<? extends HttpResp>) method.getReturnType(), head(args, method));
        } else if (null != post) {
            return HttpUtil.getInstance().post(parsePathVariable(zoneMapping.url() + post.path(), args, method), parseQuery(args, method), parseMultipartMap(args, method), (Class<? extends HttpResp>) method.getReturnType(), head(args, method));
        } else if (null != put) {
            return HttpUtil.getInstance().put(parsePathVariable(zoneMapping.url() + put.path(), args, method), parseQuery(args, method), (Class<? extends HttpResp>) method.getReturnType(), head(args, method));
        } else if (null != delete) {
            return HttpUtil.getInstance().delete(parsePathVariable(zoneMapping.url() + delete.path(), args, method), parseQuery(args, method), (Class<? extends HttpResp>) method.getReturnType(), head(args, method));
        } else {
            throw new RuntimeException("Request method comment not found");
        }
    }


    /**
     * 组装head参数
     */
    private Map<String, Object> head(Object[] args, Method method) {
        //定义head的map
        Map<String, Object> headMap = new HashMap<>();
        if (args != null) {
            if (!method.isAnnotationPresent(Header.class)) {
                return headMap;
            }
            Header header = method.getAnnotation(Header.class);
            String contentType = header.contentType();
            if (!TextUtils.isEmpty(contentType)) {
                headMap.put("Content-Type", contentType);
            }
            String headMapName = header.headMapName();
            if (TextUtils.isEmpty(headMapName)) {
                return headMap;
            }
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (!parameter.getName().equals(headMapName)) {
                    continue;
                }
                if (!(args[i] instanceof Map)) {
                    throw new RuntimeException("head must be map values");
                }
                headMap.putAll((Map<? extends String, ?>) args[i]);
            }
        }
        return null;
    }

    /**
     * 查询map组装
     *
     * @param args :参数集合
     */
    private Map<String, Object> parseQuery(Object[] args, Method method) {
        if (args != null) {
            for (Object o : args) {
                if (null == o) {
                    continue;
                }
                Annotation[][] annotations = method.getParameterAnnotations();
                for (Annotation[] annotation : annotations) {
                    if (annotation.length == 0) {
                        continue;
                    }
                    Annotation parameter = annotation[0];
                    if (parameter instanceof Query) {
                        if (o instanceof Map) {
                            return (Map<String, Object>) o;
                        }
                    }
                }

            }
        }
        return null;
    }


    /**
     * 查询是否包含@PathVariable标签,使用rest方式url
     */
    private String parsePathVariable(String url, Object[] args, Method method) {
        if (args != null) {
            for (Object o : args) {
                if (null == o) {
                    continue;
                }
                Annotation[][] annotations = method.getParameterAnnotations();
                for (Annotation[] annotation : annotations) {
                    if (annotation.length == 0) {
                        continue;
                    }
                    Annotation parameter = annotation[0];
                    if (parameter instanceof PathVariable) {
                        Parameter[] parameters = method.getParameters();
                        for (int i = 0; i < parameters.length; i++) {
                            Parameter parameter1 = parameters[i];
                            if (null != parameter1.getAnnotation(PathVariable.class)) {
                                url = url.replace("{" + parameter1.getName() + "}", args[i].toString());
                            }
                        }
                    }
                }


            }
        }
        return url;
    }

    /**
     * 查询map组装Map<String,File>
     *
     * @param args :参数集合
     */
    private Map<String, File> parseMultipartMap(Object[] args, Method method) {
        if (args != null) {
            for (Object o : args) {
                if (null == o) {
                    continue;
                }
                Annotation[][] annotations = method.getParameterAnnotations();
                for (Annotation[] annotation : annotations) {
                    if (annotation.length == 0) {
                        continue;
                    }
                    Annotation parameter = annotation[0];
                    if (parameter instanceof MultipartMap) {
                        if (o instanceof Map) {
                            return (Map<String, File>) o;
                        }
                    }
                }
            }
        }
        return null;
    }

}
