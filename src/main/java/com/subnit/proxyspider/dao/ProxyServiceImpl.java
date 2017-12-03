package com.subnit.proxyspider.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.subnit.proxyspider.dto.Proxy;
import com.subnit.proxyspider.util.ProxyCheckThread;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description:
 *
 * @author subnit
 * date : create in 下午 5:20 2017/12/2
 * modified by :
 */
@Service("proxyService")
public class ProxyServiceImpl implements ProxyService {
    private static final Logger logger = LoggerFactory.getLogger(ProxyServiceImpl.class);

    @Override
    public List<Proxy> getProxys() {
        List<Proxy> proxyList = new LinkedList<>();
        proxyList.addAll(getProxyFromXici());
        proxyList.addAll(getProxyFromXdaili());
        proxyList = checkProxyList(proxyList);
        Comparator<Proxy> comparator = new Comparator<Proxy>() {
            public int compare(Proxy p1, Proxy p2) {
                return Double.compare(p1.getTime(),p2.getTime());
            }
        };
        Collections.sort(proxyList, comparator);
        return proxyList;
    }

    /**
     * description : get Proxy from xici without check
     *
     * @return date :
     * modified by : [change date YYYY-MM-DD][name][description]
     * @author subnit
     */
    private List<Proxy> getProxyFromXici() {
        // get proxy from xici
        // 从西刺获取代理
        List<Proxy> proxyList = new LinkedList<>();
        String url = "http://www.xicidaili.com/nt";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("Connection", "keep-alive")
                .url(url)
                .build();
        String rst;
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body == null) {
                return proxyList;
            }
            rst = body.string().replaceAll("\\s", "");
        } catch (Exception e) {
            logger.error("request failed, web: {}", url);
            return proxyList;
        }
        Pattern p = Pattern.compile("<trclass=\"[\\w]{0,10}\"><tdclass=\"country\"><imgsrc=\"[\\S]{1,60}\"alt=\"[\\w]{0,10}\"/></td><td>([\\d]{1,3}.[\\d]{1,3}.[\\d]{1,3}.[\\d]{1,3})</td><td>([\\d]{1,6})</td><td><ahref=\"[\\S]{0,40}\">[\\S]{0,25}</a></td><tdclass=\"country\">[\\S]{0,10}</td><td>([\\w]{0,10})</td><tdclass=\"country\"><divtitle=\"([\\S]{0,10})秒\"class=\"bar\"><divclass=\"bar_innerfast\"style=\"width:[\\d]{0,3}%\"></div></div></td><tdclass=\"country\"><divtitle=\"[\\S]{0,10}秒\"class=\"bar\"><divclass=\"bar_innerfast\"style=\"width:[\\d]{0,3}%\"></div></div></td><td>[\\S]{0,10}</td><td>[\\S]{0,30}</td></tr>");
        Matcher m = p.matcher(rst);
        while (m.find()) {
            String ip = m.group(1);
            int port = Integer.parseInt(m.group(2));
            String type = m.group(3);
            double time = Double.parseDouble(m.group(4));
            proxyList.add(new Proxy(ip, port, time, type));
        }
        return proxyList;
    }


    /**
     * description : get Proxy from Xdaili without check
     *
     * @return date :
     * modified by : [change date YYYY-MM-DD][name][description]
     * @author subnit
     */
    private List<Proxy> getProxyFromXdaili() {
        // get proxy from Xdaili
        // 从讯代理获取代理
        List<Proxy> proxyList = new LinkedList<>();
        String url = "http://www.xdaili.cn/ipagent/freeip/getFreeIps";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("Connection", "keep-alive")
                .url(url)
                .build();
        String rst;
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body == null) {
                return proxyList;
            }
            rst = body.string();
        } catch (Exception e) {
            logger.error("request failed, web: {}", url);
            return proxyList;
        }
        JSONObject jo = JSONObject.parseObject(rst);
        JSONObject result = jo.getJSONObject("RESULT");
        JSONArray rows = result.getJSONArray("rows");
        for (int i = 0; i < rows.size(); i++) {
            JSONObject ipJo = rows.getJSONObject(i);
            String ip = ipJo.getString("ip");
            int port = ipJo.getInteger("port");
            double time = ipJo.getDouble("responsetime");
            String type = ipJo.getString("type");
            proxyList.add(new Proxy(ip, port, time, type));
        }
        return proxyList;
    }


    /**
     * description : check proxys
     * 检查多个代理是否可用
     *
     * @return date :
     * modified by : [change date YYYY-MM-DD][name][description]
     * @author subnit
     */
    private List<Proxy> checkProxyList(List<Proxy> proxyList) {
        long startTime = System.currentTimeMillis();
        List<Proxy> rstList = new Vector<>();
        CountDownLatch countDownLatch = new CountDownLatch(proxyList.size());
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        int i = 1;
        for (Proxy proxy : proxyList) {
            ProxyCheckThread proxyCheckThread = new ProxyCheckThread(rstList, proxy, countDownLatch, i);
            executorService.execute(proxyCheckThread);
            i++;
        }
        try {
            countDownLatch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long entTime = System.currentTimeMillis();
        double takes = (entTime - startTime) / 1000;
        logger.debug("check proxys takes {} seconds", takes);
        return rstList;
    }


}
