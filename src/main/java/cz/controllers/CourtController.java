package cz.controllers;

import cz.models.Court;
import cz.payload.CourtPostRequest;
import cz.services.CourtService;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    @Autowired
    private CourtService courtService;

    @GetMapping("/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(courtService.getCourtById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Court>> getAllCourts() {
        return new ResponseEntity<>(courtService.getAllCourts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Court> addCourt(@RequestBody CourtPostRequest newCourt) {
        return new ResponseEntity<>(courtService.addCourt(newCourt), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Court> updateCourt(@RequestBody Court court) {
        courtService.updateCourt(court);
        return new ResponseEntity<>(court, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteCourt(@PathVariable(name = "id") Integer id) {
        courtService.deleteCourt(id);
    }
}
