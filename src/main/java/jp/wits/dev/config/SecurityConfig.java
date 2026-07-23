package jp.wits.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize -> authorize
				// permit auth
				.requestMatchers(
					"/login",
					"/signup",
					"/css/**",
					"/js/**",
					"/images/**")
				.permitAll()
				// admin auth
				.requestMatchers("/admin/**")
				.hasRole("ADMIN")
				// any auth
				.anyRequest()
				.authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login?error")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.permitAll()
			);
		
		return http.build();
	}
}
