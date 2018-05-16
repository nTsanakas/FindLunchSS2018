package edu.hm.cs.projektstudium.findlunch.webapp.controller;

import edu.hm.cs.projektstudium.findlunch.webapp.components.PasswordResetForm;
import edu.hm.cs.projektstudium.findlunch.webapp.model.validation.passwordReset.PasswordResetValidator;
import edu.hm.cs.projektstudium.findlunch.webapp.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class PasswordResetControllerSWA {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordResetValidator passwordResetValidator;

    @RequestMapping(value = "/swa/passwordReset", method = RequestMethod.GET)
    public String passwordReset(Model model) {
        PasswordResetForm passwordResetForm = new PasswordResetForm();
        model.addAttribute("passwordResetForm", passwordResetForm);

        return "swa_passwordReset";
    }

    @RequestMapping(value = "/swa/passwordReset", method = RequestMethod.POST)
    public String processPasswordReset(@ModelAttribute("passwordResetForm") @Valid PasswordResetForm passwordResetForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "swa_passwordReset";
        }
        passwordResetService.setNewPassword(passwordResetForm.getNewPassword(), passwordResetForm.getSecurityCode());

        return "redirect:/swa/login?passwordChangeSuccess";
    }

    @InitBinder
    public void initialiseBinder(WebDataBinder binder) {
        binder.setValidator(passwordResetValidator);
    }

}
