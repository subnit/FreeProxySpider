package com.subnit.proxyspider.dao;

import com.subnit.proxyspider.dto.Proxy;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

/**
 * description:
 *
 * @author subnit
 * date : create in 下午 5:20 2017/12/2
 * modified by :
 */
public class ProxyServiceImpl implements ProxyService {

    public List<Proxy> getProxy() {
        //
        String url = "http://www.xicidaili.com/nt/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = response.body().toString();
        return null;
    }
}
