package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.hm.cs.projektstudium.findlunch.webapp.model.ResetPassword;
import edu.hm.cs.projektstudium.findlunch.webapp.model.User;

/**
 * The Interface ResetPasswordRepository. Abstraction for the data access layer.
 * @author oberm
 *
 */
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Integer>{

	/**
	 * Finds the password by token.
	 * 
	 * @param token the token
	 */
	ResetPassword findByToken(String token);
	
	/**
	 * Finds the password by user.
	 * 
	 * @param user the user
	 */
	ResetPassword findByUser(User user);
}
