package com.subnit.proxyspider.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description:
 *
 * @author subo
 * date : create in 下午 6:02 2017/12/2 0002
 * modified by :
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProxyServiceImplTest {
    @Autowired
    private ProxyService proxyService;

    @Test
    public void getProxy() throws Exception {
        proxyService.getProxys();
    }

}