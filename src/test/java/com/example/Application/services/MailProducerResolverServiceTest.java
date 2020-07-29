package com.example.Application.services;

import com.example.Application.configuration.JMSProviderConfiguration;
import com.example.Application.models.ActionToPerform;
import com.example.Application.models.UserDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MailProducerResolverServiceTest {

    @InjectMocks
    private MailProducerResolverService mailProducerResolverService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private JMSProviderConfiguration jmsProviderConfiguration;

    private String queueName;
    private String exchangeTopicName;
    private String key;

    @BeforeAll
    void generalInitialization() {
        queueName = "mailQueue";
        exchangeTopicName = "message_queue_exchange";
        key = "secret";
    }

    @BeforeEach
    void beforeAllInitialization() {
        rabbitTemplate = Mockito.mock(RabbitTemplate.class);
        jmsProviderConfiguration = Mockito.mock(JMSProviderConfiguration.class);
    }

    @Test
    @DisplayName("Testing sending message to the queue")
    @Disabled
    void sendEmailToUser() {
        UserDTO userDTO = new UserDTO(1L, ActionToPerform.WARN_ABOUT_FINISHING_EVALUATION_SCOPE);

        Mockito.when(jmsProviderConfiguration.getQueueName()).thenReturn(queueName);
        Mockito.when(jmsProviderConfiguration.getExchangeTopicName()).thenReturn(exchangeTopicName);
        Mockito.when(jmsProviderConfiguration.getKey()).thenReturn(key);

        Mockito.doNothing().when(rabbitTemplate).convertAndSend(jmsProviderConfiguration.getExchangeTopicName(),
                jmsProviderConfiguration.getKey(), userDTO);

        Assertions.assertDoesNotThrow(() -> mailProducerResolverService.sendEmailToUser(userDTO));
        Assertions.assertTrue(mailProducerResolverService.sendEmailToUser(userDTO));
    }
}