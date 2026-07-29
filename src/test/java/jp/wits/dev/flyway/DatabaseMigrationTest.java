package jp.wits.dev.flyway;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class DatabaseMigrationTest {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Test
	public void flywayMigrationShouldCreateUsersTable() throws Exception {
		Integer count = jdbcTemplate.queryForObject("""
				SELECT COUNT(*)
				FROM information_schema.tables
				WHERE table_schema = 'public'
				AND table_name = 'users'
				""", Integer.class);
		
		assertThat(count).isEqualTo(1);
	}
	
	@Test
	public void flywayMigrationShouldInsertRoles() throws Exception {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM roles", Integer.class);
		
		assertThat(count).isEqualTo(2);
	}
}
