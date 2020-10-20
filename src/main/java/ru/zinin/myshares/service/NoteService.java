package ru.zinin.myshares.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.Note;
import ru.zinin.myshares.repo.NoteRepo;

@Service
@Slf4j
public class NoteService {

    private final TokenFactory tokenFactory;
    private final NoteRepo noteRepo;

    public NoteService(TokenFactory tokenFactory, NoteRepo noteRepo) {
        this.tokenFactory = tokenFactory;
        this.noteRepo = noteRepo;
    }

    public ResponseEntity<?> getNotes() {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            log.info("Пользователь с id "+tokenFactory.getUserId()+" получил все записи");
            return ResponseEntity.ok(noteRepo.getAllByUserId(tokenFactory.getUserId()));
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> createNote(Note note) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            note.setCreationTime(System.currentTimeMillis());
            note.setUserId(tokenFactory.getUserId());
            Note savedNote = noteRepo.save(note);
            log.info("Пользователь с id "+tokenFactory.getUserId()+" добавил запись с id " +savedNote.getId());
            return ResponseEntity.ok(savedNote);
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> getNote(Long id) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            Note noteFromDb = noteRepo.getById(id);
            if (noteFromDb != null && noteFromDb.getUserId() == tokenFactory.getUserId()) {
                log.info("Пользователь с id " + tokenFactory.getUserId() + " получил запись с id " + id);
                return ResponseEntity.ok(noteFromDb);
            } else {
                return new ResponseEntity<>(
                        "invalid id note",
                        HttpStatus.FORBIDDEN
                );
            }
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> deleteNote(Long id) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            Note noteFromDb = noteRepo.getById(id);
            if (noteFromDb != null && noteFromDb.getUserId() == tokenFactory.getUserId()) {
                noteRepo.delete(noteFromDb);
                log.info("Пользователь с id " + tokenFactory.getUserId() + " удалил запись с id " + id);
                return ResponseEntity.ok("DELETED");
            } else {
                return new ResponseEntity<>(
                        "invalid id note",
                        HttpStatus.FORBIDDEN
                );
            }
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> editNote(Note note) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            Note noteFromDb = noteRepo.getById(note.getId());
            if (noteFromDb != null && noteFromDb.getUserId() == tokenFactory.getUserId()) {
                noteFromDb.setTitle(note.getTitle());
                noteFromDb.setText(note.getText());
                Note saveNote = noteRepo.save(noteFromDb);
                log.info("Пользователь с id " + tokenFactory.getUserId() + " изменил запись с id " + note.getId());
                return ResponseEntity.ok(saveNote);
            } else {
                return new ResponseEntity<>(
                        "invalid id note",
                        HttpStatus.FORBIDDEN
                );
            }
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
