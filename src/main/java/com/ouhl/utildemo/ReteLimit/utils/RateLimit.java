package com.ouhl.utildemo.ReteLimit.utils;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 创建自定义注解
 * 作用：自定义注解实现应用限流，规定时间内限制用户点击次数
 */
@Target(ElementType.METHOD) //表示该注解被用于方法上
@Retention(RetentionPolicy.RUNTIME) //表示该注解保留到运行时
@Documented
public @interface RateLimit {

    //以固定数值往令牌桶添加令牌
    double permitsPerSecond () ;

    //获取令牌最大等待时间
    long timeout();

    // 单位(例:分钟/秒/毫秒) 默认:毫秒
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    // 无法获取令牌返回提示信息 默认值可以自行修改
    String message() default "系统繁忙,请稍后再试!";


}
