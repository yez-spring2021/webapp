package com.zhenyuye.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WebappApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.out.println(System.getenv("javax.net.ssl.trustStore"));
        System.out.println(System.getenv("javax.net.ssl.trustStorePassword"));
//        System.setProperty("javax.net.ssl.trustStore", "/home/mike/clientkeystore.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword", "yzy123poi99");
        SpringApplication.run(WebappApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebappApplication.class);
    }
}
