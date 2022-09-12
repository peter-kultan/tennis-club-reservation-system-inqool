package cz.repositories.impl;

import cz.enums.ReservationType;
import cz.exceptions.DataAccessException;
import cz.models.Reservation;
import cz.repositories.CourtRepository;
import cz.repositories.ReservationRepository;
import cz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public Reservation findReservationById(int id) {
        try (var conn = DriverManager.getConnection(url, username, password);
             var st = conn.prepareStatement("SELECT id, court_id, user_id, start_date_time, duration, reservation_type FROM reservation WHERE id = ?")) {
            st.setInt(1, id);
            var rs = st.executeQuery();
            if (rs.next()) {
                var reservation = new Reservation(courtRepository.findCourtById(rs.getInt("user_id")),
                        userRepository.findUserById(rs.getInt("user_id")),
                        LocalDateTime.parse(rs.getString("start_date_time")),
                        Duration.of(rs.getInt("duration"), ChronoUnit.MINUTES),
                        rs.getInt("reservation_type") == 0 ? ReservationType.Singles : ReservationType.Doubles);
                reservation.setId(rs.getInt("id"));
                if (rs.next()) {
                    throw new DataAccessException("Multiple reservations with id: " + id + " found");
                }
                return reservation;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load reservation with id: " + id, ex);
        }
        return null;
    }

    @Override
    public Collection<Reservation> findAllReservations() {
        try (var conn = DriverManager.getConnection(url, username, password);
             var st = conn.prepareStatement("SELECT id, court_id, user_id, start_date_time," +
                     " duration, reservation_type FROM reservation")) {
            List<Reservation> reservations = new ArrayList<>();
            var rs = st.executeQuery();
            while (rs.next()) {
                var reservation = new Reservation(courtRepository.findCourtById(rs.getInt("user_id")),
                        userRepository.findUserById(rs.getInt("user_id")),
                        LocalDateTime.parse(rs.getString("start_date_time")),
                        Duration.of(rs.getInt("duration"), ChronoUnit.MINUTES),
                        rs.getInt("reservation_type") == 0 ? ReservationType.Singles : ReservationType.Doubles);
                reservation.setId(rs.getInt("id"));
                reservations.add(reservation);
            }
            return reservations;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all reservations", ex);
        }
    }

    @Override
    public void createReservation(Reservation reservation) {
        if (reservation.getId() != null) {
            throw new IllegalArgumentException("Reservation already has ID: " + reservation);
        }
        try (var conn = DriverManager.getConnection(url, username, password);
             var st = conn.prepareStatement("INSERT INTO reservation(court_id, user_id, start_date_time, " +
                             "duration, reservation_type) VALUES (?, ?, ?, ?, ?) ",
                     Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, reservation.getCourt().getId());
            st.setInt(2, reservation.getUser().getId());
            st.setString(3, reservation.getStartDateTime().toString());
            st.setLong(4, reservation.getDuration().toMinutes());
            st.setInt(5, reservation.getReservationType() == ReservationType.Singles ? 1 : 2);
            st.executeUpdate();

            try (var rs = st.getGeneratedKeys()) {
                if (rs.getMetaData().getColumnCount() != 1) {
                    throw new DataAccessException("Failed to fetch generated key: compound key returned for reservation: " + reservation);
                }
                if (rs.next()) {
                    reservation.setId(rs.getInt(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for reservation: " + reservation);
                }
                if (rs.next()) {
                    throw new DataAccessException("Failed to fetch generated key: multiple keys returned for reservation: " + reservation);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store reservation: " + reservation, ex);
        }
    }

    @Override
    public void updateReservation(Reservation reservation) {
        if (reservation.getId() == null) {
            throw new IllegalArgumentException("reservation has null ID: " + reservation);
        }
        try (var conn = DriverManager.getConnection(url, username, password);
             var st = conn.prepareStatement("UPDATE reservation SET court_id = ?, user_id = ?," +
                     " start_date_time = ?, duration = ?, reservation_type = ? WHERE id = ?")) {
            st.setInt(1, reservation.getCourt().getId());
            st.setInt(2, reservation.getUser().getId());
            st.setString(3, reservation.getStartDateTime().toString());
            st.setLong(4, reservation.getDuration().toMinutes());
            st.setInt(5, reservation.getReservationType() == ReservationType.Singles ? 1 : 2);
            st.setInt(6, reservation.getId());
            int rowUpdated = st.executeUpdate();
            if (rowUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing reservation: " + reservation);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update surface", ex);
        }
    }

    @Override
    public void deleteReservation(int id) {
        var reservation = findReservationById(id);
        if (reservation == null) {
            throw new IllegalStateException("Reservation with id: " + id + " does not exist");
        }
        try (var conn = DriverManager.getConnection(url, username, password);
             var st = conn.prepareStatement("DELETE FROM reservation WHERE id = ?")) {
            st.setInt(1, id);
            int rowDeleted = st.executeUpdate();
            if (rowDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing reservation: " + reservation);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete reservation: " + reservation, ex);
        }
    }
}
