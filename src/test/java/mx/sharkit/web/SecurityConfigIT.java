package mx.sharkit.web;

import org.junit.Test;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class SecurityConfigIT {

	@Test(expected = RuntimeException.class)
	public void exceptionOnConfigureNull() {
		new SecurityConfig().configure((HttpSecurity) null);
	}

}
