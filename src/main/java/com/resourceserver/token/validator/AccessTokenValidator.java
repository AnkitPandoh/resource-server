package com.resourceserver.token.validator;

public interface AccessTokenValidator {
	AccessTokenValidationResult validate(String accessToken);
}
