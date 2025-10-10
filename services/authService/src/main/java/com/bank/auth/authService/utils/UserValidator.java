package com.bank.auth.authService.utils;

import com.bank.auth.authService.models.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "Email обязателен");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "Пароль обязателен");
        }

        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            errors.rejectValue("firstName", "Имя обязательно");
        }

        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            errors.rejectValue("lastName", "Фамилия обязательна");
        }
    }
}
