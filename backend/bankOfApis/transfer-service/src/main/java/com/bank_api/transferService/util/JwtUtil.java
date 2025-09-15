//package com.bank_api.transferService.util;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtUtil {
//    
//    @Value("${jwt.secret}")
//    private String secret;
//    
//    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
//    
//    public Long extractUserId(String token) {
//        // TEMPORARY BYPASS - REMOVE LATER
//        logger.warn("⚠️ JWT extraction bypassed - returning user ID 1");
//        return 1L;
//    }
//
//    public boolean validateToken(String token) {
//        // TEMPORARY BYPASS - REMOVE LATER
//        logger.warn("⚠️ JWT validation bypassed - returning true");
//        return true;
//    }
//}