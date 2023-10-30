package org.bookstore.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.bookstore.dto.user.RegisterUserRequest;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    @Override
    public boolean isValid(Object candidate, ConstraintValidatorContext context) {
        RegisterUserRequest user = (RegisterUserRequest) candidate;
        return user.getPassword().equals(user.getRepeatPassword());
    }
}