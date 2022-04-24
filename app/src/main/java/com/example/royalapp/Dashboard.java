package com.example.royalapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        getSupportActionBar().setCustomView(R.layout.toolbar_circle);

       getSupportActionBar().hide();

       final DrawerLayout drawerLayout = findViewById(R.id.tela);

       findViewById(R.id.imgMenu).setOnClickListener( new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               drawerLayout.openDrawer(GravityCompat.START);
           }
       });

        NavigationView navigationView = findViewById(R.id.option);
        navigationView.setItemIconTintList(null);

    }
}