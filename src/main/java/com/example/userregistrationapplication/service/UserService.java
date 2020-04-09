package com.example.userregistrationapplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userregistrationapplication.entity.User;
import com.example.userregistrationapplication.form.UserForm;
import com.example.userregistrationapplication.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	IPDetailsService ipDetailsService;

	@Autowired
	UserRepository userRepository;

	public User createUser(UserForm userForm) {
		String encrytedPassword = this.passwordEncoder.encode(userForm.getPassword());
		User user = new User(userForm.getName(), userForm.getEmail(), encrytedPassword);
		userRepository.save(user);
		return user;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public String conditionalView(String ip) {
		boolean attemptsStatus = ipDetailsService.checkAttempts(ip);
		if (attemptsStatus) {
			return "registerPageWithCaptcha";
		} else {
			return "registerPage";
		}
	}
}
