package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;

    // Oauth 로그인 후
    // userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest Client Registration: {}", userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        log.info("userRequest AccessToken: {}", userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 반환(OAtuh Client 라이브러리가 받음) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필을 받아줌
        log.info("getAttributes: {}", oAuth2User.getAttributes());

        // 강제로 자동 회원가입을 진행
        String provider = userRequest.getClientRegistration().getClientName(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId;
        User user = userService.findUserByUsername(username);
        if(user == null) {
            user = User.builder()
                    .username(username)
                    .password("oauthLogin") // OAuth 로그인이기 때문에 패스워드가 필요없으나 DB에 저장하기 위해 값 지정
                    .email(oAuth2User.getAttribute("email"))
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userService.saveUser(user);
        }


        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
