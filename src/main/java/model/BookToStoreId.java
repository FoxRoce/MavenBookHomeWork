package model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class BookToStoreId implements Serializable {
    private Long book_bid;
    private Long store_sid;
}
