package com.example.ncrm;

/**
 * Created by shameer on 2018-02-10.
 */

public class Contact {
    private String mName;
    private String mOrganization;
    private String mStreetAddress;
    private String mCity;
    private String mCountry;
    private String mPhoneNumber;
    private String mMobileNumber;
    private String mEmail;
    private String mWebsite;
    private String mFacebookId;
    private String mTwitterId;
    private String mLinkedInId;
    private String mUser;

    public Contact(String name, String organization, String streetAddress, String city, String country,
                   String phoneNumber, String mobileNumber, String email, String website,
                   String facebookId, String twitterId, String linkedInId, String user) {
        mName = name;
        mOrganization = organization;
        mStreetAddress = streetAddress;
        mCity = city;
        mCountry = country;
        mPhoneNumber = phoneNumber;
        mMobileNumber = mobileNumber;
        mEmail = email;
        mWebsite = website;
        mFacebookId = facebookId;
        mTwitterId = twitterId;
        mLinkedInId = linkedInId;
        mUser = user;
    }

    public Contact() { }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmOrganization(String mOrganization) {
        this.mOrganization = mOrganization;
    }

    public void setmStreetAddress(String mStreetAddress) {
        this.mStreetAddress = mStreetAddress;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setmMobileNumber(String mMobileNumber) {
        this.mMobileNumber = mMobileNumber;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setmWebsite(String mWebsite) {
        this.mWebsite = mWebsite;
    }

    public void setmFacebookId(String mFacebookId) {
        this.mFacebookId = mFacebookId;
    }

    public void setmTwitterId(String mTwitterId) {
        this.mTwitterId = mTwitterId;
    }

    public void setmLinkedInId(String mLinkedInId) {
        this.mLinkedInId = mLinkedInId;
    }

    public void setmUser(String mUser) {
        this.mUser = mUser;
    }

    public String getmName() {
        return mName;
    }

    public String getmOrganization() {
        return mOrganization;
    }

    public String getmStreetAddress() {
        return mStreetAddress;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmCountry() {
        return mCountry;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getmMobileNumber() {
        return mMobileNumber;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmWebsite() {
        return mWebsite;
    }

    public String getmFacebookId() {
        return mFacebookId;
    }

    public String getmTwitterId() {
        return mTwitterId;
    }

    public String getmLinkedInId() {
        return mLinkedInId;
    }

    public String getmUser() {
        return mUser;
    }
}
