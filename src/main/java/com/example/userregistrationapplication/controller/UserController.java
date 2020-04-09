package com.example.userregistrationapplication.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.userregistrationapplication.entity.User;
import com.example.userregistrationapplication.form.UserForm;
import com.example.userregistrationapplication.service.CaptchaValidateService;
import com.example.userregistrationapplication.service.IPDetailsService;
import com.example.userregistrationapplication.service.UserService;
import com.example.userregistrationapplication.validation.UserRegistrationValidator;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	IPDetailsService ipDetailsService;

	@Autowired
	CaptchaValidateService captchaValidateService;

	@Autowired
	UserRegistrationValidator userRegistrationValidator;

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == UserForm.class) {
			dataBinder.setValidator(userRegistrationValidator);
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String viewHome(Model model) {
		return "welcomePage";
	}

	@RequestMapping(value = "/registerSuccessful", method = RequestMethod.GET)
	public String viewRegisterSuccessful(Model model) {
		return "registerSuccessPage";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String viewRegister(Model model, HttpServletRequest httpServletRequest) {
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);
		return userService.conditionalView(httpServletRequest.getRemoteAddr());
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(Model model, @ModelAttribute("userForm") @Validated UserForm userForm,
			BindingResult result,
			@RequestParam(name = "g-recaptcha-response", required = false) String recaptchaResponse,
			HttpServletRequest httpServletRequest) {

		String ip = httpServletRequest.getRemoteAddr();
		ipDetailsService.saveIPDetails(ip);
		if (result.hasErrors()) {
			return userService.conditionalView(ip);
		}
		try {
			boolean checkAttempts = ipDetailsService.checkAttempts(ip);
			if (checkAttempts == true && recaptchaResponse != null) {
				if (recaptchaResponse.isEmpty()) {
					model.addAttribute("errorMessage", "Error: " + "Captcha verification is required");
					return "registerPageWithCaptcha";
				} else {
					boolean validateCaptcha = captchaValidateService.validateCaptcha(recaptchaResponse);
					if (!validateCaptcha) {
						model.addAttribute("errorMessage", "Error: " + "Captcha is not valid");
						return "registerPageWithCaptcha";
					}
				}
			}
			userService.createUser(userForm);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error: " + e.getMessage());
			return userService.conditionalView(ip);
		}

		return "redirect:/registerSuccessful";
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String getUsers(Model model) {
		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);
		return "usersPage";
	}

}
