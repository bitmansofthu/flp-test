package com.skyscanner.flightliveprices.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PricingOption implements Comparable<PricingOption> {

    @SerializedName("Agents")
    @Expose
    private List<Agent> agents = null;
    @SerializedName("QuoteAgeInMinutes")
    @Expose
    private Integer quoteAgeInMinutes;
    @SerializedName("Price")
    @Expose
    private Double price;
    @SerializedName("DeeplinkUrl")
    @Expose
    private String deeplinkUrl;

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public Integer getQuoteAgeInMinutes() {
        return quoteAgeInMinutes;
    }

    public void setQuoteAgeInMinutes(Integer quoteAgeInMinutes) {
        this.quoteAgeInMinutes = quoteAgeInMinutes;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDeeplinkUrl() {
        return deeplinkUrl;
    }

    public void setDeeplinkUrl(String deeplinkUrl) {
        this.deeplinkUrl = deeplinkUrl;
    }

    @Override
    public int compareTo(@NonNull PricingOption po) {
        return price.compareTo(po.price);
    }
}
