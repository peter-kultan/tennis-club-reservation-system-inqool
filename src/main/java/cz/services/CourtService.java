package cz.services;

import cz.models.Court;
import cz.payload.CourtPostRequest;

import java.util.Collection;

public interface CourtService {
    Collection<Court> getAllCourts();

    Court getCourtById(int id);

    Court addCourt(CourtPostRequest court);

    void updateCourt(Court court);

    void deleteCourt(int id);
}
