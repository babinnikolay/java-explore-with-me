package ru.practicum.explorewithme.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NewEventDateValidator.class)
public @interface ValidNewEventDate {
    String message() default "{message.id}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
