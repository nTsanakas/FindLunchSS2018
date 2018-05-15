package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import edu.hm.cs.projektstudium.findlunch.webapp.model.KitchenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface RestaurantKitchenTypeRepository extends JpaRepository<KitchenType, Serializable> {

    KitchenType findByName(String name);
}
