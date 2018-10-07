package com.resourceserver.token.validator;

import java.util.Collections;
import java.util.Map;

public class AccessTokenValidationResult {
	private final boolean valid;
    private final Map<String, ?> tokenClaims;

    public AccessTokenValidationResult(boolean valid, Map<String, ?> tokenClaims) {
        this.valid = valid;
        this.tokenClaims = tokenClaims;
    }

    public boolean isValid() {
        return valid;
    }

    public Map<String, ?> getTokenClaims() {
        return Collections.unmodifiableMap(tokenClaims);
}
}
