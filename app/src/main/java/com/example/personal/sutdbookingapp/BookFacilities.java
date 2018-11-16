package com.example.personal.sutdbookingapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class BookFacilities extends AppCompatActivity {

    private ArrayList<Bookable> facilities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_facilities);
        initImage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private void initImage() {
        for (int i=0; i<50; i++) {
            Bookable facility = new Bookable();
            facility.setName("Facility "+i);
            facility.setImage("http://www.sunandmoonhotel.com/uploads/images/Gallery/Meeting-Room-Board-Room-Gallery/meeting-room-g1.jpg");
            facilities.add(facility);
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView= findViewById(R.id.recycler_view);
        ListAdapter listAdapter = new ListAdapter(facilities, this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
