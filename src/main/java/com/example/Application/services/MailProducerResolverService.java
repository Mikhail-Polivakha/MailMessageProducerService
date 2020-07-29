package com.example.Application.services;

import com.example.Application.configuration.JMSProviderConfiguration;
import com.example.Application.models.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailProducerResolverService {

    private RabbitTemplate rabbitTemplate;

    private JMSProviderConfiguration jmsProviderConfiguration;

    @Autowired
    public MailProducerResolverService(RabbitTemplate rabbitTemplate, JMSProviderConfiguration jmsProviderConfiguration) {
        this.rabbitTemplate = rabbitTemplate;
        this.jmsProviderConfiguration = jmsProviderConfiguration;
    }
    public boolean sendEmailToUser(UserDTO userDTO) {
        try {
            rabbitTemplate.convertAndSend(jmsProviderConfiguration.getExchangeTopicName(),
                    jmsProviderConfiguration.getKey(), userDTO);
            log.info("Message about sending the Email to user with id : {} was successfully stored in queue with name : {}",
                    userDTO.getId(), jmsProviderConfiguration.getQueueName());
            return true;
        } catch (AmqpException e) {
            log.warn("Message about sending the Email to user with id : {} failed to store in queue with name : {}, " +
                    "exchange topic name : {}", userDTO.getId(), jmsProviderConfiguration.getQueueName(),
                    jmsProviderConfiguration.getExchangeTopicName());
            return false;
        }
    }
}
