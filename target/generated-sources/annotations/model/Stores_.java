package model;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Stores.class)
public abstract class Stores_ {

	public static volatile SingularAttribute<Stores, String> owner;
	public static volatile SingularAttribute<Stores, String> address;
	public static volatile SingularAttribute<Stores, Integer> id;

	public static final String OWNER = "owner";
	public static final String ADDRESS = "address";
	public static final String ID = "id";

}

