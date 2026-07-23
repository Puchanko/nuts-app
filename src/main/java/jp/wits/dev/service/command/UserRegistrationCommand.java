package jp.wits.dev.service.command;

public record UserRegistrationCommand(
		String email,
		String displayName,
		String rawPassword) {
}
