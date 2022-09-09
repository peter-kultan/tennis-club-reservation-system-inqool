package cz.controllers;

import cz.models.Surface;
import cz.services.SurfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/surfaces")
public class SurfaceController {

    @Autowired
    private SurfaceService surfaceService;

    @GetMapping("/{id}")
    public ResponseEntity<Surface> getSurfaceById(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(surfaceService.getSurfaceById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Surface>> getAllSurfaces() {
        return new ResponseEntity<>(surfaceService.getAllSurfaces(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Surface> addSurface(@RequestBody Surface newSurface) {
        surfaceService.addSurface(newSurface);
        return new ResponseEntity<>(newSurface, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Surface> updateSurface(@RequestBody Surface surface) {
        surfaceService.updateSurface(surface);
        return new ResponseEntity<>(surface, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteSurface(@PathVariable(name = "id") Integer id) {
        surfaceService.deleteSurface(id);
    }
}
