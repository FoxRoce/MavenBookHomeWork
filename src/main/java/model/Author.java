package model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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

    //    ---------------------------------------------------------------------

    public Author(){}

    public Author(int id, String name, LocalDate dob, Gender gender, List<Book> bookList) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.bookList = bookList;
    }

    //    ---------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Gender getGender() {
        return gender;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    //    ---------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    //    ---------------------------------------------------------------------

    @Override
    public String toString() {
        String text = "Author{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", dob=" + dob +
                    ", gender=" + gender +
                    ", bookList=";
        for (var book : bookList) {
            text += book.getTitle() + "\n";

        }
        return text;
    }

    //    ---------------------------------------------------------------------
}
