package cz.controllers;

import cz.models.Reservation;
import cz.payload.ReservationPostRequest;
import cz.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(reservationService.getReservationById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Reservation>> getAllReservation() {
        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Double> addReservation(@RequestBody ReservationPostRequest newReservation) {
        return new ResponseEntity<>(reservationService.addReservation(newReservation), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateReservation(@RequestBody Reservation reservation) {
        reservationService.updateReservation(reservation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteSurface(@PathVariable(name = "id") Integer id) {
        reservationService.deleteReservation(id);
    }
}
