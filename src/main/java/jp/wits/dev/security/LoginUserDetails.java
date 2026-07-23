package jp.wits.dev.security;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jp.wits.dev.model.entity.User;

public class LoginUserDetails implements UserDetails {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	private final Long userId;
	
	private final String email;
	
	private final String passwordHash;
	
	private final String displayName;
	
	private final boolean enabled;
	
	private final boolean accountLocked;
	
	private final List<GrantedAuthority> authorities;
	
	private LoginUserDetails(
			Long userId,
			String email,
			String passwordHash,
			String displayName,
			boolean enabled,
			boolean accountLocked,
			List<GrantedAuthority> authorities) {
		this.userId = userId;
		this.email = email;
		this.passwordHash = passwordHash;
		this.displayName = displayName;
		this.enabled = enabled;
		this.accountLocked = accountLocked;
		this.authorities = List.copyOf(authorities);
	}
	
	public static LoginUserDetails from(User user) {
		List<GrantedAuthority> authorities = user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(
						"ROLE_" + role.getRoleCode()))
				.map(GrantedAuthority.class::cast)
				.toList();
		
		return new LoginUserDetails(
				user.getId(),
				user.getEmail(),
				user.getPasswordHash(),
				user.getDisplayName(),
				user.isEnabled(),
				user.isAccountLocked(),
				authorities);
	}
	
	public Long getUserId() {
		return this.userId;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public @Nullable String getPassword() {
		return this.passwordHash;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !this.accountLocked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
