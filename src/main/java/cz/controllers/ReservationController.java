package cz.controllers;

import cz.models.Reservation;
import cz.payload.ReservationPostRequest;
import cz.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * The reservation controller for spring boot api
 *
 * @author Peter Kultán
 */
@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;


    /**
     * Returns a reservation searched by id
     *
     * @param id an id of searched reservation
     * @return a reservation searched by id with HttpStatus
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(reservationService.getReservationById(id), HttpStatus.OK);
    }

    /**
     * Returns all reservations or if presented, filter it with phoneNumber
     *
     * @param phoneNumber not required param, a phone number of searched reservations
     * @return reservations with searched phone number, HttpStatus
     */
    @GetMapping
    public ResponseEntity<Collection<Reservation>> getAllReservation(@RequestParam(required = false) String phoneNumber) {
        if (phoneNumber != null) {
            return new ResponseEntity<>(reservationService.getReservationsByPhoneNumber(phoneNumber), HttpStatus.OK);
        }
        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }

    /**
     * Add new reservation
     *
     * @param newReservation a reservation to be saved into the system
     * @return price of reservation and HttpStatus
     */
    @PostMapping
    public ResponseEntity<Double> addReservation(@RequestBody ReservationPostRequest newReservation) {
        return new ResponseEntity<>(reservationService.addReservation(newReservation), HttpStatus.CREATED);
    }

    /**
     * Update saved reservation
     *
     * @param reservation to be updated, with new values
     * @return HttpStatus of operation
     */
    @PutMapping
    public ResponseEntity<HttpStatus> updateReservation(@RequestBody Reservation reservation) {
        reservationService.updateReservation(reservation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete existing reservation specified by id
     *
     * @param id of reservation to be deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSurface(@PathVariable(name = "id") Integer id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
