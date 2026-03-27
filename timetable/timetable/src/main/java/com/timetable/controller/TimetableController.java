package com.timetable.controller;

import com.timetable.model.Timetable;
import com.timetable.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetable")
@CrossOrigin(origins = "*")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @GetMapping("/generate")
    public ResponseEntity<?> generate() {
        try {
            List<Timetable> result = timetableService.generateTimetable();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/view")
    public ResponseEntity<List<Timetable>> view() {
        return ResponseEntity.ok(timetableService.getAllTimetable());
    }
}
