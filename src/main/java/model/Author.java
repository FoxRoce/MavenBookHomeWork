package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
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
    private boolean gender;

    @OneToMany (mappedBy = "author")
    private List<Book> bookList;


    public Author(){}

    public Author(int id, String name, LocalDate dob, boolean gender, List<Book> bookList) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.bookList = bookList;
    }

    public boolean getGender() {
        return gender;
    }


    @Override
    public String toString() {
        String text ="\n(id: "+id+") "+ "Author" +
                    "\n\tName='" + name + '\'' +
                    "\n\tDate of Birth=" + dob +
                    "\n\tGender=" + (gender ? "Male" : "Female");
//                    "\n\tBook List= ";
//        for (var book : bookList) {
//            text += book.getTitle() + "\n";
        return text;
    }

}
