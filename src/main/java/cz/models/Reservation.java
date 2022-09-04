package cz.models;

import java.sql.Time;
import java.time.Duration;
import java.util.Date;

public class Reservation {

    private int id;
    private Court court;
    private User user;
    private Date startDate;
    private Time startTime;
    private Duration duration;

    public Reservation(int id, Court court, User user, Date startDate, Time startTime, Duration duration) {
        this.id = id;
        this.court = court;
        this.user = user;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Reservation(Court court, User user, Date startDate, Time startTime, Duration duration) {
        this.court = court;
        this.user = user;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", court=" + court +
                ", user=" + user +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
