package cz.payload;

import cz.enums.ReservationType;

import java.sql.Time;
import java.util.Date;

public class ReservationPostRequest {

    private final Integer courtId;

    private final String phoneNumber;

    private final String userName;

    private final Date startDate;

    private final Time startTime;

    private final int duration;

    private final ReservationType reservationType;

    public ReservationPostRequest(Integer courtId, String phoneNumber, String userName, Date startDate, Time startTime,
                                  int duration, ReservationType reservationType) {
        this.courtId = courtId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.reservationType = reservationType;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }
}
