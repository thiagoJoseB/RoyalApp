package com.example.royalapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.example.royalapp.remote.request.NovaSenhaPerfil;
import com.example.royalapp.remote.response.Resultado;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlterarSenhaPerfil extends AppCompatActivity {

    EditText txtSenhaAtual;
    EditText txtNovaSenha;
    EditText txtConfirmarSenha;
    Button btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar_senha_perfil);

        txtSenhaAtual = findViewById(R.id.txtSenhaAtual);
        txtNovaSenha = findViewById(R.id.txtNovaSenha);
        txtConfirmarSenha = findViewById(R.id.txtConfirmarNovaSenha);
        btnConfirmar = findViewById(R.id.btnConfirmarSenhaPerfil);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        this.btnConfirmar.setOnClickListener(view -> {

            if(!validade()){

                Toast.makeText(this, "Os Campos Devem Ser Preenchidos", Toast.LENGTH_LONG).show();
                return;

            }else{

                NovaSenhaPerfil novaSenhaPerfil = new NovaSenhaPerfil();
                if(txtConfirmarSenha.getText().toString().equals(txtNovaSenha.getText().toString())){

                    novaSenhaPerfil.setNova(txtNovaSenha.getText().toString());
                    novaSenhaPerfil.setAntiga(txtSenhaAtual.getText().toString());

                    novaSenha(novaSenhaPerfil);

                }else{
                    Toast.makeText(AlterarSenhaPerfil.this,
                            "Senha Incorreta",Toast.LENGTH_LONG).show();

                }

            }


        });





    }

    public void novaSenha(NovaSenhaPerfil novaSenhaPerfil){


        Call<Resultado> call = API.get().novaSenha(novaSenhaPerfil, DashboardActivity.token);   ////DashboardActivity.token, 2022, 5
        call.enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                if (response.isSuccessful()){

                    Toast.makeText(AlterarSenhaPerfil.this,
                            "Senha alterada",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

                Log.d("API-ERRO",t.getMessage());

            }
        });
    }




    public boolean validade(){

        return(
                !txtNovaSenha.getText().toString().isEmpty() &&
                !txtSenhaAtual.getText().toString().isEmpty()
                );
    }



}