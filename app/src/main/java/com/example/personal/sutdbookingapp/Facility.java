package com.example.personal.sutdbookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Facility extends AppCompatActivity {
    public String facilityId;
    public final static String FACIL_ID = "FACIL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);
        Intent intent = getIntent();
        String name = intent.getStringExtra(FACIL_ID);
        setTitle(name);
    }
}
