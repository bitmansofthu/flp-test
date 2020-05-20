package com.skyscanner.flightliveprices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Itineary {

    @SerializedName("OutboundLegId")
    @Expose
    private Leg outboundLegId;
    @SerializedName("InboundLegId")
    @Expose
    private Leg inboundLegId;
    @SerializedName("PricingOptions")
    @Expose
    private List<PricingOption> pricingOptions = null;
    @SerializedName("BookingDetailsLink")
    @Expose
    private BookingDetailsLink bookingDetailsLink;
    @SerializedName("FormattedData")
    @Expose
    private String formattedData;

    public Leg getOutboundLegId() {
        return outboundLegId;
    }

    public void setOutboundLegId(Leg outboundLegId) {
        this.outboundLegId = outboundLegId;
    }

    public Leg getInboundLegId() {
        return inboundLegId;
    }

    public void setInboundLegId(Leg inboundLegId) {
        this.inboundLegId = inboundLegId;
    }

    public List<PricingOption> getPricingOptions() {
        return pricingOptions;
    }

    public void setPricingOptions(List<PricingOption> pricingOptions) {
        this.pricingOptions = pricingOptions;
    }

    public BookingDetailsLink getBookingDetailsLink() {
        return bookingDetailsLink;
    }

    public void setBookingDetailsLink(BookingDetailsLink bookingDetailsLink) {
        this.bookingDetailsLink = bookingDetailsLink;
    }

    public String getFormattedData() {
        return formattedData;
    }

    public void setFormattedData(String formattedData) {
        this.formattedData = formattedData;
    }

}
