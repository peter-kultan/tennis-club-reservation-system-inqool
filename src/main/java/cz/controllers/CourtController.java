package cz.controllers;

import cz.models.Court;
import cz.payload.CourtPostRequest;
import cz.services.CourtService;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * The court controller for spring boot api
 *
 * @author Peter Kultán
 */
@RestController
@RequestMapping("/api/courts")
public class CourtController {

    @Autowired
    private CourtService courtService;

    /**
     * Returns a court searched by id
     *
     * @param id an id of searched court
     * @return a court searched by id with HttpStatus
     */
    @GetMapping("/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(courtService.getCourtById(id), HttpStatus.OK);
    }

    /**
     * Returns all stored courts
     *
     * @return all stored courts
     */
    @GetMapping
    public ResponseEntity<Collection<Court>> getAllCourts() {
        return new ResponseEntity<>(courtService.getAllCourts(), HttpStatus.OK);
    }

    /**
     * Adds new court specified by request body
     *
     * @param newCourt a court to be added to system
     * @return HttpStatus of add operation
     */
    @PostMapping
    public ResponseEntity<Court> addCourt(@RequestBody CourtPostRequest newCourt) {
        return new ResponseEntity<>(courtService.addCourt(newCourt), HttpStatus.CREATED);
    }

    /**
     * Updates an existing court specified by request body
     *
     * @param court to be updated, with new values
     * @return HttpStatus of update operation
     */
    @PutMapping
    public ResponseEntity<Court> updateCourt(@RequestBody Court court) {
        courtService.updateCourt(court);
        return new ResponseEntity<>(court, HttpStatus.OK);
    }

    /**
     * Delete court specified by id
     *
     * @param id of court to be deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCourt(@PathVariable(name = "id") Integer id) {
        courtService.deleteCourt(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
