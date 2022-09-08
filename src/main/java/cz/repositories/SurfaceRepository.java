package cz.repositories;

import cz.models.Surface;

import java.util.Collection;

public interface SurfaceRepository {

    Surface findSurfaceById(int id);

    Collection<Surface> findAllSurfaces();

    void createSurface(Surface surface);

    void updateSurface(Surface surface);

    void deleteSurface(int id);
}
