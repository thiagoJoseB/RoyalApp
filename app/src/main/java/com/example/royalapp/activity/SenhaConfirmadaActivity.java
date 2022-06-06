package com.example.royalapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.royalapp.R;

public class SenhaConfirmadaActivity extends AppCompatActivity {

    TextView btnCancelarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);


        btnCancelarSenha = findViewById(R.id.btnCancelarNovaSenha);


        btnCancelarSenha.setOnClickListener(view -> {
            Intent intent = new Intent(SenhaConfirmadaActivity.this, LoginUsuarioActivity.class);
            startActivity(intent);

        });










        //toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}