package cz.models;

import cz.enums.ReservationType;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "courtId", referencedColumnName = "id")
    private Court court;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @Column(columnDefinition = "VARCHAR(255)")
    private LocalDateTime startDateTime;

    private Duration duration;

    private ReservationType reservationType;

    public Reservation(Court court, User user, LocalDateTime startDateTime, Duration duration,
                       ReservationType reservationType) {
        this.court = court;
        this.user = user;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.reservationType = reservationType;
    }

    public Reservation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (this.id != null) {
            throw new IllegalArgumentException("Id can't be changed");
        }
        this.id = id;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDate) {
        this.startDateTime = startDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationType reservationType) {
        this.reservationType = reservationType;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", court=" + court +
                ", user=" + user +
                ", startDate=" + startDateTime +
                ", duration=" + duration +
                ", reservationType=" + reservationType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(court, that.court) && Objects.equals(user, that.user) && Objects.equals(startDateTime, that.startDateTime) && Objects.equals(duration, that.duration) && reservationType == that.reservationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, court, user, startDateTime, duration, reservationType);
    }
}
