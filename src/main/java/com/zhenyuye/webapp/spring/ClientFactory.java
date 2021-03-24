package com.zhenyuye.webapp.spring;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientFactory {
    @Value("${amazon.realm}")
    private String realm;
//    @Value("${logging.file.path}")
//    private String loggingPath;
    @Bean
    public StatsDClient getStatsDClient() {
        log.info("Initializing StatsDClient");
//        log.info(loggingPath);
        if(realm.equals("desktop")) {
            log.info("Using NoOpStatsDClient in {}", realm);
            return new NoOpStatsDClient();
        }
        log.info("Using NonBlockingStatsDClient in {}", realm);
        return new NonBlockingStatsDClient("csye6225", "localhost", 8125);
    }
}
