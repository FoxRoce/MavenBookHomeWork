package model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table (name = "VN_Books")
public class Book {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "bid")
    private int id;

    @Column
    private String isbn;

    @Column
    private LocalDate dob;

    @Column
    private int edition;

    @Column
    private String title;

    @Column
    private boolean active = true;

    @ManyToOne
    private Author author;

    @OneToMany(mappedBy = "book")
    private List<BookToStore> storeList;


    //    ---------------------------------------------------------------------


    public Book(){}

    public Book(int id, String isbn, LocalDate dob, int edition, String title, Author author, List<BookToStore> storeList) {
        this.id = id;
        this.isbn = isbn;
        this.dob = dob;
        this.edition = edition;
        this.title = title;
        this.author = author;
        this.storeList = storeList;
    }


    //    ---------------------------------------------------------------------


    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }
    public LocalDate getDob() {
        return dob;
    }

    public int getEdition() {
        return edition;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActive() {
        return active;
    }

    public Author getAuthor() {
        return author;
    }

    public List<BookToStore> getStoreList() {
        return storeList;
    }


    //    ---------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setStoreList(List<BookToStore> storeList) {
        this.storeList = storeList;
    }


    //    ---------------------------------------------------------------------


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", dob=" + dob +
                ", edition=" + edition +
                ", title='" + title + '\'' +
                ", active=" + active +
                ", author=" + author +
                ", storeList=" + storeList +
                '}';
    }

    //    ---------------------------------------------------------------------
}
