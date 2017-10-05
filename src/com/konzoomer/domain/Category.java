package com.konzoomer.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 09-11-2010
 * Time: 17:31:01
 */
public class Category {

    private final byte id;
    private final byte parentID;
    private final String nameKey;

    public Category(byte id, byte parentID, String nameKey) {
        this.id = id;
        this.parentID = parentID;
        this.nameKey = nameKey;
    }

    public byte getId() {
        return id;
    }

    public byte getParentID() {
        return parentID;
    }

    public String getNameKey() {
        return nameKey;
    }
}
