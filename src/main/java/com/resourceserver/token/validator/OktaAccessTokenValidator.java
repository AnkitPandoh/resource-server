package com.resourceserver.token.validator;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.resourceserver.properties.JwtProperties;
import com.nimbusds.oauth2.sdk.ParseException;
import com.okta.jwt.JoseException;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtHelper;
import com.okta.jwt.JwtVerifier;

/**
 * <p>
 * This class validates and decodes the access token or JWT token(OKTA returns
 * JWT token as access token). It uses <code>JwtVerifier</code> written by
 * <tt>OKTA</tt> to verify and decodes the access token and throws Exception if
 * token is not valid. This will validate JWT for the following:
 * <ul>
 * <li>Token expiration date</li>
 * <li>Valid token not before date</li>
 * <li>The token issuer matches the expected value passed into the
 * <code>JwtHelper</code></li>
 * <li>The token audience matches the expected value passed into the above
 * helper</li>
 * </ul>
 * Once the token is verified, it will decode it and returns a
 * {@link AccessTokenValidationResult} object which has all the necessary
 * information such as claims in jwt token and its validity.
 * </p>
 * 
 * @author Ankit Pandoh
 *
 */
public class OktaAccessTokenValidator implements AccessTokenValidator {

	private final static Log logger = LogFactory.getLog(OktaAccessTokenValidator.class);

	@Autowired
	JwtProperties properties;

	@Override
	public AccessTokenValidationResult validate(String accessToken) {
		boolean debug = logger.isDebugEnabled();
		if (debug) {
			logger.debug("Executing OktaAccessTokenValidator#validate()..");
		}
		AccessTokenValidationResult result = null;
		Jwt jwt = null;
		try {
			JwtVerifier jwtVerifier = new JwtHelper().setIssuerUrl(properties.getIssuerUrl())
					.setAudience(properties.getAud()).setClientId(properties.getClientId()).build();
			jwt = jwtVerifier.decodeAccessToken(accessToken);
			
		} catch (ParseException | IOException | JoseException ex) {
			logger.error("Token is not valid ", ex);
		}
		if (jwt != null && jwt.getClaims() != null) {
			if (debug) {
				logger.debug("Token is Valid");
			}
			result = new AccessTokenValidationResult(true, jwt.getClaims());
		} else {
			logger.error("Token is not valid, claims are null.. ");
			result = new AccessTokenValidationResult(false, null);
		}
		return result;
	}
}
