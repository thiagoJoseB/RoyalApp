package com.example.royalapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilUsuarioActivity extends AppCompatActivity {

    EditText txtNomeCompleto;
    EditText txtEmailPerfil;
    CheckBox checkboxPerfil;

    private BottomNavigationView menuBaixo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_transferencia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNomeCompleto = findViewById(R.id.txtNomeCompleto);
        txtEmailPerfil = findViewById(R.id.txtEmailPerfil);
        checkboxPerfil = findViewById(R.id.checkboxPerfil);

        menuBaixo = findViewById(R.id.perfil_menu_baixo);
        menuBaixo.setSelectedItemId(R.id.menu_baixo_perfil);

        menuBaixo.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_baixo_geral: {
                    Intent intent = new Intent(PerfilUsuarioActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_extratos: {
                    Intent intent = new Intent(PerfilUsuarioActivity.this, ExtratoUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_perfil: {
                    break;
                }
            }

            return false;
        });

        API.get().getPerfil(DashboardActivity.token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

                txtNomeCompleto.setText(json.get("nome").getAsString());
                txtEmailPerfil.setText(json.get("email").getAsString());
                checkboxPerfil.setChecked(json.get("duasetapas").getAsBoolean());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });





        }

        public void deslogar(View a){
            new AlertDialog.Builder(this)
                    .setMessage("Deseja sair da sua conta?")
                    .setNegativeButton("Cancelar", (v, i) -> {})
                    .setPositiveButton("Sair", (v, i) -> {
                        DashboardActivity.token = null;

                        getSharedPreferences("data", MODE_PRIVATE).edit().remove("token").apply();

                        Intent intent = new Intent(PerfilUsuarioActivity.this, LoginUsuarioActivity.class);
                        startActivity(intent);
                        this.finish();
                    }).create().show();
        }
    }





