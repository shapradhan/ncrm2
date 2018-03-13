package com.example.ncrm;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by shameer on 2018-02-11.
 */

public class Meeting implements Serializable {
    private String title;
    private String venue;
    private String streetAddress;
    private String city;
    private String country;
    private String date;
    private String time;
    private String agenda;
    private Map<String, Boolean> participants;

    @Exclude
    private String id;

    public Meeting() {    }

    public Meeting(String title, String venue, String streetAddress, String city, String country, String date, String time, String agenda, Map<String, Boolean> participants) {
        this.title = title;
        this.venue = venue;
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
        this.date = date;
        this.time = time;
        this.agenda = agenda;
        this.participants = participants;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public Map<String, Boolean> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Boolean> participants) {
        this.participants = participants;
    }
}
