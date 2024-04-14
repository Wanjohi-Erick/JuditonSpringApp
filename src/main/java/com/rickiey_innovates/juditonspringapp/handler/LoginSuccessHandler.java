package com.rickiey_innovates.juditonspringapp.handler;

import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import com.rickiey_innovates.juditonspringapp.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private final UserRepository userRepository;

	public LoginSuccessHandler(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication authentication) throws IOException, ServletException {

		try {
			User user = userRepository.findByUsername(authentication.getName()).orElse(null);
			int logins = user.getLogins();
			logins++;
			user.setLogins(logins);
			user.setSessionid(RequestContextHolder.currentRequestAttributes().getSessionId());
			userRepository.save(user);


			/*if (user.getPassword().equalsIgnoreCase(passwordEncoder.encode("password"))) {
				redirectStrategy.sendRedirect(arg0, arg1, "/api/resetPass");
			} else {
				redirectStrategy.sendRedirect(arg0, arg1, "/dashboard");
			}*/
			redirectStrategy.sendRedirect(arg0, arg1, "/home");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
