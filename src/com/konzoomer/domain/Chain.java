package com.konzoomer.domain;

import javax.jdo.annotations.*;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 09-11-2010
 * Time: 17:26:22
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Chain {

    @PrimaryKey
    @Persistent
    private Long id;

    @Persistent
    private Long motherChainID;

    @Persistent
    private String name;

    @Persistent
    private boolean hasStores;

    @Persistent
    private String country;

    @Persistent
    private String currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMotherChainID() {
        return motherChainID;
    }

    public void setMotherChainID(Long motherChainID) {
        this.motherChainID = motherChainID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasStores() {
        return hasStores;
    }

    public void setHasStores(boolean hasStores) {
        this.hasStores = hasStores;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
