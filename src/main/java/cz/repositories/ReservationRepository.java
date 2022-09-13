package cz.repositories;

import cz.models.Reservation;

import java.util.Collection;

public interface ReservationRepository {

    Reservation findReservationById(int id);

    Collection<Reservation> findAllReservations();

    Collection<Reservation> findReservationsByPhoneNumber(String phoneNumber);

    void createReservation(Reservation reservation);

    void updateReservation(Reservation reservation);

    void deleteReservation(int id);
}
