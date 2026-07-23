package jp.wits.dev.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import jp.wits.dev.model.entity.Role;

@Mapper
public interface RoleMapper {
	
	Optional<Role> findByRoleCode(String roleCode);
}
