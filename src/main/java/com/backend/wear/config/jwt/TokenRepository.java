package com.backend.wear.config.jwt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    Optional <Token> findByIdAndRefreshToken(Long id, String refreshToken);
}