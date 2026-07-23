package jp.wits.dev.controller;

import java.util.Objects;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.wits.dev.exception.DuplicateEmailException;
import jp.wits.dev.model.form.SignupForm;
import jp.wits.dev.service.UserRegistrationService;
import jp.wits.dev.service.command.UserRegistrationCommand;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {
	
	private final UserRegistrationService userRegistrationService;
	
	@ModelAttribute("signupForm")
	public SignupForm signupForm() {
		return new SignupForm();
	}
	
	@GetMapping("/signup")
	public String showSignupForm() {
		return "signup";
	}
	
	@PostMapping("/signup")
	public String signup(
			@Valid @ModelAttribute("signupForm") SignupForm signupForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		
		if (!Objects.equals(
				signupForm.getPassword(),
				signupForm.getPasswordConfirmation())) {
			bindingResult.rejectValue(
					"passwordConfirmation",
					"password.mismatch",
					"パスワードが一致しません。");
		}
		
		if (bindingResult.hasErrors()) {
			clearPasswords(signupForm);
			return "signup";
		}
		
		try {
			UserRegistrationCommand command 
				= new UserRegistrationCommand(
						signupForm.getEmail(),
						signupForm.getDisplayName(),
						signupForm.getPassword());
			
			userRegistrationService.register(command);
		} catch (DuplicateEmailException e) {
			bindingResult.rejectValue("email", "email.duplicate", e.getMessage());
			clearPasswords(signupForm);
			return "signup";
		}
		
		redirectAttributes.addFlashAttribute("signupCompleted", true);
		
		return "redirect:/login";
	}
	
	private void clearPasswords(SignupForm signupForm) {
		signupForm.setPassword("");
		signupForm.setPasswordConfirmation("");
	}
}
