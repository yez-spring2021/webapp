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
        System.setProperty("javax.net.ssl.trustStore", System.getenv("trustStore"));
        System.setProperty("javax.net.ssl.trustStorePassword", System.getenv("trustStorePassword"));
        System.out.println(System.getProperty("javax.net.ssl.trustStore"));
        System.out.println(System.getProperty("javax.net.ssl.trustStorePassword"));
        SpringApplication.run(WebappApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebappApplication.class);
    }
}
