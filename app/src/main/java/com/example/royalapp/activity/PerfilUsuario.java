package com.example.royalapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.royalapp.R;

public class PerfilUsuario extends AppCompatActivity {

    EditText txtNomeCompleto;
    EditText txtEmailPerfil;
    Button btnTrocarSenha;
    CheckBox checkboxPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_transferencia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNomeCompleto = findViewById(R.id.txtNomeCompleto);
        txtEmailPerfil = findViewById(R.id.txtEmailPerfil);
        btnTrocarSenha = findViewById(R.id.btnTrocarSenhaPerfil);
        checkboxPerfil = findViewById(R.id.checkboxPerfil);









    }
}