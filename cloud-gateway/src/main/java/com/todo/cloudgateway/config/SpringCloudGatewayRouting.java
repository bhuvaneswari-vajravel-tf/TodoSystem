package com.todo.cloudgateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.todo.cloudgateway.filter.AuthenticationFilter;
import com.todo.cloudgateway.filter.AuthenticationFilter.Config;


@Configuration
public class SpringCloudGatewayRouting {

	@Bean
	public RouteLocator configureRoute(RouteLocatorBuilder builder, AuthenticationFilter authenticationFilter) {
		GatewayFilter gatewayFilter = authenticationFilter.apply(new Config());

		return builder.routes()
				.route("todo-service",
						r -> r.path("/todolist/api/**").filters(f -> f.filter(gatewayFilter)).uri("lb://TODO-SERVICE"))
				.route("task-system",
						r -> r.path("/tasklist/**").filters(f -> f.filter(gatewayFilter)).uri("lb://TASK-SERVICE"))
				.route("identity-system", r -> r.path("/api/auth/**").uri("lb://IDENTITY-SERVICE"))
				.build();
	}
	 @Bean
	    public CorsWebFilter corsFilter() {
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowCredentials(true);
	        config.addAllowedOrigin("http://localhost:4200");
	        config.addAllowedHeader("*");
	        config.addAllowedMethod("*");
	        source.registerCorsConfiguration("/**", config);
	        return new CorsWebFilter(source);
	    }

}
