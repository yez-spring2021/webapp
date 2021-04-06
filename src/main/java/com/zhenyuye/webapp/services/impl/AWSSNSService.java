package com.zhenyuye.webapp.services.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AWSSNSService {
    @Autowired
    private AmazonSNS amazonSNSClient;
    @Value("${amazon.sns.topic.arn}")
    private String topicArn;

    public String publishMsgToTopic(String message) {
        PublishRequest publishRequest = new PublishRequest(topicArn, message);
        PublishResult publishResult = amazonSNSClient.publish(publishRequest);
        log.info("Publish message:{} to SNS, messageId: {}", message, publishResult.getMessageId());
        return publishResult.getMessageId();
    }
}
