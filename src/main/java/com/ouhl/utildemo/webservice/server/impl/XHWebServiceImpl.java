package com.ouhl.utildemo.webservice.server.impl;


import com.ouhl.utildemo.webservice.server.XHWebService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * web server 实现类
 */
@Component
public class XHWebServiceImpl implements XHWebService {


    @Override
    public String sendEncryptMsg(String id ,String str) throws Exception {
        return "尊敬的 "+str+" 您好呀!您的 ID 为 "+id;
    }
}
