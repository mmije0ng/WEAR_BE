package com.backend.wear.aspects;

import com.backend.wear.config.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.SignatureException;

@Slf4j
@Aspect
@Component
public class AuthorizationAspect {
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthorizationAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Pointcut("execution(* com.backend.wear.controller..*(..))")
    public void aspectJWT() {}

    // jwt 토큰 userId 검증 aop
    @Around(value = "aspectJWT() && args(authorizationHeader,userId,  ..)", argNames = "joinPoint,authorizationHeader,userId")
    public Object validateUserId(ProceedingJoinPoint joinPoint, String authorizationHeader, Long userId) throws Throwable {
        try {
            jwtUtil.validateUserIdWithHeader(authorizationHeader, userId);
            log.info("jwt aop 검증 테스트 통과");
            return joinPoint.proceed();
        } catch (SignatureException e) {
            log.error("AOP JWT SignatureException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}