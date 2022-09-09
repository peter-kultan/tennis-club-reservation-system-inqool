package cz.repositories;

import cz.models.Court;

import java.util.Collection;

public interface CourtRepository {

    Court findCourtById(int id);

    Collection<Court> findAllCourts();

    void createCourt(Court court);

    void updateCourt(Court court);

    void deleteCourt(int id);
}
