package com.emoke.core.auxiliary.http;

import com.alibaba.fastjson.JSON;
import com.emoke.core.auxiliary.pojo.Test;
import com.emoke.core.auxiliary.resp.HttpResp;
import com.emoke.core.auxiliary.util.ReflectUtil;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private OkHttpClient okHttpClient;

    private OkHttpClient getOkHttpClient() {
        if (null == okHttpClient) {
            synchronized (HttpUtil.class) {
                if (null == okHttpClient) {
                    okHttpClient = new OkHttpClient.Builder()
                            //设置连接超时时间
                            .connectTimeout(15, TimeUnit.SECONDS)
                            //设置读取超时时间
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }

        return okHttpClient;
    }

    private static HttpUtil httpUtil;

    enum HttpUtilSingletonEnum {
        //创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private HttpUtil httpUtil;

        //私有化枚举的构造函数
        private HttpUtilSingletonEnum() {
            httpUtil = new HttpUtil();
        }

        public HttpUtil getInstance() {
            return httpUtil;
        }
    }

    public static HttpUtil getInstance() {
        return HttpUtilSingletonEnum.INSTANCE.getInstance();
    }


    /**
     * get请求--无参数
     *
     * @param url         :请求地址
     * @param cls         :返回类型
     * @param paramMap    :请求参数
     * @param headMap :头部设置
     */
    public Object get(String url, Map<String, Object> paramMap, Class<? extends HttpResp> cls, Map<String, Object> headMap) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Request.Builder requestBuilder = new Request.Builder();

        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode(entry.getValue().toString(), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        try {
            addHeader(requestBuilder, headMap);
            requestBuilder.url(urlBuilder.toString()).get();
            return callResult(requestBuilder, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return exceptionResp(e, cls, url);
        }
    }


    /**
     * post请求--无file,纯参数请求
     *
     * @param url         :请求地址
     * @param cls         :返回类型
     * @param mapParams   :请求参数
     * @param headMap :头部设置
     */
    public Object post(String url, Map<String, Object> mapParams, Class<? extends HttpResp> cls,Map<String, Object> headMap) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        FormBody.Builder form = new FormBody.Builder();
        if (null != mapParams) {
            for (String key : mapParams.keySet()) {
                form.add(key, mapParams.get(key) + "");
            }
        }
        Request.Builder requestBuilder = buildRequest(url);
        try {
            addHeader(requestBuilder, headMap);
            requestBuilder.post(form.build());
            return callResult(requestBuilder, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return exceptionResp(e, cls, url);
        }
    }

    /**
     * put请求--参数请求
     *
     * @param url         :请求地址
     * @param cls         :返回类型
     * @param mapParams   :请求参数
     * @param headMap :头部设置
     */
    public Object put(String url, Map<String, Object> mapParams, Class<? extends HttpResp> cls,Map<String, Object> headMap) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        FormBody.Builder form = new FormBody.Builder();
        if (null != mapParams) {
            for (String key : mapParams.keySet()) {
                form.add(key, mapParams.get(key) + "");
            }
        }
        Request.Builder requestBuilder = buildRequest(url);
        try {
            addHeader(requestBuilder, headMap);
            requestBuilder.put(form.build());
            return callResult(requestBuilder, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return exceptionResp(e, cls, url);
        }
    }

    /**
     * delete--参数请求
     *
     * @param url         :请求地址
     * @param cls         :返回类型
     * @param mapParams   :请求参数
     * @param headMap :头部设置
     */
    public Object delete(String url, Map<String, Object> mapParams, Class<? extends HttpResp> cls, Map<String, Object> headMap) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        FormBody.Builder form = new FormBody.Builder();
        if (null != mapParams) {
            for (String key : mapParams.keySet()) {
                form.add(key, mapParams.get(key) + "");
            }
        }
        Request.Builder requestBuilder = buildRequest(url);
        try {
            addHeader(requestBuilder, headMap);
            requestBuilder.delete(form.build());
            return callResult(requestBuilder, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return exceptionResp(e, cls, url);
        }
    }

    /**
     * post请求--带有file的form-data
     *
     * @param url         :请求地址
     * @param cls         :返回类型
     * @param mapParams   :请求参数
     * @param headMap :头部设置
     */
    public Object post(String url, Map<String, Object> mapParams, Map<String, File> fileMap, Class<? extends HttpResp> cls, Map<String, Object> headMap) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        if (null == fileMap || fileMap.size() == 0) {
            //不存在file或者file为null或者0，此时执行无file参数post方法
            return post(url, mapParams, cls, headMap);
        }
        MultipartBody.Builder multipartBuild = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (null != mapParams) {
            for (String s : mapParams.keySet()) {
                multipartBuild.addFormDataPart(s, mapParams.get(s).toString());
            }
        }
        //遍历出file
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
            RequestBody fileBody = RequestBody.Companion.create(entry.getValue(), MediaType.Companion.parse(com.emoke.core.auxiliary.pojo.MediaType.FormData.getLabel()));
            multipartBuild.addFormDataPart(entry.getKey(), entry.getValue().getName(), fileBody);
        }
        Request.Builder requestBuilder = buildRequest(url);
        try {
            addHeader(requestBuilder, headMap);
            requestBuilder.post(multipartBuild.build());
            return callResult(requestBuilder, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return exceptionResp(e, cls, url);
        }
    }

    /**
     * 初始化Request.Builder
     */
    private Request.Builder buildRequest(String url) {
        Request.Builder requestBuilder = new Request.Builder();
        return requestBuilder.url(url);
    }

    /**
     * 执行结果
     */
    private Object callResult(Request.Builder requestBuilder, Class<? extends HttpResp> cls) throws IOException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        final Request request = requestBuilder.build();
        Call call = getOkHttpClient().newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {

            ResponseBody responseBody = response.body();
            if (null != responseBody) {
                String resp = responseBody.string();
                System.out.println("请求结果=" + resp);
                return parseResp(JSON.parseObject(resp, cls), response);
            }
        } else {
            return parseResp(ReflectUtil.newInstance(cls), response);
        }
        return null;
    }

    /**
     * 处理异常
     *
     * @param e           :异常父类
     * @param cls:目标class
     * @param url         :请求url
     */
    private Object exceptionResp(Exception e, Class<?> cls, String url) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        if (e instanceof NoSuchFieldException || e instanceof IllegalAccessException || e instanceof InstantiationException) {
            Object o = ReflectUtil.newInstance(cls);
            Class<?> superClass = o.getClass().getSuperclass();
            superClass.getDeclaredField(HttpResp.IS_SUCCESS).set(o, false);
            superClass.getDeclaredField(HttpResp.STATUS_CODE).set(o, -1);
            superClass.getDeclaredField(HttpResp.HTTP_ERROR_MSG).set(o, "未知错误!");
        } else if (e instanceof SocketTimeoutException) {
            Object o = ReflectUtil.newInstance(cls);
            Class<?> superClass = o.getClass().getSuperclass();
            superClass.getDeclaredField(HttpResp.IS_SUCCESS).set(o, false);
            superClass.getDeclaredField(HttpResp.STATUS_CODE).set(o, 408);
            superClass.getDeclaredField(HttpResp.HTTP_ERROR_MSG).set(o, "请求超时!");
            return o;
        } else if (e instanceof ConnectException) {
            Object o = ReflectUtil.newInstance(cls);
            Class<?> superClass = o.getClass().getSuperclass();
            System.out.println(superClass+"--------"+superClass.getSuperclass());
            superClass.getDeclaredField(HttpResp.IS_SUCCESS).set(o, false);
            superClass.getDeclaredField(HttpResp.STATUS_CODE).set(o, 408);
            superClass.getDeclaredField(HttpResp.HTTP_ERROR_MSG).set(o, "无法连接" + url);
            return o;
        } else {
            Object o = ReflectUtil.newInstance(cls);
            Class<?> superClass = o.getClass().getSuperclass();
            superClass.getDeclaredField(HttpResp.IS_SUCCESS).set(o, false);
            superClass.getDeclaredField(HttpResp.STATUS_CODE).set(o, 500);
            superClass.getDeclaredField(HttpResp.HTTP_ERROR_MSG).set(o, "未知错误:" + e.toString());
            return o;

        }
        return null;
    }

    private void addHeader(Request.Builder requestBuilder, Map<String, Object> headMap) {
        if (null == headMap) {
            return;
        }
        for (Map.Entry<String, Object> entry : headMap.entrySet()) {
            if (null != entry.getValue())
                requestBuilder.addHeader(entry.getKey(), entry.getValue().toString());
        }
        requestBuilder.build();
    }

    /**
     * 组装基础object
     *
     * @param o        :原始子类
     * @param response :http返回响应
     */
    private Object parseResp(Object o, Response response) throws NoSuchFieldException, IllegalAccessException {
        Class<?> superClass = o.getClass().getSuperclass();
        superClass.getDeclaredField(HttpResp.IS_SUCCESS).set(o, response.isSuccessful());
        superClass.getDeclaredField(HttpResp.STATUS_CODE).set(o, response.code());
        superClass.getDeclaredField(HttpResp.HTTP_ERROR_MSG).set(o, response.message());
        superClass.getDeclaredField(HttpResp.HEADERS).set(o, response.headers());
        return o;
    }


    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Test o = (Test) ReflectUtil.newInstance(Test.class);
        Class<?> superClass = o.getClass().getSuperclass();
        try {
            superClass.getDeclaredField("isSuccess").set(o, true);
            superClass.getDeclaredField("statusCode").set(o, 1);
            superClass.getDeclaredField("httpErrorMsg").set(o, "error");

            System.out.println("o====" + o.isSuccess());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

}