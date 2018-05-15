package edu.hm.cs.projektstudium.findlunch.webapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "swa_todo_request_typ")
@Getter
@Setter
public class ToDoRequestTyp {

    @Id
    private int id;

    private String name;

    @OneToMany(mappedBy = "toDoRequestTyp", fetch = FetchType.LAZY)
    private List<ToDo> toDos;

    public ToDoRequestTyp() { super(); }
}
