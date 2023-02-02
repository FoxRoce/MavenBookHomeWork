package model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

enum Gender {
    male, female
}

@Entity
@Table (name = "VN_Authors")
public class Author {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "aid")
    private int id;

    @Column
    private String name;

    @Column
    private LocalDate dob;

    @Column
    private Gender gender;

    @OneToMany (mappedBy = "author")
    private List<Book> bookList;




}
