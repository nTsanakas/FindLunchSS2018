package edu.hm.cs.projektstudium.findlunch.webapp.model.validation.profile;

import edu.hm.cs.projektstudium.findlunch.webapp.components.ProfileForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Alexander Carl on 23.06.2017.
 */

public class ProfilePasswordEqualValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return ProfileForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileForm profileForm = (ProfileForm) target;

        if(!profileForm.getNewPassword().equals(profileForm.getRepeatNewPassword())) {
            errors.rejectValue("newPassword", "universal.validation.passwordNotEqual");
        }

    }

}
