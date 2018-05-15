package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Additives;

/**
 * The Interface AdditivesRepository. Abstraction for the data access layer.
 * @author Basti Heller
 *
 */
@Repository
public interface AdditivesRepository extends JpaRepository<Additives, Integer>{

	/**
	 * Finds additives by the name of the product.
	 * @param courseType
	 * @return
	 */
	Additives findByName(String name);
	
	/**
	 * Finds additives by the key of the product.
	 * @param courseType
	 * @return
	 */
	Additives findByShortKey(String key);
	
	/**
	 * Finds additives by the id of the product.
	 * @param id
	 * @return
	 */
	Additives findById(int id);
	
}
