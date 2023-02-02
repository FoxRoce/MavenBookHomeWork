package model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table (name = "VN_Books")
public class Book {

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

    @ManyToOne
    private Author author;

    @ManyToMany(cascade = CascadeType.ALL)
    List<Store> storeList;
}
