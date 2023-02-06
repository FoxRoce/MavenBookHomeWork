package main;

import model.*;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.time.LocalDate;
import java.util.Map;

public class Controller implements AutoCloseable {

    private HibernateContext model = new HibernateContext();

    @Override
    public void close() throws Exception {
        model.close();
    }

//    public void createSession() {
//        Session session = model.getSession();
//        session.close();
//
////        Transaction t = session.beginTransaction();
////        session.getTransaction().commit();
//    }

    public void printBooksWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void printAuthorsWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM Author", Author.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void printStoresWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + ", " + thing.getAddress());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public String getBookById(int id) {
        Session session = model.getSession();

        session.beginTransaction();
        Book book = session.find(Book.class, id);
        session.close();

        if (book == null) {
            return "No such ID";
        } else {
            return book.getId() +" - "+book.getTitle()+" ("+book.getIsbn()+")" + ", Author: "+book.getAuthor().getName();
        }
    }

    public String getAuthorById(int id) {
        Session session = model.getSession();

        session.beginTransaction();
        Author author = session.find(Author.class, id);
        session.close();

        if (author == null) {
            return "No such ID";
        } else {
            return author.getId() + " - " + author.getName();
        }
    }

    public String getStoreById(int id) {
        Session session = model.getSession();

        session.beginTransaction();
        Store store = session.find(Store.class, id);
        session.close();

        if (store == null) {
            return "No such ID";
        } else {
            return store.getId() + " - " + store.getName() +", "+ store.getAddress();
        }
    }

    public void addNewBook(Object[] newBook) {
        Book book = new Book();
        book.setTitle(String.valueOf(newBook[0]));
        book.setIsbn(String.valueOf(newBook[1]));
        book.setEdition((Integer) newBook[2]);

        Session session = model.getSession();
        session.beginTransaction();

        try {
            book.setDob(LocalDate.parse(String.valueOf(newBook[3])));
        } catch (Exception e) {
            System.out.println("Wrong date format\nBook Date of Publish not added");
        }

        if (!(newBook[4] instanceof Integer)) {
            Author author = addNewAuthor((Object[]) newBook[4], session);
            session.merge(author);
            book.setAuthor(author);
        } else {
            try {
                int aid = (Integer) newBook[4];
                book.setAuthor(session.find(Author.class, aid));
            } catch (Exception e) {
                System.out.println("No such ID for Author\nBook Author not added");
            }
        }

        if (newBook[5] instanceof Integer) {
            try {
                int sid = (Integer) newBook[5];
                Store store = session.find(Store.class, sid);
                BookToStore bookToStore = new BookToStore(book, store, (Integer) newBook[6]);
                session.persist(bookToStore);
            } catch (Exception e) {
                System.out.println("No such ID for Store\nBook Store not added");
            }
        }
        session.persist(book);
        session.getTransaction().commit();
        session.close();
    }
    public void addOnlyNewAuthor(Object[] newAuthor){
        Session session = model.getSession();
        session.beginTransaction();
        addNewAuthor(newAuthor,session);
        session.getTransaction().commit();
        session.close();
    }

    public Author addNewAuthor(Object[] newAuthor, Session session) {
        Author author = new Author();
        author.setName(String.valueOf(newAuthor[0]));
        try {
            author.setDob(LocalDate.parse(String.valueOf(newAuthor[1])));
        } catch (Exception e) {
            System.out.println("Wrong date format\nAuthor Date of Birth not added");
        }
        if (newAuthor[2].equals("m")){
            author.setGender(true);
        } else if (newAuthor[2].equals("f")) {
            author.setGender(false);
        } else {
            System.out.println("Wrong gender format\nAuthor Gender not added");
        }
//        Session session = model.getSession();
//        session.beginTransaction();
        session.persist(author);
//        session.getTransaction().commit();
//        session.close();
        return author;
    }

    public void addNewStore(Object[] newStore) {
        Store store = new Store();
        store.setName(String.valueOf(newStore[0]));
        store.setAddress(String.valueOf(newStore[1]));
        store.setOwner(String.valueOf(newStore[2]));

        if (newStore[3].equals("y")) {
            store.setActive(true);
        }
        Session session = model.getSession();
        session.beginTransaction();
        session.persist(store);
        session.getTransaction().commit();
        session.close();
    }

    public void addBooksToStore(int sid, Map<Integer, Integer> bookList) {
        Session session = model.getSession();
        session.beginTransaction();
        Store store;
        try {
            store = session.find(Store.class, sid);
        } catch (Exception e) {
            System.out.println("No such Store ID\nNo Books added");
            return;
        }

        for (int bookID : bookList.keySet()) {
            try {
                Book book = session.find(Book.class, bookID);
                BookToStore bookToStore = null;
                try {
                    bookToStore = new BookToStore(book, store, bookList.get(bookID));
                } catch (PersistentObjectException e){
                   SelectionQuery<BookToStore> query = session.createSelectionQuery("FROM BookToStore b where b.book_bid = ?1 and b.store_sid = ?2", BookToStore.class);
                    query.setParameter(1,bookID);
                    query.setParameter(2,sid);
                    for (var thing : query.list()) {
                        thing.setAmount(thing.getAmount() + bookList.get(bookID)); 
                    }
                }
                session.persist(bookToStore);
            } catch (Exception e) {
                System.out.println("Book ID: " + bookID + ", does not exist. Not added");
            }
        }
        session.getTransaction().commit();
        session.close();
    }

    public void removeAuthor(int id) {
        Session session = model.getSession();
        session.beginTransaction();

        Author author;
        try {
            author = session.find(Author.class, id);
        } catch (Exception e) {
            System.out.println("No such ID\nAuthor not removed");
            return;
        }
        session.remove(author);
        session.getTransaction().commit();
        session.close();
    }

    public void removeStore(int id) {
        Session session = model.getSession();
        session.beginTransaction();

        Store store;
        try {
            store = session.find(Store.class, id);
        } catch (Exception e) {
            System.out.println("No such ID\nStore not removed");
            return;
        }
        session.remove(store);
        session.getTransaction().commit();
        session.close();
    }

    public void removeBooksFromStore(int sid, int[] bookIds) {
        Session session = model.getSession();
        session.beginTransaction();

        Store store;
        try {
            store = session.find(Store.class, sid);
        } catch (Exception e) {
            System.out.println("No such ID\nStore not modified");
            return;
        }

        for (int bookId : bookIds) {
            try {
                session.find(Book.class, bookId);

                String line = "Delete From BookToStore b where b.store_sid =?1 and b.book_bid =?2";
                SelectionQuery<?> query = session.createSelectionQuery(line);
                query.setParameter(1, sid);
                query.setParameter(2, bookId);

                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("No such Book ID\nBook not removed from store");
            }
        }
        session.close();
    }

    public void printStoreBookAmount() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<BookToStore> query = session.createSelectionQuery("FROM BookToStore", BookToStore.class);

        for (var thing : query.list()) {
            System.out.println(thing.getStore().getName() + " - " + thing.getBook().getTitle() + ", Amount: " + thing.getAmount());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void print3StoreWithBooksBellow10(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<BookToStore> query = session.createSelectionQuery("FROM BookToStore b where b.amount < 10 order by b.amount limit 3", BookToStore.class);

        for (var thing : query.list()) {
            System.out.println(thing.getStore().getName() + " - " + thing.getBook().getTitle() + ", Amount: " + thing.getAmount());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public String getBookByISBN(String isbn) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book b where b.isbn =?1", Book.class);
        query.setParameter(1,isbn);

        String temp = "";
        for (Book book : query.list()) {
            temp += book.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }


    public String getBookByTittle(String title) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book b where b.title like ?1", Book.class);
        query.setParameter(1,"%"+title+"%");

        String temp = "";
        for (Book book : query.list()) {
            temp += book.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }

    public String getBookByAuthor(int aid) {
        Session session = model.getSession();
        session.beginTransaction();

        try {
            session.find(Author.class,aid);
        } catch (Exception e){
            return "No such Author ID\nReturning to search menu...";
        }

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book b where b.author_aid = ?1", Book.class);
        query.setParameter(1,aid);

        String temp = "";
        for (Book book : query.list()) {
            temp += book.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }

    public String getAuthorByName(String name) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM Author a where a.name like ?1", Author.class);
        query.setParameter(1,"%"+name+"%");

        String temp = "";
        for (Author author : query.list()) {
            temp += author.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }

    public String getAuthorByDob(String dob) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM Author a where a.dob like ?1", Author.class);
        query.setParameter(1,"%"+dob+"%");

        String temp = "";
        for (Author author : query.list()) {
            temp += author.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }

    public String getAuthorByGender(boolean gender) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM Author a where a.gender = ?1", Author.class);
        query.setParameter(1,gender);

        String temp = "";
        for (Author author : query.list()) {
            temp += author.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }

    public String getStoreByAddress(String address) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store s where s.address like ?1", Store.class);
        query.setParameter(1,"%"+address+"%");

        String temp = "";
        for (Store store : query.list()) {
            temp += store.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }

    public String getStoreByOwner(String owner) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store s where s.owner like ?1", Store.class);
        query.setParameter(1,"%"+owner+"%");

        String temp = "";
        for (Store store : query.list()) {
            temp += store.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }

    public String getStoreByBookId(int bid) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<BookToStore> query = session.createSelectionQuery("FROM BookToStore b where b.book_bid = ?1", BookToStore.class);
        query.setParameter(1,bid);

        String temp = "";
        for (BookToStore bookToStore : query.list()) {
            temp += bookToStore.toString() + "\n";
        }
//        session.getTransaction().commit();
        session.close();
        return temp;
    }

    public void modifyBook(int bid, Object[] modifiedBook) {
        Session session = model.getSession();
        session.beginTransaction();

        Book book = session.find(Book.class,bid);

        if ( modifiedBook[0] != ""){
            book.setIsbn(String.valueOf(modifiedBook[0]));
        }

        try {
            if ( modifiedBook[1] != ""){
                book.setDob(LocalDate.parse(String.valueOf(modifiedBook[1])));
            }
        } catch (Exception e){
            System.out.println("Wrong Date format\nBook Date of publish did not changed");
        }

        if ( (modifiedBook[2] != "")){
            book.setEdition((Integer) modifiedBook[2]);
        }

        if ( modifiedBook[3] != ""){
            book.setTitle(String.valueOf(modifiedBook[3]));
        }

        if ( modifiedBook[4] != ""){
            if(modifiedBook[4] instanceof Integer){
                try {
                    book.setAuthor(session.find(Author.class,(Integer) modifiedBook[4]));
                } catch (Exception e){
                    System.out.println("No Such Author ID\nBook Author wasn't modified");
                }

            } else {
                Author author = addNewAuthor(new Object[]{modifiedBook[4]},session);
                session.merge(author);
                book.setAuthor(author);
            }
        }
        session.persist(book);
        session.getTransaction().commit();
        session.close();
    }

    public void printActiveBookWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book b where b.active = true", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle() + " - Active: " + thing.isActive());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void printInactiveBookWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book b where b.active = false", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle() + " - Active: " + thing.isActive());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void modifyBookActiveness(int bid) {
        Session session = model.getSession();
        session.beginTransaction();

        Book book = session.find(Book.class,bid);

        book.setActive(!book.isActive());

        session.getTransaction().commit();
        session.close();
    }

    public void modifyAuthor(int aid, Object[] modifiedAuthor) {
        Session session = model.getSession();
        session.beginTransaction();

        Author author = session.find(Author.class,aid);

        if ( modifiedAuthor[0] != ""){
            author.setName(String.valueOf(modifiedAuthor[0]));
        }

        try {
            if ( modifiedAuthor[1] != ""){
                author.setDob(LocalDate.parse(String.valueOf(modifiedAuthor[1])));
            }
        } catch (Exception e){
            System.out.println("Wrong Date format\nAuthor Date of Birth did not changed");
        }

        if ( modifiedAuthor[2] != "") {
            if (modifiedAuthor[2].equals("m")){
                author.setGender(true);
            } else if (modifiedAuthor[2].equals("f")) {
                author.setGender(false);
            } else {
                System.out.println("Wrong gender format\nAuthor Gender did not changed");
            }
        }
        session.persist(author);
        session.getTransaction().commit();
        session.close();
    }

    public void modifyStore(int sid, Object[] modifiedStore) {
        Session session = model.getSession();
        session.beginTransaction();

        Store store = session.find(Store.class,sid);

        if ( modifiedStore[0] != ""){
            store.setName(String.valueOf(modifiedStore[0]));
        }

        if ( modifiedStore[1] != ""){
            store.setAddress(String.valueOf(modifiedStore[1]));
        }

        if ( modifiedStore[2] != ""){
            store.setOwner(String.valueOf(modifiedStore[2]));
        }

        session.persist(store);
        session.getTransaction().commit();
        session.close();
    }

    public void printActiveStoreWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store s where s.active = true", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + " - Active: " + thing.isActive());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void printInactiveStoreWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store s where s.active = false", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + " - Active: " + thing.isActive());
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void modifyStoreActiveness(int sid) {
        Session session = model.getSession();
        session.beginTransaction();

        Store store = session.find(Store.class,sid);

        store.setActive(!store.isActive());

        session.getTransaction().commit();
        session.close();
    }

    public void printStores() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store s where s.active = true", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + ", " + thing.getAddress() +
                    ", Owner: " + thing.getOwner() + "Books: ");
            for (var book : thing.getBookList()) {
                System.out.print(book.getTitle() + "\n");
            }
