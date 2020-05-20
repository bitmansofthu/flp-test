package com.skyscanner.flightliveprices.network;

import android.content.Context;

import com.google.gson.Gson;
import com.skyscanner.flightliveprices.interfaces.ApiService;
import com.skyscanner.flightliveprices.model.ApiResponse;
import com.skyscanner.flightliveprices.model.Itineary;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ApiServiceHelper {

    private static DateFormat formDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final String BASE_URL = "http://partners.api.skyscanner.net/apiservices/";
    public static final String API_KEY = "ss630745725358065467897349852985";

    private static final int PAGE_SIZE = 30;

    private Context context;

    private ApiService apiService;
    private String sessionUrl;

    public ApiServiceHelper(Context context) {
        this.context = context;

        apiService = ApiClient.getClient(context, BASE_URL)
                .create(ApiService.class);
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public Single<Response<Object>> getSessionKey(RequestQuery query) {
        HashMap<String, String> map = new HashMap<>();

        map.put("apikey", API_KEY);
        map.put("adults", String.valueOf(query.getAdults()));
        map.put("cabinclass", query.getCabinClass());
        map.put("children", String.valueOf(query.getChildren()));
        map.put("country", query.getCountry());
        map.put("currency", query.getCurrency());
        map.put("destinationplace", query.getTo());
        map.put("inbounddate", formDateFormat.format(query.getInbound()));
        map.put("infants", String.valueOf(query.getInfants()));
        map.put("locale", query.getLocale());
        map.put("locationSchema", query.getLocationSchema());
        map.put("originplace", query.getFrom());
        map.put("outbounddate", formDateFormat.format(query.getOutbound()));

        return apiService.getSessionKey(map);
    }

    public Single<List<Itineary>> pollResults(int pageIndex) {
        return Single.create(emitter -> {
            try {
                Call<ResponseBody> call = apiService.pollResults(sessionUrl, API_KEY, pageIndex, PAGE_SIZE);
                Response<ResponseBody> resp = call.execute();

                if (!resp.isSuccessful()) {
                    throw new IOException("Bad response code: " + resp.code());
                }

                JSONObject root = new JSONObject(resp.body().string());
                FlightPricesParser parser = new FlightPricesParser();

                emitter.onSuccess(parser.parse(root));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<ApiResponse> readFromAssets(final String assetsFile) {
        return Single.create(emitter -> {
                Reader reader = null;

                try {
                    reader = new InputStreamReader(context.getAssets().open(assetsFile), "UTF-8");
                    ApiResponse response = new Gson().fromJson(reader, ApiResponse.class);
                    emitter.onSuccess(response);
                } catch (Exception e) {
                    emitter.onError(e);
                } finally {
                    try {
                        reader.close();
                    } catch (Exception e) {

                    }
                }
            });
    }

}
