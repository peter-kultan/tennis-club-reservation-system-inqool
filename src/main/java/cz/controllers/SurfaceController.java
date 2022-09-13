package cz.controllers;

import cz.models.Surface;
import cz.services.SurfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * The surface controller for spring boot api
 *
 * @author Peter Kultán
 */
@RestController
@RequestMapping("/api/surfaces")
public class SurfaceController {

    @Autowired
    private SurfaceService surfaceService;

    /**
     * Returns a surface searched by id
     *
     * @param id an id of searched surface
     * @return a surface searched by id with HttpStatus
     */
    @GetMapping("/{id}")
    public ResponseEntity<Surface> getSurfaceById(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(surfaceService.getSurfaceById(id), HttpStatus.OK);
    }

    /**
     * Returns all surfaces
     *
     * @return all surfaces stored in system
     */
    @GetMapping
    public ResponseEntity<Collection<Surface>> getAllSurfaces() {
        return new ResponseEntity<>(surfaceService.getAllSurfaces(), HttpStatus.OK);
    }

    /**
     *  Add new surface to system
     *
     * @param newSurface surface to be added to system
     * @return added surface and HttpStatus
     */
    @PostMapping
    public ResponseEntity<Surface> addSurface(@RequestBody Surface newSurface) {
        surfaceService.addSurface(newSurface);
        return new ResponseEntity<>(newSurface, HttpStatus.OK);
    }

    /**
     * Update existing surface
     *
     * @param surface to be updated, with new values
     * @return updated surface and HttpStatus
     */
    @PutMapping
    public ResponseEntity<Surface> updateSurface(@RequestBody Surface surface) {
        surfaceService.updateSurface(surface);
        return new ResponseEntity<>(surface, HttpStatus.OK);
    }

    /**
     * Delete surface specified with id
     *
     * @param id of surface to be delete from the surface
     */
    @DeleteMapping("/{id}")
    public void deleteSurface(@PathVariable(name = "id") Integer id) {
        surfaceService.deleteSurface(id);
    }
}
