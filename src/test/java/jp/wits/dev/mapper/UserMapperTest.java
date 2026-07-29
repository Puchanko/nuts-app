package jp.wits.dev.mapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import jp.wits.dev.model.entity.User;

@MybatisTest
@AutoConfigureTestDatabase(
	replace = AutoConfigureTestDatabase.Replace.NONE
)
public class UserMapperTest {
	
	@Autowired
	private UserMapper userMapper;
	
	@Test
	public void findByEmail_test_001() throws Exception {
		User actual = userMapper
				.findByEmail("admin@example.com")
				.orElseThrow();
		
		
		assertThat(actual.getEmail()).isEqualTo("admin@example.com");
		assertThat(actual.getDisplayName()).isEqualTo("管理者ユーザー");
		assertThat(actual.isEnabled()).isTrue();
		assertThat(actual.isAccountLocked()).isFalse();
		assertThat(actual.getRoles()).extracting("roleCode").containsExactly("USER", "ADMIN");
	}
	
	@Test
	public void findByEmail_test_002() throws Exception {
		User actual = userMapper
				.findByEmail("ADMIN@EXAMPLE.COM")
				.orElseThrow();
		assertThat(actual.getEmail()).isEqualTo("admin@example.com");
	}
	
	@Test
	public void findByEmail_test_003() throws Exception {
		assertThat(userMapper.findByEmail("unknown@example.com")).isEmpty();
	}
}
