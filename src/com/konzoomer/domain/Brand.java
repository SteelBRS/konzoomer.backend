package com.konzoomer.domain;

import javax.jdo.annotations.*;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 09-11-2010
 * Time: 17:30:26
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Brand implements Comparable<Brand> {

    public static final short RATING_NONAME = 10000;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;

    @Persistent
    private Short rating;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    public int compareTo(Brand otherBrand) {
        return name.compareTo(otherBrand.name);
    }
}
