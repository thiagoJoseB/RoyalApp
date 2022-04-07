package com.example.royalapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;


public class AlterarSenha extends AppCompatActivity {

    private EditText txtNovaSenha;
    private EditText txtConfirmarNovaSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

       txtNovaSenha = findViewById(R.id.txtNovaSenha);
       txtConfirmarNovaSenha = findViewById(R.id.txtConfirmarNovaSenha);



//        public void afterTextChanged() {
//
//            double charSpace = 1.75;
//            double spaceLeft = charSpace * 1.75;
//            if((txtNovaSenha.length() <= 8) && (txtNovaSenha.toString()  spaceLeft < (charSpace * 1.75)){
//
//            }
//        }



        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_alterar_senha);



        /////

    }


}