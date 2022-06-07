package com.example.royalapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.example.royalapp.remote.response.Resultado;
import com.example.royalapp.remote.request.SenhaNova;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlterarSenhaActivity extends AppCompatActivity {

    private EditText txtNovaSenha;
    private EditText txtConfirmarNovaSenha;
    private Button btnConfirmarSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

       txtNovaSenha = findViewById(R.id.txtNovaSenha);
       txtConfirmarNovaSenha = findViewById(R.id.txtConfirmarNovaSenha);
       btnConfirmarSenha = findViewById(R.id.btnConfirmarSenha);


        btnConfirmarSenha.setOnClickListener(view -> {

            /// FUNCAO DE VALIDACAO DE CAMPOS PREENCHIDOS
            if (!validate()){

                Toast.makeText(this, "Os campos devem ser preenchidos", Toast.LENGTH_LONG).show();
                return;

            }else{
                SenhaNova senhaNova = new SenhaNova();

                if (txtConfirmarNovaSenha.getText().toString().equals(txtNovaSenha.getText().toString())){
                    senhaNova.setSenha(txtNovaSenha.getText().toString());
                    senhaNova.setTipo("MUDAR");

                    //// recebendo a chave da intent , chave que recebe o valor
                    String valor = getIntent().getStringExtra("email");

//                Log.d("thiagao", String.valueOf(valor));

                    senhaNova.setEmail(valor);
                    Log.d("teste", valor);

                    addSenhaNova(senhaNova);
                }

            }


        });



        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);









//        public void afterTextChanged() {
//
//            double charSpace = 1.75;
//            double spaceLeft = charSpace * 1.75;
//            if((txtNovaSenha.length() <= 8) && (txtNovaSenha.toString()  spaceLeft < (charSpace * 1.75)){
//
//            }
//        }

    }

    public void addSenhaNova(SenhaNova senhaNova){
        Call<Resultado> call = API.get().addSenhaNova(senhaNova);

        call.enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                Log.d("teste", Integer.toString(response.code()));

                if(response.isSuccessful()){
//                    Toast.makeText(AlterarSenhaActivity.this, "BOA", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AlterarSenhaActivity.this, LoginUsuarioActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(AlterarSenhaActivity.this, "Erro ao confirmar nova senha", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {
//                Log.d("API-ERRO",t.getMessage());
                Toast.makeText(AlterarSenhaActivity.this, "Erro", Toast.LENGTH_SHORT).show();


            }
        });
    }

    /// FUNCAO DE VALIDADE
    public boolean validate(){

        return (
                        !txtNovaSenha.getText().toString().isEmpty() &&
                        !txtConfirmarNovaSenha.getText().toString().isEmpty()
        );

    }


}