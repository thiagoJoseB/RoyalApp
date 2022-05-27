package com.example.royalapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        getSupportActionBar().hide();


        Executors.newSingleThreadExecutor().execute(() -> {
            SharedPreferences stack = getSharedPreferences("data", MODE_PRIVATE);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (stack.contains("token")) {
                String token = stack.getString("token", null);
                JsonObject objeto = new JsonObject();
                objeto.addProperty("token", token);

                Response<String> body;

                Log.d("teste", objeto.toString());

                try {
                    body = API.get().autoLogin(objeto.toString()).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Log.d("teste", String.valueOf(body.code()));
                Log.d("teste", Objects.toString(body.body()));

                if (body.code() == 200) {
                    DashboardActivity.token = token;

                    runOnUiThread(() -> {
                        Intent intent = new Intent(this, DashboardActivity.class);
                        startActivity(intent);
                        this.finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(this, LoginUsuarioActivity.class);
                        startActivity(intent);
                        this.finish();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    Intent intent = new Intent(this, LoginUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                });
            }
        });


    }
}