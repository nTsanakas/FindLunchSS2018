package edu.hm.cs.projektstudium.findlunch.webapp.repositories;

import edu.hm.cs.projektstudium.findlunch.webapp.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Serializable> {

    List<ToDo> findBySalesPersonEmail(String email);

    ToDo findById(int toDoId);

    List<ToDo> findByOfferId(int offerId);

    @Modifying
    @Query("delete from ToDo where id = ?1")
    void deleteById(int toDoId);

}
