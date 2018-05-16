package edu.hm.cs.projektstudium.findlunch.webapp.model.validation.passwordReset;

import edu.hm.cs.projektstudium.findlunch.webapp.components.PasswordResetForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Alexander Carl on 18.06.2017.
 */

@Component
public class PasswordResetPasswordEqualValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return PasswordResetForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordResetForm passwordResetForm = (PasswordResetForm) target;

        if(!passwordResetForm.getNewPassword().equals(passwordResetForm.getNewPasswordRepeat())) {
            errors.rejectValue("newPassword", "universal.validation.passwordNotEqual");
        }
    }

}
