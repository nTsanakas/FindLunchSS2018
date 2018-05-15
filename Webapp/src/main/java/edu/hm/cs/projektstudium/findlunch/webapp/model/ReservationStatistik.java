package edu.hm.cs.projektstudium.findlunch.webapp.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
/**
 * The Class ReservationStatistik.
 */
/*
 * Wrapper Klasse, da man sonst keinen zugriff auf die reservation Objekt hat
 * , da java.util.ArrayList nicht erkannt wird von Thymeleaf*/
@ApiModel(
		description = "Wrapper-Klasse, da man sonst keinen Zugriff auf das Reservation-Objekt hat."
)
@Getter
@Setter
public class ReservationStatistik {

	
	public ReservationStatistik(ArrayList<Reservation> reservations, String label, int countAll) {
		this.setLabel(label);
		this.setAverageRespondeTime(calculateAverageRespondeTime(reservations));
		this.setPercent(calculatePercent(reservations.size(), countAll));
		this.setReservationCount(reservations.size());
		this.setReservations(reservations);
		this.setTotalValue(calculateTotalValue(reservations));
	}

	private String label = "";
	private int reservationCount = 0;
	private double totalValue = 0;
	private double averageRespondeTime = 0;
	private double percent = 0;
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();
	
	private double calculateTotalValue(ArrayList<Reservation> reservations){
		double sumPrice = 0;
		for (Reservation reservation : reservations) {
			sumPrice += reservation.getTotalPrice();
		}
		return sumPrice;
	}
	
	private double calculateAverageRespondeTime(ArrayList<Reservation> reservations){
		double avrgTime = 0;
		double totalTime = 0;
		int counter = 1;

		for (Reservation reservation : reservations) {
			if(reservation.getTimestampReceived() != null && reservation.getTimestampResponded() != null){
				long milliseconds = reservation.getTimestampResponded().getTime()-reservation.getTimestampReceived().getTime();
				long minutes =  milliseconds/60000;
				totalTime += minutes;
				counter++;
			}
			avrgTime = totalTime/counter;
		}
		
		return avrgTime;
	}
	
	private double calculatePercent(int countBase, int countAll){
		if(countAll > 0 && countBase >0){
			double percent = (double)countBase/countAll;
			percent = percent*100;
			return percent;
		}
		return 0;
	}
}
