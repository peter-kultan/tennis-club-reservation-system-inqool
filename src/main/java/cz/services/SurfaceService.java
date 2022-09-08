package cz.services;

import cz.models.Surface;

import java.util.Collection;

public interface SurfaceService {
    Collection<Surface> getAllSurfaces();

    Surface getSurfaceById(int id);

    void addSurface(Surface surface);

    void updateSurface(Surface surface);

    void deleteSurface(int id);
}
