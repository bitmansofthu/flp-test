package com.skyscanner.flightliveprices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Flights {

    @SerializedName("SessionKey")
    @Expose
    private String sessionKey;

    @SerializedName("Itineraries")
    @Expose
    private List<Itineary> itineraries;

    public List<Itineary> getItinearies() {
        return itineraries;
    }

    public void setItinearies(List<Itineary> itinearies) {
        this.itineraries = itinearies;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

}
