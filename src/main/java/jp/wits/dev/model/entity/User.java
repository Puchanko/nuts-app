package jp.wits.dev.model.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class User {
	
	private Long id;

    private String email;

    private String passwordHash;

    private String displayName;

    private boolean enabled;

    private boolean accountLocked;

    private int failedLoginAttempts;

    private OffsetDateTime lockedAt;

    private OffsetDateTime lastLoginAt;

    private OffsetDateTime passwordChangedAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private List<Role> roles = new ArrayList<>();
}