//            session.getTransaction().commit();
            session.close();
        }
    }

    public void printAllStores() {
            Session session = model.getSession();
            session.beginTransaction();

            SelectionQuery<Store> query = session.createSelectionQuery("FROM Store", Store.class);

            for (var thing : query.list()) {
                System.out.println(thing.getId() + " - " + thing.getName() + ", " + thing.getAddress() +
                        ", Owner: " + thing.getOwner() + "\nBooks: ");
                for (var book : thing.getBookList()) {
                    System.out.print(book.getTitle() + "\n");
                }
//                session.getTransaction().commit();
                session.close();
            }
        }

    public void printAuthors(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM Author", Author.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName()+ " " + (thing.getGender() ? "male" : "female") + ", Born: "+ thing.getDob() +
                    "\nBooks: ");
            for (var book : thing.getBookList()) {
                System.out.print(book.getTitle() + "\n");
            }
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void printBooks(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book b where b.active = true", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle() + ", Author: "+ thing.getAuthor().getName() +
                    "\nEdition: "+thing.getEdition()+ ", Isbn: "+thing.getIsbn() + ", Publish date: "+thing.getDob() +
                    "\nStores: ");
            for (var store : thing.getStoreList()) {
                System.out.print(store.getName() + ", " + store.getAddress() +"\n");
            }
        }
//        session.getTransaction().commit();
        session.close();
    }

    public void printAllBooks(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle() + ", Author: "+ thing.getAuthor().getName() + ", Active: " +
                    thing.isActive() +
                    "\nEdition: "+thing.getEdition()+ ", Isbn: "+thing.getIsbn() + ", Publish date: "+thing.getDob() +
                    "\nStores: ");
            for (var store : thing.getStoreList()) {
                System.out.print(store.getName() + ", " + store.getAddress() +"\n");
            }
        }
//        session.getTransaction().commit();
        session.close();
    }
}