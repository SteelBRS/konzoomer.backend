package com.konzoomer.domain;

import com.google.appengine.api.datastore.Blob;

import javax.jdo.annotations.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 05-11-2010
 * Time: 17:59:34
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Offer {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private long chainID;       // long because not null - must be attached to a chain

    @Persistent
    private String name;

    @Persistent
    private String description;

    @Persistent
    private Blob image;

    @Persistent
    private Date start;

    @Persistent
    private Date end;

    @Persistent
    private Set<Long> brands;

    @Persistent
    private byte category;

    @Persistent
    private short totalQuantity;

    @Persistent
    private short totalQuantityTo;      // Used only if totalQuantity isn't fixed (400-650g)

    @Persistent
    private byte totalQuantityUnit;     // liter, centiliter, gram, piece, etc.

    @Persistent
    private short unitQuantity;

    @Persistent
    private short unitQuantityTo;      // Used only if unitQuantity isn't fixed (33-50cl)

    @Persistent
    private byte unitQuantityUnit;     // liter, centiliter, gram, piece, etc.

    @Persistent
    private byte number;

    @Persistent
    private byte type;          // packages, glasses, bags, cans, bottles, pieces, etc.

    @Persistent
    private int priceE2;

    @Persistent
    private short rating;

    public Offer() {
    }

    public Long getId() {
        return id;
    }

    public long getChainID() {
        return chainID;
    }

    public void setChainID(long chainID) {
        this.chainID = chainID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Set<Long> getBrands() {
        if (brands == null)
            return new HashSet<Long>();
        else
            return brands;
    }

    public void setBrands(Set<Long> brands) {
        this.brands = brands;
    }

    public byte getCategory() {
        return category;
    }

    public void setCategory(byte category) {
        this.category = category;
    }

    public short getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(short totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public short getTotalQuantityTo() {
        return totalQuantityTo;
    }

    public void setTotalQuantityTo(short totalQuantityTo) {
        this.totalQuantityTo = totalQuantityTo;
    }

    public byte getTotalQuantityUnit() {
        return totalQuantityUnit;
    }

    public void setTotalQuantityUnit(byte totalQuantityUnit) {
        this.totalQuantityUnit = totalQuantityUnit;
    }

    public short getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(short unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public short getUnitQuantityTo() {
        return unitQuantityTo;
    }

    public void setUnitQuantityTo(short unitQuantityTo) {
        this.unitQuantityTo = unitQuantityTo;
    }

    public byte getUnitQuantityUnit() {
        return unitQuantityUnit;
    }

    public void setUnitQuantityUnit(byte unitQuantityUnit) {
        this.unitQuantityUnit = unitQuantityUnit;
    }

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getPriceE2() {
        return priceE2;
    }

    public void setPriceE2(int priceE2) {
        this.priceE2 = priceE2;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }
}
