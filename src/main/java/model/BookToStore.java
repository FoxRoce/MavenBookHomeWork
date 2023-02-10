package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table (name = "VN_Books_TO_VN_Stores")
public class BookToStore {

//    @Id
//    @ManyToOne
//    private Book book;
//
//    @Id
//    @ManyToOne
//    private Store store;

    @EmbeddedId
    private BookToStoreId id = new BookToStoreId();

    @ManyToOne
    @MapsId ("book_bid")
    private Book book;

    @ManyToOne
    @MapsId("store_sid")
    private Store store;

    @Column
    private int amount;


    public BookToStore(){}

    public BookToStore(Book book, Store store, int amount) {
        this.book = book;
        this.store = store;
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "\nBookToStore" +
                "\n\tbook=" + book.getTitle() +
                "\n\tstore=" + store.getName() + ", " + store.getAddress() +
                "\n\tamount=" + amount;
    }
}
