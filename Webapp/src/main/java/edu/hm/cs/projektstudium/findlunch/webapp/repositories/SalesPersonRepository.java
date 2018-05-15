package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import edu.hm.cs.projektstudium.findlunch.webapp.model.SalesPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface SalesPersonRepository extends JpaRepository<SalesPerson, Serializable> {

    SalesPerson findByEmail(String userEmail);

    SalesPerson findById(int id);
}
