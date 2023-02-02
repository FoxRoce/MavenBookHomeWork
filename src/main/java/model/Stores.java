package model;

import jakarta.persistence.*;

@Entity
@Table
public class Stores {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "sid")
    int id;

    @Column
    String address;

    @Column
    String owner;

}
