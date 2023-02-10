package model;

import jakarta.persistence.*;

@Entity
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


    public Book getBook() {
        return book;
    }

    public Store getStore() {
        return store;
    }

    public int getAmount() {
        return amount;
    }


    public void setBook(Book book) {
        this.book = book;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setAmount(int amount) {
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
