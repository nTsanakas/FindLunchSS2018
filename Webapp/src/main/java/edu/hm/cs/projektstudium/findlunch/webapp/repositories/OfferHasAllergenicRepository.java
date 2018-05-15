package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import edu.hm.cs.projektstudium.findlunch.webapp.model.OfferHasAllergenic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface OfferHasAllergenicRepository extends JpaRepository<OfferHasAllergenic, Serializable> {

    @Modifying
    @Query("delete from OfferHasAllergenic where offer_id = ?1")
    void deleteByOfferId(int offerId);
}