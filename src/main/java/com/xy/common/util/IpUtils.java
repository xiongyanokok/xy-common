package com.xy.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.xy.common.config.StringPool;

/**
 * IpUtils
 */
public class IpUtils {

    /**
     * ip环境变量的keys
     */
    static String[] args = new String[]{"x-forwarded-for", "Proxy-Client-IP", "X-Forwarded-For", "WL-Proxy-Client-IP", "X-Real-IP"};

    /**
     * 获取当前ip
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = null;
        for (String key : args) {
            ip = request.getHeader(key);
            if (StringUtils.isNotBlank(ip)) {
                String[] ips = ip.split(StringPool.Symbol.COMMA);//有可能是多个ip,取第一个.
                ip = ips[0];
                break;
            }
        }
        return ip;
    }

    /**
     * string类型的ip转换为number类型
     *
     * @param ip xxx.xxx.xxx.xxx
     * @return long 3663452325
     */
    public static long encodeIp(String ip) {
        long ret = 0;
        if (ip == null) {
            return ret;
        }
        String[] segs = ip.split("\\.");

        for (int i = 0; i < segs.length; i++) {
            long seg = Long.parseLong(segs[i]);
            ret += (seg << ((3 - i) * 8));
        }

        return ret;
    }

    /**
     * number类型的ip转换为string类型
     *
     * @param ipLong 3663452325
     * @return String xxx.xxx.xxx.xxx
     */
    public static String decodeIp(long ipLong) {
        StringBuilder ip = new StringBuilder(String.valueOf(ipLong >> 24));
        ip.append(StringPool.Symbol.DOT);
        ip.append(String.valueOf((ipLong & 16711680) >> 16));
        ip.append(StringPool.Symbol.DOT);
        ip.append(String.valueOf((ipLong & 65280) >> 8));
        ip.append(StringPool.Symbol.DOT);
        ip.append(String.valueOf(ipLong & 255));

        return ip.toString();
    }
}
