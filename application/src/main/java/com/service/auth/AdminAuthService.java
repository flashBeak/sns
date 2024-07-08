package com.service.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface AdminAuthService {
	public boolean isAdmin(HttpServletRequest request) throws Exception;
}
