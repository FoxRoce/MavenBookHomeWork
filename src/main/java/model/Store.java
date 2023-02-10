package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table (name = "VN_Stores")
public class Store {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "sid")
    private Long id;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String owner;

    @Column
    private boolean active = false;

//    @OneToMany(mappedBy = "store")
//    private List<BookToStore> bookList;

    @ManyToMany(mappedBy = "storeList")
    private List<Book> bookList;


    public Store(){}

    public Store(Long id, String name, String address, String owner, List<Book> bookList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.owner = owner;
        this.bookList = bookList;
    }


    @Override
    public String toString() {
        String text ="\n(id: "+id+") "+ "Store" +
                "\n\tName='" + name + '\'' +
                "\n\tAddress='" + address + '\'' +
                "\n\tOwner='" + owner + '\'' +
                "\n\tActive=" + (active ? "Yes" : "No");
//                "\n\tBook List= ";
//        for (var book : bookList) {
//            text += book.getTitle() + "\n";
        return text;

    }
}
