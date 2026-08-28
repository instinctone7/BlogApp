package com.InstinctOne.BlogApp.repositories;

import com.InstinctOne.BlogApp.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<UserToken,Long> {
    UserToken findByToken(String token);
}
