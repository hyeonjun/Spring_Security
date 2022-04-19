package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// 기본 CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IoC가 된다. -> JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer> {
    // findBy규칙 -> Username 문법
    // select * from user where username = ?
    public User findByUsername(String username);

}
