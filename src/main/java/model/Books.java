package model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class Books {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String isbn;

    @Column
    private LocalDate dob;

    @Column
    private int edition;

    @Column
    private String title;

    @ManyToMany
    @Column
    private int author;
}
