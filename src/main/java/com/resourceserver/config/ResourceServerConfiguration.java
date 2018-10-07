package com.resourceserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.resourceserver.token.services.OktaTokenServices;
import com.resourceserver.token.validator.AccessTokenValidator;
import com.resourceserver.token.validator.OktaAccessTokenValidator;

/**
 * <p>
 * Use this Configuration class to configure the path which should be oauth
 * protected i.e protected by oauth2 access(JWT) token. Also configuring to make
 * microservices completely stateless. Configuration also involves registering a
 * bean of type {@link ResourceServerTokenServices} which will validate the
 * token using {@link AccessTokenValidator} and provide
 * <code>OAuth2Authentication</code> object using instance of type
 * {@link AccessTokenConverter} that will be set into Spring Security Context.
 * Also, end points can be 2 legged and 3 legged protected which can be
 * configured using spring expression in the configure method.
 * </p>
 * 
 * @author Ankit Pandoh
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration implements ResourceServerConfigurer {

	@Value("${serverResourceId}")
	private static String SERVER_RESOURCE_ID;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(SERVER_RESOURCE_ID);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/health").permitAll().antMatchers("/*").access("#oauth2.isUser()")
				.antMatchers("/admin/**").access("#oauth2.isClient()").anyRequest().authenticated().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	public ResourceServerTokenServices getTokenServices(AccessTokenValidator tokenValidator,
			AccessTokenConverter tokenConverter) {
		ResourceServerTokenServices oktaTokenServices = new OktaTokenServices(tokenValidator, tokenConverter);
		return oktaTokenServices;
	}

	@Bean
	public AccessTokenValidator getAccessTokenValidator() {
		return new OktaAccessTokenValidator();
	}

	@Bean
	public AccessTokenConverter getAccessTokenConverter() {
		return new DefaultAccessTokenConverter();
	}
}
