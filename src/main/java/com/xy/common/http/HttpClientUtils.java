package com.xy.common.http;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xy.common.config.StringPool;
import com.xy.common.config.ToolsConf;

/**
 * HttpClientUtils
 */
class HttpClientUtils {
    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * connManager
     */
    private static PoolingHttpClientConnectionManager connManager = null;


    static {
        try {
            // SSLContext
            SSLContextBuilder sslContextbuilder = new SSLContextBuilder();
            SSLContext sslContext = sslContextbuilder.loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier())).build();

            // Create ConnectionManager
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            // Create socket configuration
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);

            // Create message constraints
            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();

            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    //.setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints).build();

            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(ToolsConf.getPoolingHttpClientConnectionManagerMaxTotal());
            connManager.setDefaultMaxPerRoute(ToolsConf.getPoolingHttpClientConnectionManagerDefaultMaxPerRoute());
        } catch (KeyManagementException e) {
            logger.error("KeyManagementException", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        }

    }


    /**
     * 获取response
     *
     * @param reqPack RequestPackage
     * @return ResponsePackage
     */
    static ResponsePackage exec(RequestPackage reqPack) {
        Charset charset = Charset.forName(reqPack.getCharset());
        HttpRequestBase requestBase;
        if ("POST".equalsIgnoreCase(reqPack.getMethod())) {
            //post 方法，创建 httpPost
            HttpPost post = new HttpPost(reqPack.getUrl());
            if (reqPack.getPostContent() != null) {
                //request post content 不为空
                post.setEntity(new StringEntity(reqPack.getPostContent(), charset));
            } else if (reqPack.getNameValuePairs() != null) {
                //request data不为空 则创建UrlEncodedFormEntity
                post.setEntity(new UrlEncodedFormEntity(reqPack.getNameValuePairs(), charset));
            }
            requestBase = post;
        } else if ("GET".equalsIgnoreCase(reqPack.getMethod())) {
            //get 方法
            String fullUrl = reqPack.getUrl();
            if (reqPack.getNameValuePairs() != null) {
                //如果原url包含问号，则追加&  不包含问号，则追加问号
                StringBuilder urlBuffer = new StringBuilder(fullUrl).append(
                        fullUrl.contains(StringPool.Symbol.QUESTION) ?
                                StringPool.Symbol.AMPERSAND :
                                StringPool.Symbol.QUESTION
                );
                urlBuffer.append(URLEncodedUtils.format(reqPack.getNameValuePairs(), charset));
                fullUrl = urlBuffer.toString();
            }
            requestBase = new HttpGet(fullUrl);
        } else {
            logger.error("HTTP METHOD UNSUPPORTED");
            return null;
        }
        if (reqPack.getHeaders() != null && !reqPack.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> kv : reqPack.getHeaders().entrySet()) {
                requestBase.setHeader(kv.getKey(), kv.getValue());
            }
        }
        HttpClientContext context = HttpClientContext.create();
        if (reqPack.getCookies() != null) {
            BasicCookieStore cookieStore = new BasicCookieStore();
            for (Map.Entry<String, String> kv : reqPack.getCookies().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(kv.getKey(), kv.getValue());
                cookie.setPath("/");
                cookieStore.addCookie(cookie);
            }
            context.setCookieStore(cookieStore);
        }
        // set accept
        if (StringUtils.isNotBlank(reqPack.getAccept())) {
            requestBase.setHeader("Accept", reqPack.getAccept());
        }
        // set User-Agent
        if (StringUtils.isNotBlank(reqPack.getUserAgent())) {
            requestBase.setHeader("User-Agent", reqPack.getUserAgent());
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(ToolsConf.getHttpClientSocketTimeout()) //设置传输超时
                .setConnectTimeout(ToolsConf.getHttpClientConnectTimeout()) //设置从pool中获取client的超时时间
                .setConnectionRequestTimeout(ToolsConf.getHttpClientRequestTimeout()).build(); //设置连接超时时间
        requestBase.setConfig(requestConfig);

        ResponsePackage respPack = new ResponsePackage();
        try {
            //从连接池创建HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClients.custom().disableRedirectHandling().setConnectionManager(connManager);
            // client
            CloseableHttpClient client = httpClientBuilder.build();
            // 执行请求
            HttpResponse httpResponse = client.execute(requestBase, context);
            // 获取响应消息实体
            HttpEntity entityRep = httpResponse.getEntity();
            if (entityRep != null) {
                // 获取相应内容
                String responseContent = EntityUtils.toString(entityRep, charset);
                // 获取HTTP响应的状态码
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                respPack.setContent(responseContent);
                respPack.setHttpStatus(statusCode);
                if (statusCode == HttpStatus.SC_OK) {
                    //处理header
                    Header[] allHeaders = httpResponse.getAllHeaders();
                    if (allHeaders != null && allHeaders.length > 0) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (Header header : allHeaders) {
                            map.put(header.getName(), header.getValue());
                        }
                        respPack.setHeaders(map);
                    }
                }
            }
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        } catch (ConnectTimeoutException e) {
            logger.error("ConnectTimeoutException", e);
        } catch (SocketTimeoutException e) {
            logger.error("SocketTimeoutException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            requestBase.releaseConnection();
        }
        return respPack;
    }
}
