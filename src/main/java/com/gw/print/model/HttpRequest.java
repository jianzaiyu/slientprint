package com.gw.print.model;


import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 自定义http请求对象
 */
public class HttpRequest {
    private String requestString;
    private String requestMethod;
    private String uri;
    private String method;
    private Map<String, String> parameter;
    private Map<String, byte[]> multiPartFiles;
    private String origin;

    /**
     * 构造函数，传入netty的Http请求对象
     *
     * @param msg http请求对象
     * @throws UnsupportedEncodingException 编码异常
     */
    public HttpRequest(FullHttpRequest msg) {
        this.requestString = msg.toString();
        HttpMethod method = msg.method();
        this.requestMethod = StringUtils.upperCase(method.name());
        this.uri = msg.uri();

        QueryStringDecoder uriDecoder = new QueryStringDecoder(uri);
        this.method = uriDecoder.path().substring(1);

        this.origin = Optional.ofNullable(msg.headers().get(HttpHeaderNames.ORIGIN))
                .filter(str -> !StringUtils.equals(str, "null")).orElse("");

        if (method.equals(HttpMethod.GET)) {
            parameter = convertParam(uriDecoder.parameters());
        } else if (method.equals(HttpMethod.POST)) {
            // 请求内容
           processMethodPostRequestParams(msg);
        }
    }


    private void processMethodPostRequestParams(FullHttpRequest request) {
        Map<String,String> param = new HashMap<>();
        Map<String,byte[]> fileParam = new HashMap<>();
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : httpPostData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                param.put(attribute.getName(), attribute.getValue());
            }else if(data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload){
                MemoryFileUpload fileUpload = (MemoryFileUpload) data;
                fileParam.put(fileUpload.getFilename(),fileUpload.get());
            }
        }
        parameter = param;
        multiPartFiles = fileParam;
    }

    /**
     * 解析参数
     *
     * @param paramStr 字符串
     * @return 参数键值对
     */
    private static Map<String, String> convertParam(Map<String, List<String>> paramStr) {
        Map<String, String> param = new HashMap<>(20);
        for (Map.Entry<String, List<String>> attr : paramStr.entrySet()) {
            param.put(attr.getKey(), attr.getValue().get(0));
        }
        return param;
    }

    public Map<String, String> getParameter() {
        return parameter;
    }

    public String getMethod() {
        return method;
    }

    public String getOrigin() {
        return origin;
    }

    public String getRequestString() {
        return requestString;
    }

    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


    public Map<String, byte[]> getMultiPartFiles() {
        return multiPartFiles;
    }

    public void setMultiPartFiles(Map<String, byte[]> multiPartFiles) {
        this.multiPartFiles = multiPartFiles;
    }
}
