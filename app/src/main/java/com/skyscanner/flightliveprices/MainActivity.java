package com.skyscanner.flightliveprices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.skyscanner.flightliveprices.fragment.FlightPricesResultFragment;
import com.skyscanner.flightliveprices.interfaces.ChangeableToolbarTitle;
import com.skyscanner.flightliveprices.network.RequestQuery;
import com.skyscanner.flightliveprices.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ChangeableToolbarTitle {

    @BindView(R.id.toolbar_main_title)
    TextView toolbarMainTitle;

    @BindView(R.id.toolbar_detail_title)
    TextView toolbarDetailTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(findViewById(R.id.toolbar));

        Date nextMonday = DateUtils.getNextDayOfWeek(new Date(), Calendar.MONDAY);

        RequestQuery query = new RequestQuery.Builder()
                .from("EDI-sky")
                .to("LOND-sky")
                .outbound(nextMonday)
                .inbound(DateUtils.getNextDay(nextMonday))
                .build();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, FlightPricesResultFragment.newInstance(query))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public void setToolbarTitle(String main, String detail) {
        toolbarMainTitle.setText(main);
        toolbarDetailTitle.setText(detail);
    }
}
