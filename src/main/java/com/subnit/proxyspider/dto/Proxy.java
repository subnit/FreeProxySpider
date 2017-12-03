package com.subnit.proxyspider.dto;

import lombok.Data;
import lombok.Getter;

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
    private int port;
    private double time;
    private String type;

    public Proxy(String ip, int port, double time) {
        this.ip = ip;
        this.port = port;
        this.time = time;
    }

    public Proxy(String ip, int port, double time, String type) {
        this.ip = ip;
        this.port = port;
        this.time = time;
        this.type = type;
    }

    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
