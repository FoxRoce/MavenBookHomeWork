package model;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Books.class)
public abstract class Books_ {

	public static volatile SingularAttribute<Books, LocalDate> dob;
	public static volatile SingularAttribute<Books, Integer> author;
	public static volatile SingularAttribute<Books, Integer> edition;
	public static volatile SingularAttribute<Books, Integer> id;
	public static volatile SingularAttribute<Books, String> title;

	public static final String DOB = "dob";
	public static final String AUTHOR = "author";
	public static final String EDITION = "edition";
	public static final String ID = "id";
	public static final String TITLE = "title";

}

