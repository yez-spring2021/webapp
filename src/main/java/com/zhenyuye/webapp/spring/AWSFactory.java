package com.zhenyuye.webapp.spring;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AWSFactory {
    @Value("${amazon.realm}")
    private String realm;

    @Bean
    public AmazonS3 getAmazonS3() {
        AWSCredentialsProvider provider;
        if (realm.equals("desktop")) {
            provider = new EnvironmentVariableCredentialsProvider();
        } else {
            provider = InstanceProfileCredentialsProvider.getInstance();
        }
        return AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(provider).build();
    }

    @Bean
    public AmazonSNS getAmazonSNS() {
        return AmazonSNSClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }

}
