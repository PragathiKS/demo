package com.tetrapak.publicweb.core.beans.pxp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class BearerToken.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "access_token", "token_type", "expires_in" })
public class BearerToken {

    /** The access token. */
    @JsonProperty("access_token")
    private String accessToken;

    /** The token type. */
    @JsonProperty("token_type")
    private String tokenType;

    /** The expires in. */
    @JsonProperty("expires_in")
    private String expiresIn;
    
    /** The scope. */
    @JsonProperty("scope")
    private String scope;

    /**
     * Gets the access token.
     *
     * @return the access token
     */
    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets the access token.
     *
     * @param accessToken
     *            the new access token
     */
    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Gets the token type.
     *
     * @return the token type
     */
    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Sets the token type.
     *
     * @param tokenType
     *            the new token type
     */
    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Gets the expires in.
     *
     * @return the expires in
     */
    @JsonProperty("expires_in")
    public String getExpiresIn() {
        return expiresIn;
    }

    /**
     * Sets the expires in.
     *
     * @param expiresIn
     *            the new expires in
     */
    @JsonProperty("expires_in")
    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Gets the scope.
     *
     * @return the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the scope.
     *
     * @param scope the new scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

}
