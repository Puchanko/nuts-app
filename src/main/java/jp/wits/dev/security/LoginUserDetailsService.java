package jp.wits.dev.security;

import java.util.Locale;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.wits.dev.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {
	
	private final UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String normalizedEmail = username.strip().toLowerCase(Locale.ROOT);
		return userMapper.findByEmail(normalizedEmail)
				.map(LoginUserDetails::from)
				.orElseThrow(() -> 
					new UsernameNotFoundException("ユーザーが見つかりません。")
				);
	}

}
