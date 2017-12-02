package com.subnit.proxyspider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description:
 *
 * @author subnit
 * date : create in 下午 5:20 2017/12/2
 * modified by :
 */
@Controller
public class ProxyController {
    @RequestMapping("/abc")
    @ResponseBody
    public String demo() {
        return "hello";
    }
}
