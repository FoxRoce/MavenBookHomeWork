package main;

import model.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;

public class Controller implements AutoCloseable {

    private HibernateContext model = new HibernateContext();

    @Override
    public void close() throws Exception {
        model.close();
    }

    public void createTables(){
        Session session = model.getSession();
        Transaction t = session.beginTransaction();
        session.getTransaction().commit();
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
}
