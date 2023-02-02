package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "VN_Stores")
public class Store {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "sid")
    int id;

    @Column
    String address;

    @Column
    String owner;

    @ManyToMany(mappedBy = "storeList")
    private List<Book> bookList;


}
