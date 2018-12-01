package com.example.personal.sutdbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;

import java.text.SimpleDateFormat;
import org.joda.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class BookTimings extends AppCompatActivity{
    private ArrayList<TimingsData> timings = new ArrayList<>();
    private TextView dateInfo;
    private TimingsAdapter timingsAdapter;
    private static final String TAG = "XBookTimings";
    private int startTime;
    private int endTime;
    private Date date;
    private LocalDateTime datePicked;
    private String name;
    private ArrayList<String> blockedTimings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookable_timings);
        startTime = 9;  //set booking time range
        endTime = 18;

        Intent intent = getIntent();

        if (intent.getExtras().getBoolean("PROF")) {
            date = (Date) intent.getSerializableExtra(Prof.DATE_PICKED);
            name = intent.getStringExtra(Prof.NAME);
            blockedTimings = intent.getStringArrayListExtra(Prof.BLOCKED_TIMINGS);
        }
        else{
            //set up facil
            date = (Date) intent.getSerializableExtra(Prof.DATE_PICKED);
        }

        //convert Date to joda-date
        datePicked = new LocalDateTime(date);
        dateInfo = findViewById(R.id.date);
        dateInfo.setText(datePicked.toString("E dd MMM yyyy"));
        setTitle(getName());
        initTimings();


    }


    //create timings
    private void initTimings() {
        for (int i = startTime; i < endTime; i++) {
            LocalDateTime time = new LocalDateTime(datePicked.getYear(), datePicked.getMonthOfYear(), datePicked.getDayOfMonth(), i, 0);
            LocalDateTime time1 = new LocalDateTime(datePicked.getYear(), datePicked.getMonthOfYear(), datePicked.getDayOfMonth(), i, 30);
            Log.i(TAG, "initTimings: " + time.toString("HH:mm"));
            TimingsData timingsData = new TimingsData()
                    .setTime(time)
                    .setAvailability(getAvailability(time));
            TimingsData timingsData1 =new TimingsData()
                    .setTime(time1)
                    .setAvailability(getAvailability(time1));
            timings.add(timingsData);
            timings.add(timingsData1);
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView= findViewById(R.id.recycler_view);
        timingsAdapter = new TimingsAdapter(this, timings);
        recyclerView.setAdapter(timingsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //get Facility or Prof name to display on action bar
    private String getName() {
        return name;
    }

    private Boolean getAvailability(LocalDateTime timingGiven) {
        for (int i = 0; i < blockedTimings.size(); i ++) {
            if (timingGiven.toString().equals(blockedTimings.get(i))) {
                return false;
            }
        }
        return true;
    }

}



