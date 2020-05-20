package com.skyscanner.flightliveprices.network;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class RequestQuery implements Parcelable {

    private String from;
    private String to;
    private Date inbound;
    private Date outbound;
    private String country;
    private String locale;
    private String currency;
    private int adults;
    private int children;
    private int infants;
    private String locationSchema;
    private String cabinClass;
    private boolean groupPricing;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Date getInbound() {
        return inbound;
    }

    public Date getOutbound() {
        return outbound;
    }

    public String getCountry() {
        return country;
    }

    public String getLocale() {
        return locale;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public int getInfants() {
        return infants;
    }

    public String getLocationSchema() {
        return locationSchema;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public boolean isGroupPricing() {
        return groupPricing;
    }

    public static class Builder {

        private RequestQuery query = new RequestQuery();

        public Builder from(String from) {
            query.from = from;

            return this;
        }

        public Builder to(String to) {
            query.to = to;

            return this;
        }

        public Builder inbound(Date inbound) {
            query.inbound = inbound;

            return this;
        }

        public Builder outbound(Date outbound) {
            query.outbound = outbound;

            return this;
        }

        public RequestQuery build() {
            query.children = 0;
            query.adults = 1;
            query.currency = "GBP";
            query.cabinClass = "Economy";
            query.children = 0;
            query.country = "UK";
            query.currency = "GBP";
            query.groupPricing = false;
            query.infants = 0;
            query.locale = "en-GB";
            query.locationSchema = "sky";

            return query;
        }
    }

    public static final Parcelable.Creator<RequestQuery> CREATOR
            = new Parcelable.Creator<RequestQuery>() {
        public RequestQuery createFromParcel(Parcel in) {
            return new RequestQuery(in);
        }

        public RequestQuery[] newArray(int size) {
            return new RequestQuery[size];
        }
    };

    private RequestQuery() {

    }

    private RequestQuery(Parcel in) {
        from = in.readString();
        to = in.readString();
        inbound = new Date(in.readLong());
        outbound = new Date(in.readLong());
        country = in.readString();
        locale = in.readString();
        currency = in.readString();
        adults = in.readInt();
        children = in.readInt();
        infants = in.readInt();
        locationSchema = in.readString();
        cabinClass = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(from);
        parcel.writeString(to);
        parcel.writeLong(inbound.getTime());
        parcel.writeLong(outbound.getTime());
        parcel.writeString(country);
        parcel.writeString(locale);
        parcel.writeString(currency);
        parcel.writeInt(adults);
        parcel.writeInt(children);
        parcel.writeInt(infants);
        parcel.writeString(locationSchema);
        parcel.writeString(cabinClass);
    }


}
