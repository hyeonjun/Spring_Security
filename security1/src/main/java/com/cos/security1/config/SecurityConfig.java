package com.cos.security1.config;

// OAuth2.0
// 1. 코드받기(인증)
// 2. 액세스토큰(사용자 정보에 접근할 수 있는 권한이 생김)
// 3. 사용자 프로필 정보를 가져옴
// 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
// 4-2. 사용자 프로필(이메일, 전화번호, 이름, 아이디)
//  쇼핑몰 같은 경우 -> 집 주소 등이 더 필요함 => 그래서 추가적인 회원가입 창이 나와서 회원가입을 해야함

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록됨.
// securedEnabled=true: secure 어노테이션 활성화
// prePostEnabled=true: preAuthorize, PostAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        * csrf 토큰 활성화 시
        * 쿠키를 생성할 때 HttpOnly 태그를 사용하면 클라이언트 스트립트가 보호된 쿠키에 액세스하는 위험을 줄일 수 있으므로 쿠키 보안을 강화할 수 있음
        * http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        * */
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증 필요
                .antMatchers("/manager/**", "/info").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 권한 필요
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 다른 요청은 권한 상관 없음
                .and()
                .formLogin() // 로그인 폼
                .loginPage("/loginForm") // 해당 주소로 로그인 페이지 호출함
                .loginProcessingUrl("/login") // /login 주소 호출 시 시큐리티가 인터셉트하여 대신 로그인을 진행 -> loadUserByName
                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 주소
                .and()
                .logout()
                .logoutSuccessUrl("/loginForm") // 로그아웃 성공 시 리턴 URL
                .invalidateHttpSession(true) // 인증 정보를 지우고 세션을 무효화
                .deleteCookies("JSESSIONID", "remember-me") // JSEESION, remember-me 쿠키 삭제
                .permitAll()
                .and()
                .sessionManagement()
                .maximumSessions(-1) // 세션 최대 허용 수, -1인 경우 무제한 세션 허용
                .maxSessionsPreventsLogin(false) // true면 중복 로그인을 막고, false면 이전 로그인의 세션을 해제
                .expiredUrl("/loginForm")
                .and()
                .and().rememberMe() // 로그인 유지
                .alwaysRemember(false) // 항상 기억? x
                .tokenValiditySeconds(43200) // second, 12시간 유지
                .rememberMeParameter("remember-me")
                .and()
                .oauth2Login() // Oauth 로그인이 완료된 후 뒤처리가 필요
                .loginPage("/loginForm") // 코드X, (액세스토큰+사용자정보 O)
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}
