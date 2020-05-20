package com.skyscanner.flightliveprices.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skyscanner.flightliveprices.R;
import com.skyscanner.flightliveprices.adapter.FlightPricesAdapter;
import com.skyscanner.flightliveprices.interfaces.ChangeableToolbarTitle;
import com.skyscanner.flightliveprices.model.ApiResponse;
import com.skyscanner.flightliveprices.model.Itineary;
import com.skyscanner.flightliveprices.network.RequestQuery;
import com.skyscanner.flightliveprices.network.ApiServiceHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FlightPricesResultFragment extends BaseFragment {

    private CompositeDisposable disposable = new CompositeDisposable();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.number_of_results)
    TextView numberOfResults;

    private RequestQuery query;
    private FlightPricesAdapter adapter;

    private ApiServiceHelper apiService;
    private int pageIndex;

    public static FlightPricesResultFragment newInstance(RequestQuery query) {
        Bundle args = new Bundle();
        args.putParcelable("query", query);

        FlightPricesResultFragment fragment = new FlightPricesResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        query = getArguments().getParcelable("query");

        apiService = new ApiServiceHelper(getActivity().getApplicationContext());

        adapter = new FlightPricesAdapter(getContext());
        adapter.setCurrency(Currency.getInstance(query.getCurrency()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flightprices_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        fetch();
        //fetchOffline();

        updateTitle();
    }

    @Override
    public void onStop() {
        disposable.clear();

        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void fetch() {
        progressBar.setVisibility(View.VISIBLE);

        disposable.add(apiService.getSessionKey(query)
                .flatMap(objectResponse -> {
                    String location = objectResponse.headers().get("Location");

                    apiService.setSessionUrl(location);

                    return apiService.pollResults(0);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Itineary>>() {
                    @Override
                    public void onSuccess(List<Itineary> itineraries) {
                        adapter.appendItienaries(itineraries);

                        numberOfResults.setText(getString(R.string.number_of_results, itineraries.size()));

                        progressBar.setVisibility(View.INVISIBLE);

                        ++pageIndex;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(FlightPricesResultFragment.class.getSimpleName(), "Fetch failed", e);

                        showError(getString(R.string.error_dialog_title), e.getMessage());

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }));
    }

    private void poll() {
        disposable.add(apiService.pollResults(pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Itineary>>() {
                    @Override
                    public void onSuccess(List<Itineary> itinearies) {
                        adapter.appendItienaries(itinearies);

                        ++pageIndex;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(FlightPricesResultFragment.class.getSimpleName(), "Poll failed", e);

                        showError(getString(R.string.error_dialog_title), e.getMessage());
                    }
                }));
    }

    /*private void fetchOffline() {
        progressBar.setVisibility(View.VISIBLE);

        disposable.add(apiService.readFromAssets("results.json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        adapter.appendItienaries(response.getFlights().getItinearies());

                        numberOfResults.setText(getString(R.string.number_of_results, response.getFlights().getItinearies().size()));

                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(FlightPricesResultFragment.class.getSimpleName(), "Fetch offline failed", e);

                        showError(getString(R.string.error_dialog_title), e.getMessage());
                    }
                }));
    }*/

    private void showError(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, (DialogInterface dialogInterface, int i) -> {

                }).show();

    }

    private void updateTitle() {
        DateFormat df = new SimpleDateFormat("dd MMM");

        String main = String.format("%s - %s", query.getFrom(), query.getTo());
        String detail = getString(R.string.toolbar_detail_title, df.format(query.getInbound()), df.format(query.getOutbound()), query.getAdults(), query.getCabinClass());

        ((ChangeableToolbarTitle)getActivity()).setToolbarTitle(main, detail);
    }
}
