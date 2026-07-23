package jp.wits.dev.model.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class SignupForm {
	
	@NotBlank(message = "メールアドレスを入力してください。")
	@Email(message = "メールアドレスの形式で入力してください。")
	@Size(max = 255, message = "メールアドレスは255文字以内で入力してください。")
	private String email;
	
	@NotBlank(message = "表示名を入力してください。")
	@Size(max = 100, message = "表示名は100文字以内で入力してください。")
	private String displayName;
	
	@NotBlank(message = "パスワードを入力してください。")
	@Size(min = 8, max = 72, message = "パスワードは8文字以上72文字以内で入力してください。")
	private String password;
	
	@NotBlank(message = "確認用パスワードを入力してください。")
	private String passwordConfirmation;
}
