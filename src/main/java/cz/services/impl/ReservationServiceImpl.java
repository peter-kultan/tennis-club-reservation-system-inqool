package cz.services.impl;

import cz.enums.ReservationType;
import cz.models.Reservation;
import cz.models.User;
import cz.payload.ReservationPostRequest;
import cz.repositories.CourtRepository;
import cz.repositories.ReservationRepository;
import cz.repositories.UserRepository;
import cz.services.ReservationService;
import org.apache.tomcat.util.file.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Override
    public Collection<Reservation> getAllReservations() {
        return reservationRepository.findAllReservations();
    }

    @Override
    public Collection<Reservation> getReservationsByPhoneNumber(String phoneNumber) {
        return reservationRepository.findReservationsByPhoneNumber(phoneNumber);
    }

    @Override
    public Reservation getReservationById(int id) {
        return reservationRepository.findReservationById(id);
    }

    @Override
    public double addReservation(ReservationPostRequest reservation) {
        var court = courtRepository.findCourtById(reservation.getCourtId());
        if (court == null) {
            throw new IllegalArgumentException("New reservation with non-existing court");
        }

        String allCountryPhoneNumberRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";

        if (Matcher.match(allCountryPhoneNumberRegex, reservation.getPhoneNumber(), true)) {
            throw new IllegalArgumentException("Phone number is not valid");
        }

        var overlap = reservationRepository.findAllReservations().stream().filter(res -> Objects.equals(res.getCourt().getId(), reservation.getCourtId()))
                .anyMatch(res -> (res.getStartDateTime().isBefore(LocalDateTime.parse(reservation.getStartDate()).plusMinutes(reservation.getDuration()))) &&
                        res.getStartDateTime().plusMinutes(res.getDuration().toMinutes()).isAfter(LocalDateTime.parse(reservation.getStartDate())));
        if (overlap) {
            throw new IllegalArgumentException("There is overlap with existing reservation");
        }

        var user = userRepository.findUserByPhoneNumber(reservation.getPhoneNumber());
        if (user == null) {
            user = new User(reservation.getPhoneNumber(), reservation.getUserName());
            userRepository.createUser(user);
        } else if (!Objects.equals(user.getName(), reservation.getUserName())) {
            throw new IllegalArgumentException("Phone number is paired with different name");
        }

        var newReservation = new Reservation(
                court, user, LocalDateTime.parse(reservation.getStartDate()),
                Duration.of(reservation.getDuration(), ChronoUnit.MINUTES), reservation.getReservationType());
        reservationRepository.createReservation(newReservation);
        return newReservation.getCourt().getHourPrice() * (newReservation.getDuration().toMinutes() / 60.00) *
                (newReservation.getReservationType() == ReservationType.Singles ? 1 : 1.5);
    }

    @Override
    public void updateReservation(Reservation reservation) {
        reservationRepository.updateReservation(reservation);
    }

    @Override
    public void deleteReservation(int id) {
        reservationRepository.deleteReservation(id);
    }
}
