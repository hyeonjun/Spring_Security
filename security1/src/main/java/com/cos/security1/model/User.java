package com.cos.security1.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity // User클래스가 데이터베이스에 테이블이 생성됨
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN

    private String provider; // oauth 플랫폼
    private String providerId; // 해당 oauth에서 받은 데이터 중 id를 저장

    @CreationTimestamp
    private Timestamp createDate;

    @Builder // 빌더 패턴: ORM -> Java(다른 언어) Obejct -> 테이블로 매핑해주는 기술
    public User(String username, String password, String email, String provider, String providerId, Timestamp createDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }
}
