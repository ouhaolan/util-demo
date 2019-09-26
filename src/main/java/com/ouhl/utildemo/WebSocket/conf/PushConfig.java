package com.ouhl.utildemo.WebSocket.conf;

import com.ouhl.utildemo.WebSocket.WebSocketConnect;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 推送设置
 */
@Component
@EnableScheduling
public class PushConfig {

    /**
     * 任务调度：每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void test(){
        CopyOnWriteArraySet<WebSocketConnect> webSocketSet =
                WebSocketConnect.getWebSocketSet();

        webSocketSet.forEach(c->{
            try {
                c.sendMessage("  定时发送  " + new Date().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
