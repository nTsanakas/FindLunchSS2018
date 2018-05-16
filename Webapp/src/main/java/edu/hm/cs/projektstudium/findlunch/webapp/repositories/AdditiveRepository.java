package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Additive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The Interface AdditivesRepository. Abstraction for the data access layer.
 */
@Repository
public interface AdditiveRepository extends JpaRepository<Additive, Integer>{

	/**
	 * Finds additives by the name of the product.
	 * @param name
	 * @return
	 */
	Additive findByName(String name);

	/**
	 * Finds additives by the key of the product.
	 * @param key
	 * @return
	 */
	Additive findByShortKey(String key);

	/**
	 * Finds additives by the id of the product.
	 * @param id
	 * @return
	 */
	Additive findById(int id);
}
