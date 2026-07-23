package jp.wits.dev.exception;

public class DuplicateEmailException extends RuntimeException {
	
	public DuplicateEmailException() {
		super("指定されたメールアドレスは既に登録されています。");
	}
}
