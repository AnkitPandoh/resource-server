# README #

Resource Server Using OKTA as OAuth Authorization Server.

### What is this repository for? ###

This respository contains a resource server application which protects resources (Microservices) using OAuth i.e. resources can be accessed if 
valid access token sent in the Authorization header as Bearer token.
<p>Since OKTA provides JWT as access token, this repository uses OKTA jwt verifier to validate the access token and provides access to 
protected resources.

### How do I get set up? ###

Configuration changes can be done in application.yml to run this application
Use postman to access the end points in MicroserviceController class and do provide Authorization Bearer<JWT token> 
in the request header

### Key Notes about Resource Server ###
 Resource : http://projects.spring.io/spring-security-oauth/docs/oauth2.html <br/>
 1. A Resource Server (can be the same as the Authorization Server or a separate application) serves resources that are protected by the OAuth2 token. <br/>
 2. A resource server can be switched on by using annotation @EnableResourceServer	On @Configuration class and configure it by implementing ResourceConfigurer.
 3. Next thing is to configure the tokenServices which can be done by the bean that define the token services and that should be instance of ResourceServerTokenServices.
 4. The main idea to implement ResourceServerTokenServices is to override its loadAuthentication method which will return OAuth2Authentication object after validating the access token successfully. This is important when we have separated  resource server from the authorization server. If both were in the same place then DefaultTokenServices would have been sufficient.
 5. Next important thing is that - The @EnableResourceServer annotation adds a filter of type OAuth2AuthenticationProcessingFilter automatically to the Spring Security filter chain.
 6. It's a pre-authentication filter for OAuth2 protected resources. Extracts an OAuth2 token from the incoming request and uses it to populate the Spring Security context with an {@link OAuth2Authentication} (if used in conjunction with an
 	{@link OAuth2AuthenticationManager}).
 7. To execute step 6, it needs and Oauth2AuthenticationManager, which gets registered when spring include this filter. And This authentication manager uses ResourceServerTokenServices loadAuthentication method to get the Oauth2Authentication object and sets the Spring security context thereby concluding that user is authenticated and can use oauth protected apis
