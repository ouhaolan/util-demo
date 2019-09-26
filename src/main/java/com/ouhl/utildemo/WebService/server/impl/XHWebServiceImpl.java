package com.ouhl.utildemo.WebService.server.impl;


import com.ouhl.utildemo.WebService.server.XHWebService;
import org.springframework.stereotype.Component;

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
