package jp.wits.dev.security;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.wits.dev.model.entity.Role;
import jp.wits.dev.model.entity.User;

public class LoginUserDetailsTest {
	
	@Test
	public void from_test_001() throws Exception {
		// Prepare
		Role userRole = new Role();
		userRole.setId(1L);
		userRole.setRoleCode("USER");
		userRole.setRoleName("一般ユーザー");
		
		Role adminRole = new Role();
		adminRole.setId(2L);
		adminRole.setRoleCode("ADMIN");
		adminRole.setRoleName("管理者");
		
		User user = new User();
		user.setId(10L);
		user.setEmail("admin@example.com");
		user.setPasswordHash("{bcrypt}password-hash");
		user.setDisplayName("管理者ユーザー");
		user.setEnabled(true);
		user.setAccountLocked(false);
		user.setRoles(List.of(userRole, adminRole));
		
		// Test
		LoginUserDetails actual = LoginUserDetails.from(user);
		
		// Verify
		assertThat(actual.getUserId()).isEqualTo(10L);
		assertThat(actual.getUsername()).isEqualTo("admin@example.com");
		assertThat(actual.getPassword()).isEqualTo("{bcrypt}password-hash");
		assertThat(actual.getDisplayName()).isEqualTo("管理者ユーザー");
		assertThat(actual.isEnabled()).isTrue();
		assertThat(actual.isAccountNonLocked()).isTrue();
		assertThat(actual.isAccountNonExpired()).isTrue();
		assertThat(actual.isCredentialsNonExpired()).isTrue();
		assertThat(actual.getAuthorities())
			.extracting(authorities -> authorities.getAuthority())
			.containsExactly("ROLE_USER", "ROLE_ADMIN");
	}
	
	@Test
	public void from_test_002() throws Exception {
		// Prepare
		User user = new User();
		user.setId(10L);
		user.setEmail("locked@example.com");
		user.setPasswordHash("{bcrypt}password-hash");
		user.setDisplayName("ロックユーザー");
		user.setEnabled(true);
		user.setAccountLocked(true);
		user.setRoles(Collections.emptyList());
		
		// Test
		LoginUserDetails actual = LoginUserDetails.from(user);
		
		// Verify
		assertThat(actual.isAccountNonLocked()).isFalse();
	}
}
