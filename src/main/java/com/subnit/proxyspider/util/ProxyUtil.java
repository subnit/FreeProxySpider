package com.subnit.proxyspider.util;

import com.subnit.proxyspider.dto.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * description:
 *
 * @author subo
 * date : create in 下午 11:45 2017/12/2 0002
 * modified by :
 */
 class ProxyUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProxyUtil.class);
    /**
     * description : check one proxy
     *               检查一个代理是否可用
     * @author subnit
     * @return
     * date :
     * modified by : [change date YYYY-MM-DD][name][description]
     */
    static double checkProxy(Proxy proxy){
        double time = 0;
        URL url = null;
        try {
            url = new URL("http://www.baidu.com/");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        // create proxy server
        InetSocketAddress addr = new InetSocketAddress(proxy.getIp(), proxy.getPort());
        java.net.Proxy mProxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, addr);
        HttpURLConnection conn;
        try {
            if (url != null) {
                conn = (HttpURLConnection)url.openConnection(mProxy);
                conn.setConnectTimeout(3000);
                long startTimeStamp = System.currentTimeMillis();
                conn.connect();
                int code = conn.getResponseCode();
                if(code == 200){
                    long entTimeStamp = System.currentTimeMillis();
                    time = (double)(entTimeStamp - startTimeStamp) / 1000;
                }
            }
        } catch (IOException e) {
            logger.debug("get connect failed, message:{}", e.getMessage());
        }
        return time;
    }

}
