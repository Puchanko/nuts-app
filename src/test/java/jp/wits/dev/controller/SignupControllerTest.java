package jp.wits.dev.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import jp.wits.dev.service.UserRegistrationService;
import jp.wits.dev.service.command.UserRegistrationCommand;

@WebMvcTest(SignupController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SignupControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private UserRegistrationService userRegistrationService;
	
	@Test
	public void showSignupForm_test() throws Exception {
		// Test
		mockMvc.perform(get("/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"))
			.andExpect(model().attributeExists("signupForm"));
	}
	
	@Test
	public void signup_test_001_ok() throws Exception {
		// Prepare
		when(userRegistrationService.register(any(UserRegistrationCommand.class))).thenReturn(100L);
		ArgumentCaptor<UserRegistrationCommand> commandCaptor = ArgumentCaptor.forClass(UserRegistrationCommand.class);
		
		// Test
		mockMvc.perform(post("/signup")
				.param("email", "USER@example.com")
				.param("displayName", "テストユーザー")
				.param("password", "Password123!")
				.param("passwordConfirmation", "Password123!"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"))
			.andExpect(flash().attribute("signupCompleted", true));
		
		// Verify
		verify(userRegistrationService).register(commandCaptor.capture());
		UserRegistrationCommand command =commandCaptor.getValue();
		assertThat(command.email()).isEqualTo("USER@example.com");
		assertThat(command.displayName()).isEqualTo("テストユーザー");
		assertThat(command.rawPassword()).isEqualTo("Password123!");
	}
	
	@Test
	public void signup_test_002_passwordUnmatch() throws Exception {
		// Prepare
		when(userRegistrationService.register(any(UserRegistrationCommand.class))).thenReturn(100L);
		
		// Test
		mockMvc.perform(post("/signup")
				.param("email", "USER@example.com")
				.param("displayName", "テストユーザー")
				.param("password", "Password123!")
				.param("passwordConfirmation", "DifferentPassword123!"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"));
		
		// Verify
		verify(userRegistrationService, never()).register(any());
	}
}
