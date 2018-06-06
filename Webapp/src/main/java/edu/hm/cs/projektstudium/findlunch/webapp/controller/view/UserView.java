package edu.hm.cs.projektstudium.findlunch.webapp.controller.view;

/**
 * Klasse UserView zum Begrenzen der per JSON zurückgegebenen Attribute des Users.
 */
public class UserView {

    /**
     * Das Interface UserLoginRest. Nur diejenigen Attribute, die mit der Annotation
     * "JsonView(UserView.UserLoginRest.class)" gekennzeichnet sind, werden beim GET-Aufruf zurückgegeben.
     */
    public interface UserLoginRest {
    }
}