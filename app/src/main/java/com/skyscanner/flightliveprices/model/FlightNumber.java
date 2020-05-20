package com.skyscanner.flightliveprices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlightNumber {

    @SerializedName("FlightNumber")
    @Expose
    private String flightNumber;
    @SerializedName("CarrierId")
    @Expose
    private Integer carrierId;
    @SerializedName("Carrier")
    @Expose
    private Carrier carrier;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

}
