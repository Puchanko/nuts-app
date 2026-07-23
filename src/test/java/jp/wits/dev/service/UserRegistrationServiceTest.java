package jp.wits.dev.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import jp.wits.dev.exception.DuplicateEmailException;
import jp.wits.dev.exception.RoleNotFoundException;
import jp.wits.dev.mapper.RoleMapper;
import jp.wits.dev.mapper.UserMapper;
import jp.wits.dev.mapper.UserRoleMapper;
import jp.wits.dev.model.entity.Role;
import jp.wits.dev.model.entity.User;
import jp.wits.dev.service.command.UserRegistrationCommand;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceTest {
	
	@Mock
	private UserMapper userMapper;
	
	@Mock
	private RoleMapper roleMapper;
	
	@Mock
	private UserRoleMapper userRoleMapper;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserRegistrationService userRegistrationService;
	
	@Test
	public void register_test_001_Ok() throws Exception {
		// Prepare
		UserRegistrationCommand command = new UserRegistrationCommand(
				" USER@Example.COM ",
				" テストユーザー ",
				"password123");
		
		Role role = createUserRole();
		
		when(userMapper.existsByEmail("user@example.com")).thenReturn(false);
		when(roleMapper.findByRoleCode("USER")).thenReturn(Optional.of(role));
		when(passwordEncoder.encode("password123")).thenReturn("{bcrypt}encoded-password");
		when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
			User user = invocation.getArgument(0);
			user.setId(100L);
			return 1;
		});
		when(userRoleMapper.insert(100L, 10L)).thenReturn(1);
		
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		
		// Test
		Long actual = userRegistrationService.register(command);
		
		// Verify
		assertThat(actual).isEqualTo(100L);
		
		verify(userMapper).insert(userCaptor.capture());
		User insertedUser = userCaptor.getValue();
		assertThat(insertedUser.getEmail()).isEqualTo("user@example.com");
		assertThat(insertedUser.getPasswordHash()).isEqualTo("{bcrypt}encoded-password");
		assertThat(insertedUser.getDisplayName()).isEqualTo("テストユーザー");
		assertThat(insertedUser.isEnabled()).isTrue();
		assertThat(insertedUser.isAccountLocked()).isFalse();
		assertThat(insertedUser.getFailedLoginAttempts()).isZero();
		assertThat(insertedUser.getPasswordChangedAt()).isNotNull();
		
		verify(userMapper).existsByEmail("user@example.com");
		verify(roleMapper).findByRoleCode("USER");
		verify(passwordEncoder).encode("password123");
		verify(userRoleMapper).insert(100L, 10L);
	}
	
	@Test
	public void register_test_002_duplicateEmail() throws Exception {
		// Prepare
		UserRegistrationCommand command = new UserRegistrationCommand(
				" USER@Example.COM ",
				" テストユーザー ",
				"password123");
		when(userMapper.existsByEmail("user@example.com")).thenReturn(true);
		
		// Test
		assertThatThrownBy(() -> userRegistrationService.register(command))
			.isInstanceOf(DuplicateEmailException.class);
		
		// Verify
		verify(userMapper).existsByEmail("user@example.com");
		verify(roleMapper, never()).findByRoleCode(any());
		verify(passwordEncoder, never()).encode(any());
		verify(userMapper, never()).insert(any());
		verify(userRoleMapper, never()).insert(any(), any());
	}
	
	@Test
	public void register_test_003_roleNotFound() throws Exception {
		// Prepare
		UserRegistrationCommand command = new UserRegistrationCommand(
				" USER@Example.COM ",
				" テストユーザー ",
				"password123");
		when(userMapper.existsByEmail("user@example.com")).thenReturn(false);
		when(roleMapper.findByRoleCode("USER")).thenReturn(Optional.empty());
		
		// Test
		assertThatThrownBy(() -> userRegistrationService.register(command))
			.isInstanceOf(RoleNotFoundException.class)
			.hasMessageContaining("USER");
		
		// Verify
		verify(userMapper).existsByEmail("user@example.com");
		verify(passwordEncoder, never()).encode(any());
		verify(userMapper, never()).insert(any());
		verify(userRoleMapper, never()).insert(any(), any());
	}
	
	@Test
	public void register_test_004_userRegisterCountZero() throws Exception {
		// Prepare
		UserRegistrationCommand command = new UserRegistrationCommand(
				" USER@Example.COM ",
				" テストユーザー ",
				"password123");
		
		Role role = createUserRole();
		
		when(userMapper.existsByEmail("user@example.com")).thenReturn(false);
		when(roleMapper.findByRoleCode("USER")).thenReturn(Optional.of(role));
		when(passwordEncoder.encode("password123")).thenReturn("{bcrypt}encoded-password");
		when(userMapper.insert(any(User.class))).thenReturn(0);
		
		// Test
		assertThatThrownBy(() -> userRegistrationService.register(command))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("ユーザーの登録件数が想定と異なります。");
		verify(userMapper).existsByEmail("user@example.com");
		verify(roleMapper).findByRoleCode("USER");
		verify(passwordEncoder).encode("password123");
		verify(userRoleMapper, never()).insert(any(), any());
	}
	
	@Test
	public void register_test_005_userRoleRegisterZero() throws Exception {
		// Prepare
		UserRegistrationCommand command = new UserRegistrationCommand(
				" USER@Example.COM ",
				" テストユーザー ",
				"password123");
		
		Role role = createUserRole();
		
		when(userMapper.existsByEmail("user@example.com")).thenReturn(false);
		when(roleMapper.findByRoleCode("USER")).thenReturn(Optional.of(role));
		when(passwordEncoder.encode("password123")).thenReturn("{bcrypt}encoded-password");
		when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
			User user = invocation.getArgument(0);
			user.setId(100L);
			return 1;
		});
		when(userRoleMapper.insert(100L, 10L)).thenReturn(0);
		
		// Test
		assertThatThrownBy(() -> userRegistrationService.register(command))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("ユーザーロールの登録件数が想定と異なります。");
		verify(userMapper).existsByEmail("user@example.com");
		verify(roleMapper).findByRoleCode("USER");
		verify(passwordEncoder).encode("password123");
	}
	
	private Role createUserRole() {
		Role role = new Role();
		role.setId(10L);
		role.setRoleCode("USER");
		role.setRoleName("一般ユーザー");
		return role;
	}
}
