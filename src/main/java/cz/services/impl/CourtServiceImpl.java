package cz.services.impl;

import cz.models.Court;
import cz.payload.CourtPostRequest;
import cz.repositories.CourtRepository;
import cz.repositories.SurfaceRepository;
import cz.services.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CourtServiceImpl implements CourtService {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private SurfaceRepository surfaceRepository;

    @Override
    public Collection<Court> getAllCourts() {
        return courtRepository.findAllCourts();
    }

    @Override
    public Court getCourtById(int id) {
        return courtRepository.findCourtById(id);
    }

    @Override
    public Court addCourt(CourtPostRequest court) {
        var surface = surfaceRepository.findSurfaceById(court.getSurfaceId());
        if (surface == null) {
            throw new IllegalArgumentException("Trying create court with non-existing surface");
        }
        var newCourt = new Court(surface, court.getHourPrice());
        courtRepository.createCourt(newCourt);
        return newCourt;
    }

    @Override
    public void updateCourt(Court court) {
        courtRepository.updateCourt(court);
    }

    @Override
    public void deleteCourt(int id) {
        courtRepository.deleteCourt(id);
    }
}
