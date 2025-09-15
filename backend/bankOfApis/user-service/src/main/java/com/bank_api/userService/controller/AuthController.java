package com.bank_api.userService.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_api.userService.Model.User;
import com.bank_api.userService.config.JwtUtil;
import com.bank_api.userService.dto.AuthResponse;
import com.bank_api.userService.dto.LoginRequest;
import com.bank_api.userService.dto.RegisterRequest;
import com.bank_api.userService.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
    	 userService.register(request);
    	    System.out.println("User registered successfully");
    	    
    	    Map<String, String> response = new HashMap<>();
    	    response.put("message", "User registered successfully");
    	    response.put("status", "success");
    	    
    	    return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        User user = userService.loadUserByEmail(request.getEmail()); 
        String token = jwtUtil.generateToken(user); 
        System.out.println(token);
        return ResponseEntity.ok(new AuthResponse(token, user.getId()));
    }

}
