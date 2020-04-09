package com.example.userregistrationapplication.validation;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.userregistrationapplication.entity.User;
import com.example.userregistrationapplication.form.UserForm;
import com.example.userregistrationapplication.service.CaptchaValidateService;
import com.example.userregistrationapplication.service.IPDetailsService;
import com.example.userregistrationapplication.service.UserService;

@Component
public class UserRegistrationValidator implements Validator {

	private EmailValidator emailValidator = EmailValidator.getInstance();

	@Autowired
	private UserService userService;

	@Autowired
	IPDetailsService ipDetailsService;

	@Autowired
	CaptchaValidateService captchaValidateService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == UserForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserForm userForm = (UserForm) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Mandatory.registerUserForm.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Mandatory.registerUserForm.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Mandatory.registerUserForm.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword",
				"Mandatory.registerUserForm.confirmPassword");

		if (!this.emailValidator.isValid(userForm.getEmail())) {
			errors.rejectValue("email", "Validation.registerUserForm.email");
		} else if (userForm.getEmail() != null) {
			User dbUser = userService.getUserByEmail(userForm.getEmail());
			if (dbUser != null) {
				errors.rejectValue("email", "Duplicate.registerUserForm.email");
			}
		}

		if (!errors.hasErrors()) {
			if (!userForm.getConfirmPassword().equals(userForm.getPassword())) {
				errors.rejectValue("confirmPassword", "Match.registerUserForm.confirmPassword");
			}
		}
	}

}
