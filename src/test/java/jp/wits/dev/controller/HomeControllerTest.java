package jp.wits.dev.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import jp.wits.dev.security.LoginUserDetails;

public class HomeControllerTest {
	
	@Test
	public void index_test_001() throws Exception {
		// Prepare
		HomeController controller = new HomeController();
		LoginUserDetails loginUser = mock(LoginUserDetails.class);
		Model model = mock(Model.class);
		
		// Test
		String actual = controller.index(loginUser, model);
		
		// Verify
		assertThat(actual).isEqualTo("index");
		verify(model).addAttribute("loginUser", loginUser);
	}
}
