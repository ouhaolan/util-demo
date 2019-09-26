package com.ouhl.utildemo.WebService.server;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * web server 类
 */
@WebService(targetNamespace="http://server.ouhl.utildemo",endpointInterface = "com.ouhl.utildemo.WebService.server.XHWebService")
public interface XHWebService {

    @WebMethod
    String sendEncryptMsg(String id, String str) throws Exception;
}
