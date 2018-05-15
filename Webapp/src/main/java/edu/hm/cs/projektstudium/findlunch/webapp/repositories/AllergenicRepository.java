package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Allergenic;

/**
 * The Interface AllergenicRepository. Abstraction for the data access layer.
 * @author Basti Heller
 *
 */
@Repository
public interface AllergenicRepository extends JpaRepository<Allergenic, Integer>{

	/**
	 * Finds allerginics by the name of the product.
	 * @param courseType
	 * @return
	 */
	Allergenic findByName(String name);
	
	/**
	 * Finds allerginics by the name of the product.
	 * @param courseType
	 * @return
	 */
	Allergenic findByShortKey(String key);
	
	/**
	 * Finds allerginics by the name of the product.
	 * @param id
	 * @return
	 */
	Allergenic findById(int id);
	
}
