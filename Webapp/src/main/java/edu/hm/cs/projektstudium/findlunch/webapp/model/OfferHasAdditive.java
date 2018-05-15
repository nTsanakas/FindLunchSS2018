package edu.hm.cs.projektstudium.findlunch.webapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "offer_has_additives")
@Getter
@Setter
public class OfferHasAdditive {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "additives_id")
    private Additive additive;

    public OfferHasAdditive() { super(); }
}
