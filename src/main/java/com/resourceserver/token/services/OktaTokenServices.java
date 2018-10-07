package com.resourceserver.token.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.resourceserver.token.validator.AccessTokenValidationResult;
import com.resourceserver.token.validator.AccessTokenValidator;
import com.resourceserver.token.validator.OktaAccessTokenValidator;

import net.minidev.json.JSONArray;

/**
 * <p>
 * Does the job of validating the token using {@link OktaAccessTokenValidator}
 * and providing the <code>OAuth2Authentication</code> Object.
 * </p>
 * 
 * @author Ankit Pandoh
 *
 */
public class OktaTokenServices implements ResourceServerTokenServices {

	private final static Log logger = LogFactory.getLog(OktaTokenServices.class);
	private final static String UID = "uid";
	private AccessTokenValidator tokenValidator;
	private AccessTokenConverter tokenConverter;
	private AccessTokenValidationResult validationResult;

	public OktaTokenServices(AccessTokenValidator tokenValidator, AccessTokenConverter tokenConverter) {
		this.tokenValidator = tokenValidator;
		this.tokenConverter = tokenConverter;
	}

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		validationResult = tokenValidator.validate(accessToken);
		if (!validationResult.isValid()) {
			throw new InvalidTokenException("Token is not valid.");
		}
		return extractAuthentication(validationResult.getTokenClaims());
	}

	private OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
		boolean debug = logger.isDebugEnabled();
		OAuth2Authentication auth = tokenConverter.extractAuthentication(claims);
		/**
		 * for 3 legged
		 */
		if (claims.containsKey(UID) && claims.get(UID) != null) {
			if (debug) {
				logger.debug("User is authenticated and has uid..");
			}
			Object principal = claims.get(UID);
			Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
			roles.add(new SimpleGrantedAuthority("ROLE_USER"));
			auth = new OAuth2Authentication(auth.getOAuth2Request(),
					new UsernamePasswordAuthenticationToken(principal, "N/A", roles));
		}
		return auth;
	}

	/**
	 * fetch claim from JWT claims Map
	 * 
	 * @param claim
	 */
	public String getClaim(String claim) {
		Object obj = null;
		AccessTokenValidationResult validationResult = getValidationResult();
		if (validationResult != null) {
			Map<String, ?> claims = validationResult.getTokenClaims();
			if (claims != null) {
				obj = claims.get(claim);
				if (obj instanceof JSONArray) {
					JSONArray arr = (JSONArray) obj;
					return arr.toString();
				} else
					return (String) obj;
			}
		}
		return null;
	}
	
	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the validationResult
	 */
	public AccessTokenValidationResult getValidationResult() {
		return validationResult;
	}

	/**
	 * @param validationResult
	 *            the validationResult to set
	 */
	public void setValidationResult(AccessTokenValidationResult validationResult) {
		this.validationResult = validationResult;
	}

}
