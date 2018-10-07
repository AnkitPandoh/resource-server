package com.resourceserver.controller;

import java.io.IOException;

import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.oauth2.sdk.ParseException;
import com.okta.jwt.JoseException;

/**
 * Use this class to expose those microservices which should be oauth2
 * protected. An oauth2 access (JWT) token is needed to access these microservices
 * unless it is ignored in the configure method of {@link ResourceServerConfiguration} class. 
 * @author Ankit Pandoh
 *
 */
@RestController
public class MicroserviceController {
	
	@RequestMapping("/api/getUser")
    public String getUser() throws ParseException, IOException, JoseException {
        return "Hello User";
    }
}
