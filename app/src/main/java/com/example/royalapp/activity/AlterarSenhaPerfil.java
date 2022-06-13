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
import com.example.royalapp.remote.Status;
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



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       txtSenhaAtual = findViewById(R.id.txtSenhaAtual);
        txtNovaSenha = findViewById(R.id.txtNovaSenhaPerfil);
        txtConfirmarSenha = findViewById(R.id.txtConfirmarNovaSenhaPerfil);
       btnConfirmar = findViewById(R.id.btnConfirmarSenhaPerfil);



        btnConfirmar.setOnClickListener(view -> {

            if(!validade()){

                Toast.makeText(this, "Os campos devem ser preenchidos", Toast.LENGTH_LONG).show();
                return;

            }else{


                NovaSenhaPerfil novaSenhaPerfil = new NovaSenhaPerfil();
                System.out.println("testtetste");

                if(txtConfirmarSenha.getText().toString().equals(txtNovaSenha.getText().toString())){
                    System.out.println("cutrim");

                    novaSenhaPerfil.setNova(txtNovaSenha.getText().toString());
                    novaSenhaPerfil.setAntiga(txtSenhaAtual.getText().toString());

                    System.out.println("aquiii");

                    novaSenha(novaSenhaPerfil);

                    System.out.println("longe");

                }else{
                    Toast.makeText(AlterarSenhaPerfil.this,
                            "Senha incorreta",Toast.LENGTH_LONG).show();

                }

            }


        });





    }

    public void novaSenha(NovaSenhaPerfil novaSenhaPerfil){


        Call<Resultado> call = API.get().novaSenha(novaSenhaPerfil, DashboardActivity.token);   ////DashboardActivity.token, 2022, 5
        call.enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                    if(response.body().status == Status.OK.codigo){
                        Toast.makeText(AlterarSenhaPerfil.this,
                                "Senha alterarda com sucesso",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AlterarSenhaPerfil.this, "Login ou senha incorretos", Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {
                throw new RuntimeException(t);
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