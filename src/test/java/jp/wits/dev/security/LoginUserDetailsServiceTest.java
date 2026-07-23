package jp.wits.dev.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jp.wits.dev.mapper.UserMapper;
import jp.wits.dev.model.entity.User;

@ExtendWith(MockitoExtension.class)
public class LoginUserDetailsServiceTest {
	
	@Mock
	UserMapper userMapper;
	
	@InjectMocks
	LoginUserDetailsService loginUserDetailsService;
	
	@Test
	public void loadUserByUsername_test_001() throws Exception {
		// Prepare
		final String username = "admin@example.com";
		User user = new User();
		user.setId(1L);
		user.setEmail(username);
		user.setPasswordHash("{bcrypt}password-hash");
		user.setDisplayName("管理者ユーザー");
		user.setEnabled(true);
		user.setAccountLocked(false);
		user.setRoles(List.of());
		
		when(userMapper.findByEmail(username)).thenReturn(Optional.of(user));
		
		// Test
		UserDetails actual = loginUserDetailsService.loadUserByUsername(username);
		
		// Verify
		assertThat(actual.getUsername()).isEqualTo(username);
		assertThat(actual.getPassword()).isEqualTo("{bcrypt}password-hash");
		assertThat(actual.isEnabled()).isTrue();
		assertThat(actual.isAccountNonLocked()).isTrue();
		verify(userMapper).findByEmail(username);
	}
	
	@Test
	public void loadUserByUsername_test_002() throws Exception {
		// Prepare
		final String username = "admin@example.com";
		User user = new User();
		user.setId(1L);
		user.setEmail(username);
		user.setPasswordHash("{bcrypt}password-hash");
		user.setDisplayName("管理者ユーザー");
		user.setEnabled(true);
		user.setAccountLocked(false);
		user.setRoles(List.of());
		
		when(userMapper.findByEmail(username)).thenReturn(Optional.of(user));
		
		// Test
		UserDetails actual = loginUserDetailsService.loadUserByUsername("  ADMIN@EXAMPLE.COM  ");
		
		// Verify
		assertThat(actual.getUsername()).isEqualTo(username);
		verify(userMapper).findByEmail(username);
	}
	
	@Test
	public void loadUserByUsername_test_003() throws Exception {
		// Prepare
		when(userMapper.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
		
		// Test
		assertThatThrownBy(() -> 
		loginUserDetailsService.loadUserByUsername("unknown@example.com"))
			.isInstanceOf(UsernameNotFoundException.class)
			.hasMessage("ユーザーが見つかりません。");
	}
}
