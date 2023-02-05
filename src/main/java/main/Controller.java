package main;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.time.LocalDate;
import java.util.Map;

public class Controller implements AutoCloseable {

    private HibernateContext model = new HibernateContext();

    @Override
    public void close() throws Exception {
        model.close();
    }

    public void createTables() {
        Session session = model.getSession();
//        Transaction t = session.beginTransaction();
//        session.getTransaction().commit();
    }

    public void printBooksWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle());
        }
        session.getTransaction().commit();
    }

    public void printAuthorsWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM Author", Author.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName());
        }
        session.getTransaction().commit();
    }

    public void printStoresWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + ", " + thing.getAddress());
        }
        session.getTransaction().commit();
    }

    public String getBookById(int id) {
        Session session = model.getSession();
        session.beginTransaction();

        Book book;
        try {
            book = session.find(Book.class, id);
        } catch (Exception e) {
            return "No such ID";
        }
        return book.toString();
    }

    public String getAuthorById(int id) {
        Session session = model.getSession();
        session.beginTransaction();

        Author author;
        try {
            author = session.find(Author.class, id);
        } catch (Exception e) {
            return "No such ID";
        }
        return author.toString();
    }

    public String getStoreById(int id) {
        Session session = model.getSession();
        session.beginTransaction();

        Store store;
        try {
            store = session.find(Store.class, id);
        } catch (Exception e) {
            return "No such ID";
        }
        return store.toString();
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
            Author author = addNewAuthor((Object[]) newBook[4]);
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
                book.getStoreList().add(new BookToStore(book, store, (Integer) newBook[6]));
            } catch (Exception e) {
                System.out.println("No such ID for Store\nBook Store not added");
            }
        }
        session.persist(book);
        session.getTransaction().commit();
    }

    public Author addNewAuthor(Object[] newAuthor) {
        Author author = new Author();
        author.setName(String.valueOf(newAuthor[0]));
        try {
            author.setDob(LocalDate.parse(String.valueOf(newAuthor[1])));
        } catch (Exception e) {
            System.out.println("Wrong date format\nAuthor Date of Birth not added");
        }
        try {
            author.setGender(Gender.valueOf(String.valueOf(newAuthor[2])));
        } catch (Exception e) {
            System.out.println("Wrong gender format\nAuthor Gender not added");
        }
        Session session = model.getSession();
        session.beginTransaction();
        session.persist(author);
        session.getTransaction().commit();
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
                store.getBookList().add(new BookToStore(book, store, bookList.get(bookID)));
            } catch (Exception e) {
                System.out.println("Book ID: " + bookID + ", does not exist. Not added");
            }
        }
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

                String line = "Delete From ? where store_sid =? and book_bid =?";
                SelectionQuery<?> query = session.createSelectionQuery(line);
                query.setParameter(1, BookToStore.class);
                query.setParameter(2, sid);
                query.setParameter(3, bookId);

                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("No such Book ID\nBook not removed from store");
            }
        }
    }

    public void printStoreBookAmount() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<BookToStore> query = session.createSelectionQuery("FROM BookToStore", BookToStore.class);

        for (var thing : query.list()) {
            System.out.println(thing.getStore().getName() + " - " + thing.getBook().getTitle() + " Amount: " + thing.getAmount());
        }
        session.getTransaction().commit();
    }

    public void print3StoreWithBooksBellow10(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<BookToStore> query = session.createSelectionQuery("FROM BookToStore order by amount limit 3", BookToStore.class);

        for (var thing : query.list()) {
            System.out.println(thing.getStore().getName() + " - " + thing.getBook().getTitle() + " Amount: " + thing.getAmount());
        }
        session.getTransaction().commit();
    }

    public String getBookByISBN(String isbn) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book where isbn =?1", Book.class);
        query.setParameter(1,isbn);
        session.getTransaction().commit();

        String temp = "";
        for (Book book : query.list()) {
            temp += book.toString() + "\n";
        }
        return temp;
    }


    public String getBookByTittle(String title) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book where title = ?1", Book.class);
        query.setParameter(1,title);
        session.getTransaction().commit();

        String temp = "";
        for (Book book : query.list()) {
            temp += book.toString() + "\n";
        }
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

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book where author_aid = ?", Book.class);
        query.setParameter(1,aid);
        session.getTransaction().commit();

        String temp = "";
        for (Book book : query.list()) {
            temp += book.toString() + "\n";
        }
        return temp;
    }

    public String getAuthorByName(String name) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM Author where name like %?1%", Author.class);
        query.setParameter(1,name);
        session.getTransaction().commit();

        String temp = "";
        for (Author author : query.list()) {
            temp += author.toString() + "\n";
        }
        return temp;
    }

    public String getAuthorByDob(String dob) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM ? where dob like %?%", Author.class);
        query.setParameter(1,Author.class);
        query.setParameter(2,dob);
        session.getTransaction().commit();

        String temp = "";
        for (Author author : query.list()) {
            temp += author.toString() + "\n";
        }
        return temp;
    }

    public String getAuthorByGender(String gender) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM ? where gender = ?", Author.class);
        query.setParameter(1,Author.class);
        query.setParameter(2,gender);
        session.getTransaction().commit();

        String temp = "";
        for (Author author : query.list()) {
            temp += author.toString() + "\n";
        }
        return temp;
    }

    public String getStoreByAddress(String address) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM ? where address like %?%", Store.class);
        query.setParameter(1,Store.class);
        query.setParameter(2,address);
        session.getTransaction().commit();

        String temp = "";
        for (Store store : query.list()) {
            temp += store.toString() + "\n";
        }
        return temp;
    }

    public String getStoreByOwner(String owner) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM ? where owner like %?%", Store.class);
        query.setParameter(1,Store.class);
        query.setParameter(2,owner);
        session.getTransaction().commit();

        String temp = "";
        for (Store store : query.list()) {
            temp += store.toString() + "\n";
        }
        return temp;
    }

    public String getStoreByBookId(int bid) {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<BookToStore> query = session.createSelectionQuery("FROM ? where book_bid = ?", BookToStore.class);
        query.setParameter(1,BookToStore.class);
        query.setParameter(2,bid);
        session.getTransaction().commit();

        String temp = "";
        for (BookToStore bookToStore : query.list()) {
            temp += bookToStore.toString() + "\n";
        }
        return temp;
    }

    public void modifyBook(int bid, Object[] modifiedBook) {
        Session session = model.getSession();
        session.beginTransaction();

        Book book = session.find(Book.class,bid);

        if ( !(modifiedBook[0] == null)){
            book.setIsbn(String.valueOf(modifiedBook[0]));
        }

        try {
            if ( !(modifiedBook[1] == null)){
                book.setDob(LocalDate.parse(String.valueOf(modifiedBook[1])));
            }
        } catch (Exception e){
            System.out.println("Wrong Date format\nBook Date of publish did not changed");
        }

        if ( !(modifiedBook[2] == null)){
            book.setEdition((Integer) modifiedBook[2]);
        }

        if ( !(modifiedBook[3] == null)){
            book.setTitle(String.valueOf(modifiedBook[3]));
        }

        if ( !(modifiedBook[4] == null)){
            if(modifiedBook[4] instanceof Integer){
                try {
                    book.setAuthor(session.find(Author.class,(Integer) modifiedBook[4]));
                } catch (Exception e){
                    System.out.println("No Such Author ID\nBook Author wasn't modified");
                }

            } else {
                Author author = addNewAuthor(new Object[]{modifiedBook[4]});
                session.merge(author);
                book.setAuthor(author);
            }
        }
        session.persist(book);
        session.getTransaction().commit();
    }

    public void printActiveBookWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM VN_Books where active = 'true'", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle() + " - Active: " + thing.isActive());
        }
        session.getTransaction().commit();
    }

    public void printInactiveBookWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM VN_Books where active = 'false'", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle() + " - Active: " + thing.isActive());
        }
        session.getTransaction().commit();
    }

    public void modifyBookActiveness(int bid) {
        Session session = model.getSession();
        session.beginTransaction();

        Book book = session.find(Book.class,bid);

        book.setActive(!book.isActive());

        session.getTransaction().commit();
    }

    public void modifyAuthor(int aid, Object[] modifiedAuthor) {
        Session session = model.getSession();
        session.beginTransaction();

        Author author = session.find(Author.class,aid);

        if ( !(modifiedAuthor[0] == null)){
            author.setName(String.valueOf(modifiedAuthor[0]));
        }

        try {
            if ( !(modifiedAuthor[1] == null)){
                author.setDob(LocalDate.parse(String.valueOf(modifiedAuthor[1])));
            }
        } catch (Exception e){
            System.out.println("Wrong Date format\nAuthor Date of Birth did not changed");
        }

        if ( !(modifiedAuthor[2] == null)) {
            try {
                author.setGender((Gender) modifiedAuthor[2]);
            } catch (Exception e) {
                System.out.println("Wrong gender format\nAuthor Gender did not changed");
            }
        }
        session.persist(author);
        session.getTransaction().commit();
    }

    public void modifyStore(int sid, Object[] modifiedStore) {
        Session session = model.getSession();
        session.beginTransaction();

        Store store = session.find(Store.class,sid);

        if ( !(modifiedStore[0] == null)){
            store.setName(String.valueOf(modifiedStore[0]));
        }

        if ( !(modifiedStore[1] == null)){
            store.setAddress(String.valueOf(modifiedStore[1]));
        }

        if ( !(modifiedStore[2] == null)){
            store.setOwner(String.valueOf(modifiedStore[2]));
        }

        session.persist(store);
        session.getTransaction().commit();
    }

    public void printActiveStoreWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM VN_Stores where active = 'true'", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + " - Active: " + thing.isActive());
        }
        session.getTransaction().commit();
    }

    public void printInactiveStoreWithId() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM VN_Stores where active = 'false'", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + " - Active: " + thing.isActive());
        }
        session.getTransaction().commit();
    }

    public void modifyStoreActiveness(int sid) {
        Session session = model.getSession();
        session.beginTransaction();

        Store store = session.find(Store.class,sid);

        store.setActive(!store.isActive());

        session.getTransaction().commit();
    }

    public void printStores() {
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store where active = 'true'", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + ", " + thing.getAddress() +
                    "Owner: " + thing.getOwner() + "Books: " + thing.getBookList());
        }
        session.getTransaction().commit();
    }

    public void printAllStores(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Store> query = session.createSelectionQuery("FROM Store", Store.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName() + ", " + thing.getAddress() +
                    "Owner: " + thing.getOwner() + "\nBooks: " + thing.getBookList());
        }
        session.getTransaction().commit();
    }

    public void printAuthors(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Author> query = session.createSelectionQuery("FROM Author", Author.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getName()+ " " + thing.getGender() + ", Born: "+ thing.getDob() +
                    "\nBooks:" + thing.getBookList());
        }
        session.getTransaction().commit();
    }

    public void printBooks(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book where active = 'true'", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle() + ", Author: "+ thing.getAuthor().getName() +
                    "\nEdition: "+thing.getEdition()+ " Isbn: "+thing.getIsbn() + " Publish date: "+thing.getDob() +
                    "\nStores: "+thing.getStoreList());
        }
        session.getTransaction().commit();
    }

    public void printAllBooks(){
        Session session = model.getSession();
        session.beginTransaction();

        SelectionQuery<Book> query = session.createSelectionQuery("FROM Book", Book.class);

        for (var thing : query.list()) {
            System.out.println(thing.getId() + " - " + thing.getTitle() + ", Author: "+ thing.getAuthor().getName() +
                    "\nEdition: "+thing.getEdition()+ " Isbn: "+thing.getIsbn() + " Publish date: "+thing.getDob() +
                    "\nStores: "+thing.getStoreList());
        }
        session.getTransaction().commit();
    }
}
/*
    public void initStudentsAndCourses() {
        Session s = model.getSession();
        Transaction tx = s.beginTransaction();

        try {
            Student s1 = new Student();
            s1.setName("Miklos");
            s1.setAddress("Bp");
            s.persist(s1);

            Student s2 = new Student();
            s2.setName("Klari");
            s2.setAddress("Szeged");
            s.persist(s2);

            Course c1 = new Course();
            c1.setMaxHeadCount(50);
            c1.setMinHeadCount(15);
            c1.setName("Angol B - 2023-01");
            c1.setTeacher("Tanarka");
            s.persist(c1);

            tx.commit();
        } catch (Exception e) {
            System.out.println(e);
            tx.rollback();
        }
    }


    public void getAllPizza() {
        Session session = model.getSession();

        Transaction tx = session.beginTransaction();

        SelectionQuery<Pizza> q = session.createSelectionQuery("SELECT p FROM Pizza p", Pizza.class);

        for (Object p : q.list()) {
            System.out.println(p);
        }
        session.clear();

        session.getTransaction().commit();
    }

    public void addPerson(String name, LocalDate dob, Passport passport) {

        Person p = new Person();
        p.setName(name);
        p.setDob(dob);

        Session session = model.getSession();
        Transaction t = session.beginTransaction();

        passport = session.merge(passport);
        p.setPassport(passport);

        session.persist(p);
//        session.flush();
        session.getTransaction().commit();

    }

    public Passport addNewPassport (String national, LocalDate createdAt, LocalDate validUntil){
        Passport p = new Passport();
        p.setNational(national);
        p.setCreatedAt(createdAt);
        p.setValidUntil(validUntil);

        Session session = model.getSession();
        Transaction t = session.beginTransaction();
        session.persist(p);
        session.getTransaction().commit();

        return p;

    }

    public void connectS2C(Integer sid, Integer cid) {
        Session session = model.getSession();
        Transaction tx = session.beginTransaction();
        try {
            Student s = session.find(Student.class, sid);
            Course c = session.find(Course.class, cid);

            s.getCourses().add(c);
            session.persist(s);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<CountryE> getCountryByCarSign(String tt) {
        List<CountryE> result = List.of();
        Session session = model.getSession();

        Transaction tx = session.beginTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CountryE> cQuery = cb.createQuery(CountryE.class);
        Root<CountryE> u = cQuery.from(CountryE.class);
        cQuery.select(u).where(cb.equal(u.get("autojel"), "TT"));

        result = session.createQuery(cQuery).getResultList();
        session.clear();

        tx.commit();
        return result;
    }

    public List<CountryE> getCountryByCarSignEntG(String tt) {
        List<CountryE> result = List.of();
        Session session = model.getSession();

        Transaction tx = session.beginTransaction();

        Query<CountryE> q = session.createNamedQuery("getTTCar", CountryE.class);
        q.setParameter("carSign", tt);
        q.setHint("javax.persistence.loadgraph", session.getEntityGraph("testEntityGraph"));
        result = q.list();
        session.clear();

        tx.commit();
        return result;
    }


    public void addUser(String name, LocalDate regAt) {
        User u = new User();
        u.setName(name);
        u.setRegAt(regAt);

        Session session = model.getSession();
        Transaction t = session.beginTransaction();
        session.persist(u);
        session.getTransaction().commit();
    }

    public void addAddressToUser(int userId, String country, String city, int postCode, String street) {
        Address a = new Address();
        a.setCountry(country);
        a.setCity(city);
        a.setPostalCode(postCode);
        a.setStreet(street);

        Session session = model.getSession();
        Transaction t = session.beginTransaction();

        try {
            User u = session.find(User.class, userId);
            u.getAddresses().add(a);

            session.persist(u);
            session.getTransaction().commit();
        } catch (Exception e){
            System.out.println("No such User!");
        }
    }

    public void deleteUser(int userId) {
        Session session = model.getSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();
            User u = session.find(User.class,userId);
            session.remove(u);
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
        }
    }
*/
