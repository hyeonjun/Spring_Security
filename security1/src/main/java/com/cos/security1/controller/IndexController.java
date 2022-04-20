package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴
@Slf4j
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication, // DI(의존성 주입)
            @AuthenticationPrincipal PrincipalDetails userDetails) { // @AuthenticationPrincipal 어노테이션을 통해 getUser를 가져올 수 있음
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); // 다운캐스팅하여 getUser를 가져올 수 있음

        // 아래 결과가 같음
        log.info("authentication getUser: {}", principalDetails.getUser());
        log.info("userDetails getUsername: {}", userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("authentication getUser: {}", oAuth2User.getAttributes());
        log.info("oauth2User: {}", oauth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping({"", "/"}) // localhost:8080, localhost:8080/
    public String index() {
        // 머스테치 기본폴더: src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) -> 생략 가능
        return "index"; // src/main/resources/templates/index.mustache 를 찾게 됨
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails: {}", principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 스프링시큐리티에서 해당 주소를 인터셉트함 <- SecurityConfig 파일 생성하면 인터셉트를 하지 않게 됨
   @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        userService.saveUser(user); // 회원가입
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정 메서드에 간단하게 권한을 부여함 -> ROLE_ADMIN 권한을 가진 사람만 접근 가능
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    // @PreAuthorize => data라는 메서드가 실행되기 직전에 실행됨
    // 하나를 걸고 싶을 때는 Secured, 여러 개를 걸고 싶을 때는 PreAuthorize
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }

}
