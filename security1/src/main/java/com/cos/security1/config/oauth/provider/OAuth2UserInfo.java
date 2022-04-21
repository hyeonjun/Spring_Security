package com.cos.security1.config.oauth.provider;

public interface OAuth2UserInfo {
    String getUsername();
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
