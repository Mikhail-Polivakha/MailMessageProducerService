package com.example.Application.controllers;

import com.example.Application.models.ActionToPerform;
import com.example.Application.models.UserDTO;
import com.example.Application.services.MailProducerResolverService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MailControllerTest {

    @InjectMocks
    private MailController mailController;

    @MockBean
    private MailProducerResolverService mailProducerResolverService;

    @Autowired
    private MockMvc mockMvc;

    private String MAPPING_FOR_POST_SENDING_EMAIL;

    @BeforeAll
    void generalInitialization() {
        MAPPING_FOR_POST_SENDING_EMAIL = "/email";
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    void beforeEachInitialization() {
        mailProducerResolverService = Mockito.mock(MailProducerResolverService.class);
        mailController = new MailController(mailProducerResolverService);
    }

    @Test
    @DisplayName("Testing MVC MailController class")
    void sendEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(MAPPING_FOR_POST_SENDING_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"id\" : 1,\n" +
                        "    \"action\" : \"WARN_ABOUT_FINISHING_EVALUATION_SCOPE\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        UserDTO userDTO = new UserDTO(1L, ActionToPerform.WELCOM_TO_COMPANY);
        Mockito.when(mailProducerResolverService.sendEmailToUser(userDTO)).thenReturn(true);

        Assertions.assertDoesNotThrow(() ->  mailController.sendEmail(userDTO));
        Mockito.verify(mailProducerResolverService, Mockito.times(1)).sendEmailToUser(userDTO);
    }
}