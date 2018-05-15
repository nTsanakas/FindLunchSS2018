package edu.hm.cs.projektstudium.findlunch.webapp.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "swa_todo_list")
@Getter
@Setter
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "todo_request_typ_id")
    private ToDoRequestTyp toDoRequestTyp;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sales_person_id")
    private SalesPerson salesPerson;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @Type(type = "timestamp")
    private Date datetime;

    public ToDo() { super(); }
}
