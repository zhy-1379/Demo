package com.c.io.http;

import com.c.utils.LoggerUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: HttpClientTest01
 * Description:
 * date: 2021/5/17 9:57
 *
 * @author zhy
 * @since JDK 1.8
 */
public class HttpClientTest01 {
    private static final Logger log = LoggerUtils.getLogger(HttpClientTest01.class);

    private static HttpClient client;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(6000, TimeUnit.MILLISECONDS);
        connectionManager.setMaxTotal(4);
        connectionManager.setDefaultMaxPerRoute(5);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(5000).build();
        // client = HttpClientBuilder
        //         .create()
        //         .setConnectionManager(connectionManager)
        //         .setDefaultRequestConfig(requestConfig)
        //         .build();
        client = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .disableAutomaticRetries()
                .setDefaultRequestConfig(requestConfig).build();
    }

    public void fun01() {
        for (int i = 0; i < 20; i++) {
            String url;
            if (i < 5) {
                url = "http://www.baidu.com";
            } else {
                url = "http://www.hao123.com";
            }
            HttpGet httpGet = new HttpGet(url);
            log.info("------request url:{}", url);
            HttpResponse res;
            try {
                res = client.execute(httpGet);
            } catch (IOException e) {
                log.error("异常： ", e);
                return;
            }
            int code = res.getStatusLine().getStatusCode();
            log.info("------end request url:{}\tcode:{}\ti:{}", url, code, i + 1);
        }
    }

    public void fun02() {
        for (int i = 0; i < 20; i++) {
            String url;
            if (i % 2 == 0) {
                url = "http://www.baidu.com";
            } else {
                url = "http://www.hao123.com";
            }
            HttpGet httpGet = new HttpGet(url);
            log.info("------request url:{}", url);
            HttpResponse res;
            try {
                res = client.execute(httpGet);
            } catch (IOException e) {
                log.error("异常： ", e);
                return;
            }
            int code = res.getStatusLine().getStatusCode();
            log.info("------end request url:{}\tcode:{}\ti:{}", url, code, i + 1);
        }
    }

    public void fun03() {
        for (int i = 0; i < 20; i++) {
            String url;
            if (i % 2 == 0) {
                url = "http://www.baidu.com";
            } else {
                url = "http://www.hao123.com";
            }
            HttpGet httpGet = new HttpGet(url);
            log.info("------request url:{}-----", url);
            HttpResponse res = null;
            try {
                res = client.execute(httpGet);
                int code = res.getStatusLine().getStatusCode();
                log.info("------end request url:{}\tcode:{}\ti:{}", url, code, i + 1);
            } catch (IOException e) {
                log.error("异常： ", e);
            } finally {
                if (res != null) {
                    EntityUtils.consumeQuietly(res.getEntity());
                }
            }
        }
    }

    public static void main(String[] args) {
        HttpClientTest01 httpClientTest01 = new HttpClientTest01();
        // httpClientTest01.fun01();
        // httpClientTest01.fun02();
        httpClientTest01.fun03();
    }

}

