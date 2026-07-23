package jp.wits.dev.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	
	@PreAuthorize("hasRole('ADMIN')")
	public String getAdminMessage() {
		return "管理者専用の処理を実行しました。";
	}
}
