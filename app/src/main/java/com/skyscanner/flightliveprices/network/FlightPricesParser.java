package com.skyscanner.flightliveprices.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.skyscanner.flightliveprices.interfaces.ApiService;
import com.skyscanner.flightliveprices.model.Agent;
import com.skyscanner.flightliveprices.model.Carrier;
import com.skyscanner.flightliveprices.model.Itineary;
import com.skyscanner.flightliveprices.model.Leg;
import com.skyscanner.flightliveprices.model.PricingOption;
import com.skyscanner.flightliveprices.model.Station;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlightPricesParser {

    private Itineary[] itinearies;

    private Carrier[] carriers;
    private Agent[] agents;
    private Station[] stations;
    private Leg[] legs;

    private Gson gson;

    public FlightPricesParser() {
        gson = getGson();
    }

    public List<Itineary> parse(JSONObject root) throws JSONException {
        carriers = gson.fromJson(root.getJSONArray("Carriers").toString(), Carrier[].class);
        agents = gson.fromJson(root.getJSONArray("Agents").toString(), Agent[].class);
        stations = gson.fromJson(root.getJSONArray("Places").toString(), Station[].class);
        legs = gson.fromJson(root.getJSONArray("Legs").toString(), Leg[].class);
        itinearies = gson.fromJson(root.getJSONArray("Itineraries").toString(), Itineary[].class);

        return new ArrayList<>(Arrays.asList(itinearies));
    }

    private JsonDeserializer<Leg> legJsonDeserializer = (json, typeOfT, context) -> {
        JsonObject jso = json.getAsJsonObject();
        Leg leg = new Leg();

        leg.setId(jso.get("Id").getAsString());
        leg.setOriginStation(findStationById(jso.get("OriginStation").getAsInt()));
        leg.setDestinationStation(findStationById(jso.get("DestinationStation").getAsInt()));
        leg.setArrival(jso.get("Arrival").getAsString());
        leg.setDeparture(jso.get("Departure").getAsString());
        leg.setDuration(jso.get("Duration").getAsInt());
        leg.setOperatingCarriers(new ArrayList<Carrier>());
        leg.getOperatingCarriers().add(findCarrierById(jso.getAsJsonArray("OperatingCarriers").get(0).getAsInt()));
        leg.setCarriers(new ArrayList<Carrier>());
        leg.getCarriers().add(findCarrierById(jso.getAsJsonArray("Carriers").get(0).getAsInt()));
        leg.setStops(new ArrayList<>());

        return leg;
    };

    private JsonDeserializer<Itineary> itinearyJsonDeserializer = (json, typeOfT, context) -> {
        JsonObject jso = json.getAsJsonObject();
        Itineary iti = new Itineary();

        ArrayList<PricingOption> pros = new ArrayList<>();

        JsonArray prosjsa = jso.getAsJsonArray("PricingOptions");
        for (JsonElement je : prosjsa) {
            PricingOption po = new PricingOption();
            JsonObject jejo = je.getAsJsonObject();
            po.setAgents(new ArrayList<>());
            po.getAgents().add(findAgentById(jejo.getAsJsonArray("Agents").get(0).getAsInt()));
            po.setPrice(jejo.get("Price").getAsDouble());
            po.setQuoteAgeInMinutes(jejo.get("QuoteAgeInMinutes").getAsInt());

            pros.add(po);
        }

        iti.setInboundLegId(findLegById(jso.get("InboundLegId").getAsString()));
        iti.setOutboundLegId(findLegById(jso.get("OutboundLegId").getAsString()));
        iti.setPricingOptions(pros);

        return iti;
    };

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Leg.class, legJsonDeserializer)
                .registerTypeAdapter(Itineary.class, itinearyJsonDeserializer)
                .create();
    }

    private Station findStationById(int id) {
        for (Station station : stations) {
            if (station.getId() == id) {
                return station;
            }
        }

        return null;
    }

    private Agent findAgentById(int id) {
        for (Agent agent : agents) {
            if (agent.getId() == id) {
                return agent;
            }
        }

        return null;
    }

    private Carrier findCarrierById(int id) {
        for (Carrier carrier : carriers) {
            if (carrier.getId() == id) {
                return carrier;
            }
        }

        return null;
    }

    private Leg findLegById(String id) {
        for (Leg leg : legs) {
            if (leg.getId().equals(id)) {
                return leg;
            }
        }

        return null;
    }

    // TODO
    /*class Finder<T> {
        T findById(T[] objs, int id) {
            for (T t : objs) {

            }
        }
    }*/
}
