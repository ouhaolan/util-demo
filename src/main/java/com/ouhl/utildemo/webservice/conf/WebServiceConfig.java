package com.ouhl.utildemo.webservice.conf;


import com.ouhl.utildemo.webservice.server.XHWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * web server 配置类
 *
 * 注意：如出现添加 web server 模块导致无法启动项目的情况，
 *      通常是 POM 中的 parent 版本和 jaxws 版本相冲突。
 */
@Configuration
public class WebServiceConfig {

    @Autowired
    private  Bus bus;

    @Autowired
    private XHWebService xhWebService;

    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        ///webservice 设置接口路径，可通过localhost:8080/webservice 访问到该接口
        return new ServletRegistrationBean(new CXFServlet(), "/webservice/*");
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, xhWebService);
        endpoint.publish("/utildemo");
        return endpoint;
    }

}
