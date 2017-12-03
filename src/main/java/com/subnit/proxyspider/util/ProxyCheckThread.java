package com.subnit.proxyspider.util;

import com.subnit.proxyspider.dto.Proxy;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * description:
 *
 * @author subo
 * date : create in 下午 11:55 2017/12/2 0002
 * modified by :
 */
public class ProxyCheckThread implements Runnable{

        private List<Proxy> rstList;
        private Proxy proxy;
        private CountDownLatch countDownLatch;
        private int count;
        public ProxyCheckThread(List<Proxy> rstList, Proxy proxy, CountDownLatch countDownLatch, int count) {
            this.proxy = proxy;
            this.rstList = rstList;
            this.countDownLatch = countDownLatch;
            this.count = count;
        }
        @Override
        public void run() {
            double time = ProxyUtil.checkProxy(proxy);
            if (time != 0) {
                proxy.setTime(time);
                rstList.add(proxy);
            }
            countDownLatch.countDown();
        }
}
