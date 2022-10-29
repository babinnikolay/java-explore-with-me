package ru.practicum.explorewithme.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NewEventDateValidator implements ConstraintValidator<ValidNewEventDate, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime newEventDate, ConstraintValidatorContext constraintValidatorContext) {
        if (newEventDate == null) {
            return true;
        }
        return newEventDate.isAfter(LocalDateTime.now().plusHours(2));
    }
}
