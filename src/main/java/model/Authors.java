package model;

import jakarta.persistence.*;

import java.time.LocalDate;

enum Gender {
    male, female
}

@Entity
@Table
public class Authors {

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




}
