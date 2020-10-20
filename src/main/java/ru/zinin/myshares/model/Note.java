package ru.zinin.myshares.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(length = 10000)
    private String text;
    private long CreationTime;
    private Long userId;

    public Note(String title, String text, long creationTime, Long userId) {
        this.title = title;
        this.text = text;
        CreationTime = creationTime;
        this.userId = userId;
    }
}
