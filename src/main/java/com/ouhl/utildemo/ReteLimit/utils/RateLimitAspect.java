package com.ouhl.utildemo.ReteLimit.utils;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.cxf.common.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Aspect
@Component
@Order(100)
public class RateLimitAspect {

    /**
     * Guava RateLimiter提供的令牌桶算法可用于平滑突发限流（SmoothBursty）和平滑预热限流（SmoothWarmingUp）实现。
     * 设计思路----------------------------
     * 进入切面后，先进入环绕通知，环绕通知里取出注解限定的速度，并以该速度生产令牌放入桶中，(令牌生产速度为 每秒多少个)，在ConcurrentHashMap中以类名+方法名作为key,以RateLimit作为value
     * * before执行时先去桶中获取令牌，获取不到阻塞一段时间。
     */
    private final static Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);

    private static Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.ouhl.utildemo.ReteLimit.utils.RateLimit)")
    public void rateLimitCutPoint() {
        logger.info("编写切入点");
    }

    /**
     * 设置前置通知
     *
     * @param joinPoint
     */
    @Before("rateLimitCutPoint()")
    public void rateLimitRefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getTarget().getClass().getSimpleName();
        RateLimiter rateLimiter = (RateLimiter) rateLimiterMap.getOrDefault(methodName, RateLimiter.create(134));
        double waitTime = rateLimiter.acquire();
        logger.info("acquire spend {} seconds", waitTime);
    }

    /**
     * 环绕通知必须有返回值, 返回值即为目标方法的返回值
     *
     * @param proceedingJoinPoint
     * @throws Throwable
     */
    @Around("rateLimitCutPoint()")
    public Object rateLimitAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取request,response
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        // 或者url(存在map集合的key)
        String url = request.getRequestURI();
        // 获取自定义注解
        RateLimit rateLimit = getAnRateLimiter(proceedingJoinPoint);
        if (rateLimit != null) {
            RateLimiter limiter = null;
            // 判断map集合中是否有创建有创建好的令牌桶
            if (!rateLimiterMap.containsKey(url)) {
                // 创建令牌桶
                limiter = RateLimiter.create(rateLimit.permitsPerSecond());
                rateLimiterMap.put(url, limiter);
                logger.info("<<=================  请求{},创建令牌桶,容量{} 成功!!!", url, rateLimit.permitsPerSecond());
            }
            limiter = rateLimiterMap.get(url);
            // 获取令牌
            boolean acquire = limiter.tryAcquire(rateLimit.timeout(), rateLimit.timeunit());

            if (!acquire) {
                responseResult(response, 500, rateLimit.message());
                return null;
            }
        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * 获取注解对象
     *
     * @param joinPoint 对象
     * @return ten LogAnnotation
     */
    private RateLimit getAnRateLimiter(final JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String name = joinPoint.getSignature().getName();
        if (!StringUtils.isEmpty(name)) {
            for (Method method : methods) {
                RateLimit annotation = method.getAnnotation(RateLimit.class);
                if (!Objects.isNull(annotation) && name.equals(method.getName())) {
                    return annotation;
                }
            }
        }
        return null;
    }

    /**
     * 自定义响应结果
     *
     * @param response 响应
     * @param code     响应码
     * @param message  响应信息
     */
    private void responseResult(HttpServletResponse response, Integer code, String message) {
        response.resetBuffer();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.println("{\"code\":" + code + " ,\"message\" :\"" + message + "\"}");
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(" 输入响应出错 e = {}", e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

}
