package com.xy.common.http;

import java.util.Map;

public class HttpUtils {

    public static String GetURIStream(String URI, Map<String, String> setParameterMap, 
    		Map<String, String> requestHeaderMap, String encode) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setUrl(URI);
        requestPackage.setHeaders(requestHeaderMap);
        requestPackage.setCharset(encode);
        requestPackage.setMethod("GET");
        try {
            ResponsePackage response = requestPackage.getResponse();
            if (response != null && response.isSuccess()) {
                return response.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String PostURIStream(String URI, Map<String, String> postParam, 
    		Map<String, String> setParameterMap, Map<String, String> requestHeaderMap, String encode) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setUrl(URI);
        requestPackage.setHeaders(requestHeaderMap);
        requestPackage.setCharset(encode);
        requestPackage.setMethod("POST");
        requestPackage.setNameValuePairs(postParam);
        try {
            ResponsePackage response = requestPackage.getResponse();
            if (response != null && response.isSuccess()) {
                return response.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
