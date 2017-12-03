package com.subnit.proxyspider.controller;

import com.alibaba.fastjson.JSON;
import com.subnit.proxyspider.dao.ProxyService;
import com.subnit.proxyspider.dto.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * description:
 *
 * @author subnit
 * date : create in 下午 5:20 2017/12/2
 * modified by :
 */
@Controller
@Slf4j
public class ProxyController {
    private final ProxyService proxyService;

    @Autowired
    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping("/abc")
    @ResponseBody
    public String demo() {
        return "hello";
    }


    @RequestMapping("/getProxys")
    @ResponseBody
    public String getProxys() {
        log.info("getProxys ------ starts");
        List<Proxy> proxys = proxyService.getProxys();
        log.info("getProxys ------ ends , proxy count: {}", proxys != null ? proxys.size() : 0);
        return JSON.toJSONString(proxys);
    }


}
