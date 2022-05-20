package com.example.royalapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.royalapp.remote.API;
import com.example.royalapp.remote.request.InserirEmail;
import com.example.royalapp.remote.response.Resultado;
import com.example.royalapp.remote.Status;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsercaoEmailActivity extends AppCompatActivity {

    /// 20 Recuperar os viwes

    EditText txtInsercaoEmail;
    Button btnInsercaoEmail;
    private final String tipo = "PEDIR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insercao_email);



        txtInsercaoEmail = findViewById(R.id.txtInsercaoEmail);
        btnInsercaoEmail = findViewById(R.id.btnInsercaoEmail);


        btnInsercaoEmail.setOnClickListener(view -> {

            if (!validate()){

                Toast.makeText (this, "TODOS OS CAMPOS DEVEM SER PREENCHIDOS!", Toast.LENGTH_LONG).show();
                return;


            }else if(! Pattern.matches("\\w+@\\w+\\.\\w+", txtInsercaoEmail.getText().toString())){
                Toast.makeText (this, "PREENCHA OS CAMPOS CORRETAMENTE", Toast.LENGTH_LONG).show();
                return;
            } else {



                ///21
                InserirEmail inserirEmail = new InserirEmail();



                /// 22

                inserirEmail.setEmail(txtInsercaoEmail.getText().toString());

                inserirEmail.setTipo("PEDIR");

                /// 24 chama o metodo
                addInserirEmail(inserirEmail);



            }

        });




        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_app);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    } /// FIM DO METODO onCREATE


    ///23 metodo addUsuario
    public void addInserirEmail(InserirEmail inserirEmail){
        /// verbo , rota
        Call<Resultado> call = API.get().addInserirEmail(inserirEmail);

        call.enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                if(response.isSuccessful()){
                    int status = response.body().status;

                    if(status == Status.OK.codigo) {

                            Intent intent = new Intent(InsercaoEmailActivity.this, InserirCodigoActivity.class);
                            intent.putExtra("email", txtInsercaoEmail.getText().toString());
                            startActivity(intent);
                            finish();

                    }else{
                        Toast.makeText(InsercaoEmailActivity.this, "ERRO DO CUTRIM",Toast.LENGTH_LONG).show();

                    }
//                    //Log.d("REPOSNSE-", String.valueOf(response.raw()));
//                    Toast.makeText(CadastroUsuario.this,
//                            "",
//                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {
                Log.e("API-ERRO",t.getMessage(), t);

            }
        });


    }

    /** FUNÇÃO DE VALIDAÇÃO **/
    public boolean validate(){

        return (
                !txtInsercaoEmail.getText().toString().isEmpty()
        );

    }





}