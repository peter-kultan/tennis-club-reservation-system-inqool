package cz.services.impl;

import cz.models.Surface;
import cz.repositories.SurfaceRepository;
import cz.services.SurfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SurfaceServiceImpl implements SurfaceService {

    @Autowired
    private SurfaceRepository surfaceRepository;


    @Override
    public Collection<Surface> getAllSurfaces() {
        return surfaceRepository.findAllSurfaces();
    }

    @Override
    public Surface getSurfaceById(int id) {
        return surfaceRepository.findSurfaceById(id);
    }

    @Override
    public void addSurface(Surface surface) {
        surfaceRepository.createSurface(surface);
    }

    @Override
    public void updateSurface(Surface surface) {
        surfaceRepository.updateSurface(surface);
    }

    @Override
    public void deleteSurface(int id) {

    }
}
