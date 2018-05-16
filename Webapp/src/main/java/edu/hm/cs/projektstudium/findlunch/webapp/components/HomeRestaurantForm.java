package edu.hm.cs.projektstudium.findlunch.webapp.components;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeRestaurantForm {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String street;
    private String streetNumber;
    private String city;
    private String zip;

    public HomeRestaurantForm() {
        super();
    }

    public HomeRestaurantForm(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.email = restaurant.getEmail();
        this.phone = restaurant.getPhone();
        this.street = restaurant.getStreet();
        this.streetNumber = restaurant.getStreetNumber();
        this.city = restaurant.getCity();
        this.zip = restaurant.getZip();
    }

}
