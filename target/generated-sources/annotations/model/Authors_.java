package model;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Authors.class)
public abstract class Authors_ {

	public static volatile SingularAttribute<Authors, Gender> gender;
	public static volatile SingularAttribute<Authors, LocalDate> dob;
	public static volatile SingularAttribute<Authors, String> name;
	public static volatile SingularAttribute<Authors, Integer> id;

	public static final String GENDER = "gender";
	public static final String DOB = "dob";
	public static final String NAME = "name";
	public static final String ID = "id";

}

