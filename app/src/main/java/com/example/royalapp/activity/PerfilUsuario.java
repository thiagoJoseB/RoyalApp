package com.example.royalapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.example.royalapp.remote.request.PerfilDoUsuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilUsuario extends AppCompatActivity {

    EditText txtNomeCompleto;
    EditText txtEmailPerfil;
    Button btnTrocarSenha;
    Button btnSalvarPerfil;
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
        btnTrocarSenha = findViewById(R.id.btnTrocarSenhaPerfil);
        btnSalvarPerfil = findViewById(R.id.btnSalvarPerfil);
        checkboxPerfil = findViewById(R.id.checkboxPerfil);

        menuBaixo = findViewById(R.id.perfil_menu_baixo);
        menuBaixo.setSelectedItemId(R.id.menu_baixo_perfil);

        menuBaixo.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_baixo_geral: {
                    Intent intent = new Intent(PerfilUsuario.this, DashboardActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_extratos: {
                    Intent intent = new Intent(PerfilUsuario.this, ExtratoUsuarioActivity.class);
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


        btnSalvarPerfil.setOnClickListener(view -> {

            PerfilDoUsuario perfilDoUsuario = new PerfilDoUsuario();

                perfilDoUsuario.getNome();
                perfilDoUsuario.getEmail();
                perfilDoUsuario.setDuasestapas(checkboxPerfil.isChecked());


                addPerfilUsuario(perfilDoUsuario);









                });




    }

    public void addPerfilUsuario(PerfilDoUsuario perfilDoUsuario){

        Call<String> call = API.get().getPerfil(DashboardActivity.token);


      call.enqueue(new Callback<String>() {
          @Override
          public void onResponse(Call<String> call, Response<String> response) {
              if(response.isSuccessful()){
                  Toast.makeText(PerfilUsuario.this,"Perfil Aleterado",Toast.LENGTH_LONG).show();
              }

          }

          @Override
          public void onFailure(Call<String> call, Throwable t) {

              Log.d("API-ERRO",t.getMessage());


          }
      });



    }


         ////FUNCAO DE VALIDADE
        public boolean validate () {
            return (
                    !txtNomeCompleto.getText().toString().isEmpty() &&
                            !txtEmailPerfil.getText().toString().isEmpty()
            );

        }
    }





