package com.ouhl.utildemo.ReteLimit;

import com.ouhl.utildemo.ReteLimit.utils.RateLimit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("reteLimit")
public class ReteLimitController {

    /**
     * RateLimit 注解为自定义注解
     * 作用：限制用户的频繁请求，在规定的时间内，只允许访问定义的次数
     * 解释：
     * 1.permitsPerSecond 每秒钟产生的令牌（用户根据令牌判断是否进入）
     * 2.timeout   排队等待令牌时间（超过则请求失败）
     * 3.timeunit  输入的 timeout 时间类型年、月、日、时、分、秒
     * 4.message   排队等待令牌的时间超过timeout 所需要提示的信息
     *
     * @param session
     * @return
     */
    @RequestMapping("testeteLimit")
    @RateLimit(permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS, message = "亲,现在流量过大,请稍后再试.")
    public String testeteLimit(HttpSession session) {
        return session.getId();
    }
}
