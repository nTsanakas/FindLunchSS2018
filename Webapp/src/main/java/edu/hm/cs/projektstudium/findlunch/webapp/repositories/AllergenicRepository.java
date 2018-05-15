package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Allergenic;

import java.util.List;

@Repository
public interface AllergenicRepository extends JpaRepository<Allergenic, Integer>{

	/**
	 * 
	 * @param name
	 * @return
	 */
	Allergenic findByName(String name);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	Allergenic findByShortKey(String key);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Allergenic findById(int id);
	
}
