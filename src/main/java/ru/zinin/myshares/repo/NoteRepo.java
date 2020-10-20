package ru.zinin.myshares.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zinin.myshares.model.Note;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> getAllByUserId(Long userId);

    Note getById(Long id);

    void deleteAllByUserId(Long userId);
}
