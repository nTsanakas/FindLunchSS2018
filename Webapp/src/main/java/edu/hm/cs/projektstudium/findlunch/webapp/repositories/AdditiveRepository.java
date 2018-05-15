package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Additive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdditiveRepository extends JpaRepository<Additive, Integer>{

	/**
	 * 
	 * @param name
	 * @return
	 */
	Additive findByName(String name);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	Additive findByShortKey(String key);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Additive findById(int id);

	
}
