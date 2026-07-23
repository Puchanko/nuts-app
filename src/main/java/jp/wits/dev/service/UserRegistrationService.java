package jp.wits.dev.service;

import java.time.OffsetDateTime;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.wits.dev.exception.DuplicateEmailException;
import jp.wits.dev.exception.RoleNotFoundException;
import jp.wits.dev.mapper.RoleMapper;
import jp.wits.dev.mapper.UserMapper;
import jp.wits.dev.mapper.UserRoleMapper;
import jp.wits.dev.model.entity.Role;
import jp.wits.dev.model.entity.User;
import jp.wits.dev.service.command.UserRegistrationCommand;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
	
	private static final String DEFAULT_ROLE_CODE = "USER";
	
	private final UserMapper userMapper;
	
	private final RoleMapper roleMapper;
	
	private final UserRoleMapper userRoleMapper;
	
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	public Long register(UserRegistrationCommand command) {
		String normalizedEmail = normalizedEmail(command.email());
		
		if (userMapper.existsByEmail(normalizedEmail)) {
			throw new DuplicateEmailException();
		}
		
		Role userRole = roleMapper
					.findByRoleCode(DEFAULT_ROLE_CODE)
					.orElseThrow(() -> new RoleNotFoundException(DEFAULT_ROLE_CODE));
		
		OffsetDateTime now = OffsetDateTime.now();
		
		User user = new User();
		user.setEmail(normalizedEmail);
		user.setDisplayName(command.displayName().trim());
		user.setPasswordHash(passwordEncoder.encode(command.rawPassword()));
		user.setEnabled(true);
		user.setAccountLocked(false);
		user.setFailedLoginAttempts(0);
		user.setPasswordChangedAt(now);
		
		int insertedUser = userMapper.insert(user);
		if (insertedUser != 1) {
			throw new IllegalStateException("ユーザーの登録件数が想定と異なります。");
		}
		
		int insertedRoles = userRoleMapper.insert(user.getId(), userRole.getId());
		if (insertedRoles != 1) {
			throw new IllegalStateException("ユーザーロールの登録件数が想定と異なります。");
		}
		
		return user.getId();
	}
	
	private String normalizedEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}
}
