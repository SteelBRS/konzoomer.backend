package com.konzoomer.domain;

import javax.jdo.annotations.*;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 09-11-2010
 * Time: 17:27:10
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Store {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private long lastTouched;

    @Persistent
    private long chainID;       // long because not null - must be attached to a chain

    @Persistent
    private String name;

    @Persistent
    private String streetName;

    @Persistent
    private String streetBuildingIdentifier;

    @Persistent
    private String postCodeIdentifier;

    @Persistent
    private String districtName;

    @Persistent
    private String districtSubDivisionIdentifier;

    @Persistent
    private String contactPerson;

    @Persistent
    private String telephone;

    @Persistent
    private String telefax;

    @Persistent
    private String email;

    @Persistent
    private String website;

    @Persistent
    private Double latitude;

    @Persistent
    private Double longitude;

    @Persistent
    private Integer mondayOpen;

    @Persistent
    private Integer mondayClose;

    @Persistent
    private Integer tuesdayOpen;

    @Persistent
    private Integer tuesdayClose;

    @Persistent
    private Integer wednesdayOpen;

    @Persistent
    private Integer wednesdayClose;

    @Persistent
    private Integer thursdayOpen;

    @Persistent
    private Integer thursdayClose;

    @Persistent
    private Integer fridayOpen;

    @Persistent
    private Integer fridayClose;

    @Persistent
    private Integer saturdayOpen;

    @Persistent
    private Integer saturdayClose;

    @Persistent
    private Integer sundayOpen;

    @Persistent
    private Integer sundayClose;

    @Persistent
    private Integer firstAndLastSundayInMonthOpen;

    @Persistent
    private Integer firstAndLastSundayInMonthClose;

    public Long getId() {
        return id;
    }

    public long getLastTouched() {
        return lastTouched;
    }

    public void setLastTouched(long lastTouched) {
        this.lastTouched = lastTouched;
    }

    public long getChainID() {
        return chainID;
    }

    public void setChainID(Long chainID) {
        this.chainID = chainID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetBuildingIdentifier() {
        return streetBuildingIdentifier;
    }

    public void setStreetBuildingIdentifier(String streetBuildingIdentifier) {
        this.streetBuildingIdentifier = streetBuildingIdentifier;
    }

    public String getPostCodeIdentifier() {
        return postCodeIdentifier;
    }

    public void setPostCodeIdentifier(String postCodeIdentifier) {
        this.postCodeIdentifier = postCodeIdentifier;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictSubDivisionIdentifier() {
        return districtSubDivisionIdentifier;
    }

    public void setDistrictSubDivisionIdentifier(String districtSubDivisionIdentifier) {
        this.districtSubDivisionIdentifier = districtSubDivisionIdentifier;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelefax() {
        return telefax;
    }

    public void setTelefax(String telefax) {
        this.telefax = telefax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getMondayOpen() {
        return mondayOpen;
    }

    public void setMondayOpen(Integer mondayOpen) {
        this.mondayOpen = mondayOpen;
    }

    public Integer getMondayClose() {
        return mondayClose;
    }

    public void setMondayClose(Integer mondayClose) {
        this.mondayClose = mondayClose;
    }

    public Integer getTuesdayOpen() {
        return tuesdayOpen;
    }

    public void setTuesdayOpen(Integer tuesdayOpen) {
        this.tuesdayOpen = tuesdayOpen;
    }

    public Integer getTuesdayClose() {
        return tuesdayClose;
    }

    public void setTuesdayClose(Integer tuesdayClose) {
        this.tuesdayClose = tuesdayClose;
    }

    public Integer getWednesdayOpen() {
        return wednesdayOpen;
    }

    public void setWednesdayOpen(Integer wednesdayOpen) {
        this.wednesdayOpen = wednesdayOpen;
    }

    public Integer getWednesdayClose() {
        return wednesdayClose;
    }

    public void setWednesdayClose(Integer wednesdayClose) {
        this.wednesdayClose = wednesdayClose;
    }

    public Integer getThursdayOpen() {
        return thursdayOpen;
    }

    public void setThursdayOpen(Integer thursdayOpen) {
        this.thursdayOpen = thursdayOpen;
    }

    public Integer getThursdayClose() {
        return thursdayClose;
    }

    public void setThursdayClose(Integer thursdayClose) {
        this.thursdayClose = thursdayClose;
    }

    public Integer getFridayOpen() {
        return fridayOpen;
    }

    public void setFridayOpen(Integer fridayOpen) {
        this.fridayOpen = fridayOpen;
    }

    public Integer getFridayClose() {
        return fridayClose;
    }

    public void setFridayClose(Integer fridayClose) {
        this.fridayClose = fridayClose;
    }

    public Integer getSaturdayOpen() {
        return saturdayOpen;
    }

    public void setSaturdayOpen(Integer saturdayOpen) {
        this.saturdayOpen = saturdayOpen;
    }

    public Integer getSaturdayClose() {
        return saturdayClose;
    }

    public void setSaturdayClose(Integer saturdayClose) {
        this.saturdayClose = saturdayClose;
    }

    public Integer getSundayOpen() {
        return sundayOpen;
    }

    public void setSundayOpen(Integer sundayOpen) {
        this.sundayOpen = sundayOpen;
    }

    public Integer getSundayClose() {
        return sundayClose;
    }

    public void setSundayClose(Integer sundayClose) {
        this.sundayClose = sundayClose;
    }

    public Integer getFirstAndLastSundayInMonthOpen() {
        return firstAndLastSundayInMonthOpen;
    }

    public void setFirstAndLastSundayInMonthOpen(Integer firstAndLastSundayInMonthOpen) {
        this.firstAndLastSundayInMonthOpen = firstAndLastSundayInMonthOpen;
    }

    public Integer getFirstAndLastSundayInMonthClose() {
        return firstAndLastSundayInMonthClose;
    }

    public void setFirstAndLastSundayInMonthClose(Integer firstAndLastSundayInMonthClose) {
        this.firstAndLastSundayInMonthClose = firstAndLastSundayInMonthClose;
    }
}
