package com.todo.cloudgateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.todo.cloudgateway.utill.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private JwtUtil jwtUtil;

	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				throw new RuntimeException("missing authorization header");
			}

			String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				authHeader = authHeader.substring(7);
			}
			try {
				jwtUtil.validateToken(authHeader);
			} catch (ExpiredJwtException ex) {
				throw new RuntimeException("Token Expired:: " + ex.getMessage());
			} catch (Exception e) {
				System.out.println("=====exception instance====" + e.getClass().getName());
				throw new RuntimeException("UnAuthorized access to application::" + e.getMessage());

			}
			return chain.filter(exchange);
		});
	}

	public static class Config {

	}
}
