package edu.hm.cs.projektstudium.findlunch.webapp.components;

import edu.hm.cs.projektstudium.findlunch.webapp.model.ToDo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeToDoForm {

    private int id;
    private int restaurantId;
    private String requestTyp;
    private String restaurantName;
    private String timestamp;
    private DateReOrder dateReOrder = new DateReOrder();

    public HomeToDoForm() {
        super();
    }

    public HomeToDoForm(ToDo toDo) {
        this.id = toDo.getId();
        this.restaurantId = toDo.getRestaurant().getId();
        this.requestTyp = toDo.getToDoRequestTyp().getTodoRequestTyp();
        this.restaurantName = toDo.getRestaurant().getName();

        String time = toDo.getDatetime().toString();
        time = time.substring(0, time.length() - 5);
        this.timestamp = dateReOrder.reOrderDateTimeString(time);
    }

}
