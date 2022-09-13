package cz.services;

import cz.models.Reservation;
import cz.payload.ReservationPostRequest;

import java.util.Collection;

public interface ReservationService {
    Collection<Reservation> getAllReservations();

    Collection<Reservation> getReservationsByPhoneNumber(String phoneNumber);

    Reservation getReservationById(int id);

    double addReservation(ReservationPostRequest reservation);

    void updateReservation(Reservation reservation);

    void deleteReservation(int id);
}
