package cz.payload;

import cz.enums.ReservationType;

public class ReservationPostRequest {

    private final Integer courtId;

    private final String phoneNumber;

    private final String userName;

    private final String startDateTime;

    private final long duration;

    private final ReservationType reservationType;

    public ReservationPostRequest(Integer courtId, String phoneNumber, String userName, String startDate, long duration, ReservationType reservationType) {
        this.courtId = courtId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.startDateTime = startDate;
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

    public String getStartDate() {
        return startDateTime;
    }

    public long getDuration() {
        return duration;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }
}
