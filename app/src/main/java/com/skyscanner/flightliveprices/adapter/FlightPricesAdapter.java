package com.skyscanner.flightliveprices.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyscanner.flightliveprices.R;
import com.skyscanner.flightliveprices.model.Itineary;
import com.skyscanner.flightliveprices.model.Leg;
import com.skyscanner.flightliveprices.model.PricingOption;
import com.skyscanner.flightliveprices.util.Utils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightPricesAdapter extends RecyclerView.Adapter<FlightPricesAdapter.MyViewHolder> {

    private Context context;
    private List<Itineary> itienaries = new ArrayList<>();

    private Currency currency;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.inboundleg_thumbnail)
        ImageView inboundThumbnail;
        @BindView(R.id.inboundleg_time)
        TextView inboundTime;
        @BindView(R.id.inboundleg_duration)
        TextView inboundDuration;
        @BindView(R.id.inboundleg_stations)
        TextView inboundStations;
        @BindView(R.id.inboundleg_stops)
        TextView inboundStops;

        @BindView(R.id.outboundleg_thumbnail)
        ImageView outboundThumbnail;
        @BindView(R.id.outboundleg_time)
        TextView outboundTime;
        @BindView(R.id.outboundleg_duration)
        TextView outboundDuration;
        @BindView(R.id.outboundleg_stations)
        TextView outboundStations;
        @BindView(R.id.outboundleg_stops)
        TextView outboundStops;

        @BindView(R.id.pricetag)
        TextView priceTag;
        @BindView(R.id.quoteage_face)
        ImageView quoteageFace;
        @BindView(R.id.quoteage_value)
        TextView quoteageValue;

        public MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    public FlightPricesAdapter(Context context) {
        this.context = context;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public FlightPricesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight_result_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FlightPricesAdapter.MyViewHolder holder, int position) {
        Itineary iti = itienaries.get(position);

        Picasso.get().load(iti.getInboundLegId().getCarriers().get(0).getImageUrl()).into(holder.inboundThumbnail);
        Picasso.get().load(iti.getOutboundLegId().getCarriers().get(0).getImageUrl()).into(holder.outboundThumbnail);

        displayLegTime(iti.getInboundLegId(), holder.inboundTime);
        displayLegTime(iti.getOutboundLegId(), holder.outboundTime);

        displayDuration(iti.getInboundLegId().getDuration(), holder.inboundDuration);
        displayDuration(iti.getOutboundLegId().getDuration(), holder.outboundDuration);

        displayStationsCarrier(iti.getInboundLegId(), holder.inboundStations);
        displayStationsCarrier(iti.getOutboundLegId(), holder.outboundStations);

        displayStops(iti.getInboundLegId(), holder.inboundStops);
        displayStops(iti.getOutboundLegId(), holder.outboundStops);

        // price options
        displayPriceAndQuoteAge(iti.getPricingOptions(), holder);
    }

    @Override
    public int getItemCount() {
        return itienaries.size();
    }

    public void appendItienaries(List<Itineary> itienaries) {
        this.itienaries.addAll(itienaries);

        notifyDataSetChanged();
    }

    public void clear() {
        itienaries.clear();

        notifyDataSetChanged();
    }

    private void displayLegTime(Leg leg, TextView target) {
        String arrivalTime = leg.getArrival().substring(11, 16);
        String departureTime = leg.getDeparture().substring(11, 16);

        target.setText(departureTime + " - " + arrivalTime);
    }

    private void displayDuration(int duration, TextView target) {
        String durtxt = String.format("%dh %02dm", duration / 60, duration % 60);

        target.setText(durtxt);
    }

    private void displayStationsCarrier(Leg leg, TextView target) {
        String origin = leg.getOriginStation().getCode();
        String dest = leg.getDestinationStation().getCode();
        String op = leg.getOperatingCarriers().get(0).getName();

        target.setText(String.format("%s-%s, %s", origin, dest, op));
    }

    private void displayStops(Leg leg, TextView target) {
        if (leg.getStops().size() == 0) {
            target.setText(context.getString(R.string.stops_direct));
        } else {
            target.setText(context.getString(R.string.stops_numberof, leg.getStops().size()));
        }
    }

    private void displayPriceAndQuoteAge(List<PricingOption> pricingOptions, MyViewHolder holder) {
        int min = Utils.findMinIndex(pricingOptions);

        if (min > -1) {
            PricingOption mpo = pricingOptions.get(min);

            if (mpo.getQuoteAgeInMinutes() < 30) {
                holder.quoteageFace.setImageResource(R.drawable.face_very_satisfied);
            } else if (mpo.getQuoteAgeInMinutes() < 60) {
                holder.quoteageFace.setImageResource(R.drawable.face_satisfied);
            } else if (mpo.getQuoteAgeInMinutes() < 80) {
                holder.quoteageFace.setImageResource(R.drawable.face_neutral);
            } else if (mpo.getQuoteAgeInMinutes() < 120) {
                holder.quoteageFace.setImageResource(R.drawable.face_dissatisfied);
            } else {
                holder.quoteageFace.setImageResource(R.drawable.face_very_dissatisfied);
            }

            //
            holder.quoteageValue.setText(String.valueOf(mpo.getQuoteAgeInMinutes()));
            holder.priceTag.setText(String.format("%s%.2f", currency.getSymbol(), mpo.getPrice()));
        }
    }
}
