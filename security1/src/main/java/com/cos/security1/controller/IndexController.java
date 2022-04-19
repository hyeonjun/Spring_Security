package com.cos.security1.controller;

import com.cos.security1.model.User;
import com.cos.security1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping({"", "/"}) // localhost:8080, localhost:8080/
    public String index() {
        // 머스테치 기본폴더: src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) -> 생략 가능
        return "index"; // src/main/resources/templates/index.mustache 를 찾게 됨
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
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
