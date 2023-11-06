package org.bookstore.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstField;
    private String secondField;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstField = constraintAnnotation.fields()[0];
        this.secondField = constraintAnnotation.fields()[1];
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object field = beanWrapper.getPropertyValue(this.firstField);
        Object fieldMatch = beanWrapper.getPropertyValue(this.secondField);
        return Objects.equals(field, fieldMatch);
    }
}
