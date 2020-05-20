package com.skyscanner.flightliveprices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingDetailsLink {

    @SerializedName("Uri")
    @Expose
    private String uri;
    @SerializedName("Body")
    @Expose
    private String body;
    @SerializedName("Method")
    @Expose
    private String method;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
