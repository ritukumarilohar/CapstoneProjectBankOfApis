package com.bank_api.userService.dto;

public class AuthResponse {
    private String token;
    private Long userId;

    public AuthResponse() {}
	public AuthResponse(String token, Long userid) {
		this.token = token;
		this.userId=userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public Long getUserId() {
        return userId;
    }
}
