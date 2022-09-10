package cz.models;

import cz.enums.ReservationType;

import javax.persistence.*;
import java.time.Duration;
import java.util.Date;

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

    @Column(columnDefinition = "long")
    private Date startDate;

    private Duration duration;

    private ReservationType reservationType;

    public Reservation(Court court, User user, Date startDate, Duration duration,
                       ReservationType reservationType) {
        this.court = court;
        this.user = user;
        this.startDate = startDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
                ", startDate=" + startDate +
                ", duration=" + duration +
                ", reservationType=" + reservationType +
                '}';
    }
}
