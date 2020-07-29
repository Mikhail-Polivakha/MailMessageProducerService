package com.example.Application.controllers;

import com.example.Application.models.UserDTO;
import com.example.Application.services.MailProducerResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MailController {

    private MailProducerResolverService mailProducerResolverService;

    @Autowired
    public MailController(MailProducerResolverService mailProducerResolverService) {
        this.mailProducerResolverService = mailProducerResolverService;
    }

    @PostMapping("email")
    public void sendEmail(@RequestBody UserDTO userDTO) {
        mailProducerResolverService.sendEmailToUser(userDTO);
    }
}
