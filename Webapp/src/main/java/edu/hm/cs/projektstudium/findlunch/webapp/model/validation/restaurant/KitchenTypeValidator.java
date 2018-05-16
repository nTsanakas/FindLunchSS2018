package edu.hm.cs.projektstudium.findlunch.webapp.model.validation.restaurant;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Restaurant;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class KitchenTypeValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        //return Restaurant.class.isAssignableFrom(aClass);
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Restaurant restaurant = (Restaurant) target;

        Object kitchenType = null;

        try {
            kitchenType = restaurant.getKitchenTypes().get(0);
        } catch (Exception e) {
            //kitchenTypesAsString is empty
        }

        if(kitchenType == null) {
            errors.rejectValue("offerTimes", "restaurant.validation.kitchenTypes");
        }
    }
}
