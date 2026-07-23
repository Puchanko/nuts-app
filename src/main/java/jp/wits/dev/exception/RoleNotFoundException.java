package jp.wits.dev.exception;

public class RoleNotFoundException extends RuntimeException {
	
	public RoleNotFoundException(String roleCode) {
		super("ロールが定義されていません: " + roleCode);
	}
}
