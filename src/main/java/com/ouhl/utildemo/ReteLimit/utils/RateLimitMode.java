package com.ouhl.utildemo.ReteLimit.utils;

/**
 * 自定义RateLimit注解限流的限流类型
 */
public class RateLimitMode {

    /**
     * 平滑突发限流（SmoothBursty）和平滑预热限流（SmoothWarmingUp）
     * 平滑突发限流，允许突发流量，后面请求速率控制会趋于平稳
     * 平滑预热限流，开始先于小于最大速率执行，慢慢趋于平稳，接近最大速率
     *
     * @return
     */
    public static final String SMOOTH_BURSTY = "smooth-bursty";
    public static final String SMOOTH_WARMING_UP = "smooth-warming-up";
}
