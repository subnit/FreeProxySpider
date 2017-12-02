package com.subnit.proxyspider.dto;

import lombok.Data;

/**
 * description:  a proxy
 *
 * @author subnit
 * date : create in 下午 5:20 2017/12/2
 * modified by :
 */
@Data
public class Proxy {
    private String ip;
    private String port;
    private String location;
    private String type;

    public Proxy(String ip, String port, String location) {
        this.ip = ip;
        this.port = port;
        this.location = location;
    }

    public Proxy(String ip, String port, String location, String type) {
        this.ip = ip;
        this.port = port;
        this.location = location;
        this.type = type;
    }

    public Proxy(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }
}
