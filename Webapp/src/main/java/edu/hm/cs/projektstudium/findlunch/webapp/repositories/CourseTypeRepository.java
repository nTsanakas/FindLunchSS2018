package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.hm.cs.projektstudium.findlunch.webapp.model.CourseType;

/**
 * The repository for the coursetypes.
 */
@Repository
public interface CourseTypeRepository extends JpaRepository<CourseType, Integer>{

	List<CourseType> findByNameAndRestaurantId(String courseTypeAsString, int restaurantId);

	List<CourseType> findByRestaurantId(int restaurantId);

	/**
	 * Gets a coursetype by its ID
	 * @param id the ID
	 * @return the coursetype
	 */
	CourseType findById(int id);
	
	/**
	 * Geths all coursetypes of a restaurant ordered by the sort value
	 * @param restaurant_id the restaurant
	 * @return
	 */
	List<CourseType> findByRestaurantIdOrderBySortByAsc(int restaurant_id);
	
	/**
	 * Gets a specific coursetype of a given restaurant
	 * @param id the coursetype
	 * @param restaurant_id the restaurant
	 * @return
	 */
	CourseType findByIdAndRestaurantId(int id, int restaurant_id);
}