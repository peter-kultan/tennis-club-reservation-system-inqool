package cz.controllers;

import cz.models.Court;
import cz.models.Surface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    @GetMapping("/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(new Court(15, new Surface(""), 2.25), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Court> getAllCourts() {
        return new ResponseEntity<>(new Court(new Surface(""), 2.40), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addCourt(@RequestBody Court newCourt) {
        return new ResponseEntity<>(newCourt, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Court> updateCourt(@PathVariable(name = "id") Integer id,
                                             @RequestBody Court court) {
        return new ResponseEntity<>(new Court(new Surface("Sand"), 2.25), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCourt(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
