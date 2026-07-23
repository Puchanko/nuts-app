package jp.wits.dev.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.wits.dev.security.LoginUserDetails;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String index(
			@AuthenticationPrincipal LoginUserDetails loginUser,
			Model model) {
		model.addAttribute("loginUser", loginUser);
		return "index";
	}
}
