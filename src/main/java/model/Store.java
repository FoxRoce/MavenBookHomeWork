package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "VN_Stores")
public class Store {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "sid")
    private int id;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String owner;

    @Column
    private boolean active = false;

    @OneToMany(mappedBy = "store")
    private List<BookToStore> bookList;




    //    ---------------------------------------------------------------------
    public Store(){}

    public Store(int id, String name, String address, String owner, List<BookToStore> bookList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.owner = owner;
        this.bookList = bookList;
    }


    //    ---------------------------------------------------------------------

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getOwner() {
        return owner;
    }

    public List<BookToStore> getBookList() {
        return bookList;
    }

    public boolean isActive() {
        return active;
    }



    //    ---------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setBookList(List<BookToStore> bookList) {
        this.bookList = bookList;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //    ---------------------------------------------------------------------

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", owner='" + owner + '\'' +
                ", active=" + active +
                ", bookList=" + bookList +
                '}';
    }

    //    ---------------------------------------------------------------------
}
