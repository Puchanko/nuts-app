package jp.wits.dev.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.wits.dev.model.entity.User;

@Mapper
public interface UserMapper {
	
	Optional<User> findByEmail(@Param("email") String email);
	
	boolean existsByEmail(String email);
	
	int insert(User user);
}
