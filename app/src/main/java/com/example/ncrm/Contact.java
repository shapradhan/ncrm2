package com.example.ncrm;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by shameer on 2018-02-10.
 */

public class Contact implements Serializable {

    private String name;
    private String organization;
    private String streetAddress;
    private String city;
    private String country;
    private String phoneNumber;
    private String mobileNumber;
    private String email;
    private String website;
    private String facebookId;
    private String twitterId;
    private String linkedInId;
    private String userId;
    private Map<String, Boolean> meeting;

    @Exclude
    private String id;

    public Contact(String name, String organization, String streetAddress, String city, String country,
                   String phoneNumber, String mobileNumber, String email, String website,
                   String facebookId, String twitterId, String linkedInId, String userId) {
        this.name = name;
        this.organization = organization;
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.website = website;
        this.facebookId = facebookId;
        this.twitterId = twitterId;
        this.linkedInId = linkedInId;
        this.userId = userId;
    }

    public Contact() { }

    public Contact(String name, String organization) {
        this.name = name;
        this.organization = organization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getLinkedInId() {
        return linkedInId;
    }

    public void setLinkedInId(String linkedInId) {
        this.linkedInId = linkedInId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Boolean> getMeeting() {
        return meeting;
    }

    public void setMeeting(Map<String, Boolean> meeting) {
        this.meeting = meeting;
    }
}
