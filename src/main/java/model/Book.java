package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table (name = "VN_Books")
public class Book {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "bid")
    private Long id;

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

//    @OneToMany(mappedBy = "book")
//    private List<BookToStore> storeList;

    @ManyToMany
    @JoinTable(
            name = "VN_Books_TO_VN_Stores",
            joinColumns = @JoinColumn(name = "book_bid"),
            inverseJoinColumns = @JoinColumn(name = "store_sid")
    )
    private List<Store> storeList;


    public Book(){}

    public Book(Long id, String isbn, LocalDate dob, int edition, String title, Author author, List<Store> storeList) {
        this.id = id;
        this.isbn = isbn;
        this.dob = dob;
        this.edition = edition;
        this.title = title;
        this.author = author;
        this.storeList = storeList;
    }


    @Override
    public String toString() {
        String authorName = "";
        try{
            authorName = author.getName();
        } catch (NullPointerException e){
            authorName = "Anonym";
        }
        String text ="'n(id: "+id+") "+ "Book" +
                "\n\tISBN='" + isbn + '\'' +
                "\n\tDate of Publish=" + dob +
                "\n\tEdition=" + edition +
                "\n\tTitle='" + title + '\'' +
                "\n\tActive=" + (active ? "Yes" : "No") +
                "\n\tAuthor=" + authorName +
                "\n\tStore List= ";

        for (var store : storeList) {
            text += store.getName() + ", " + store.getAddress()+"\n";
        }
        return text;
    }

}
