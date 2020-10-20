package ru.zinin.myshares.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.model.Note;
import ru.zinin.myshares.service.NoteService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/note")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> getNotes() {
        return noteService.getNotes();
    }

    @GetMapping("{id}")
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> getNote(@PathVariable Long id) {
        return noteService.getNote(id);
    }

    @PostMapping
    @CrossOrigin(methods = RequestMethod.POST)
    public ResponseEntity<?> createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @DeleteMapping
    @CrossOrigin(methods = RequestMethod.DELETE)
    public ResponseEntity<?> deleteNote(@RequestParam Long id) {
        return noteService.deleteNote(id);
    }

    @PutMapping
    @CrossOrigin(methods = RequestMethod.PUT)
    public ResponseEntity<?> editNote(@RequestBody Note note) {
        return noteService.editNote(note);
    }
}
