package com.cos.security1.config.oauth.provider;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;
    private Map<String, Object> kakao_acccount;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        kakao_acccount = (Map<String, Object>) attributes.get("kakao_account");
        log.info("attribute: {}", attributes);
        log.info("kakao_account: {}", kakao_acccount);
    }

    @Override
    public String getUsername() {
        return "kakao_" + attributes.get("id");
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) kakao_acccount.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> profile = (Map<String, Object>) kakao_acccount.get("profile");
        return (String) profile.get("nickname");
    }
}
