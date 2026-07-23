package jp.wits.dev.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import jp.wits.dev.config.SecurityConfig;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class AdminControllerSecurityTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void test_001_unAuthentication() throws Exception {
		mockMvc.perform(get("/admin"))
			.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void test_002_notAuthorization() throws Exception {
		mockMvc.perform(
				get("/admin")
				.with(
					user("user@example.com")
					.roles("USER")
				))
			.andExpect(status().isForbidden());
	}
	
	@Test
	public void test_003_adminOk() throws Exception {
		mockMvc.perform(
				get("/admin")
				.with(
					user("admin@example.com")
					.roles("ADMIN")
				))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/index"));
	}
}
