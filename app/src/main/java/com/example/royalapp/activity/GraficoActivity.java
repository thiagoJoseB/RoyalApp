package com.example.royalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.royalapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GraficoActivity extends AppCompatActivity {


    private BottomNavigationView menuBaixo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        menuBaixo = this.findViewById(R.id.grafico_menu_baixo);
        menuBaixo.setSelectedItemId(R.id.menu_baixo_graficos);

        menuBaixo.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_baixo_geral:{
                    Intent intent = new Intent(this, DashboardActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_graficos:
                    break;
                case R.id.menu_baixo_extratos: {
                    Intent intent = new Intent(this, ExtratoUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_perfil: {
                    Intent intent = new Intent(this, PerfilUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
            }

            return false;
        });
    }
}