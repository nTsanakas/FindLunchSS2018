package edu.hm.cs.projektstudium.findlunch.webapp.model.validation.passwordReset;

/**
 * Created by Alexander Carl on 18.06.2017.
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordResetCodeValidator.class)
@Documented
public @interface PasswordResetCode {

    String message() default "{passwordReset.validation.passwordResetCodeValidation}";

    Class<?>[] groups() default {};

    public abstract Class<? extends Payload>[] payload() default {};
}
