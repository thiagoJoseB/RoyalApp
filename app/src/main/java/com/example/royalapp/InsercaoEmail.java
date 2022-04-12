package com.example.royalapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royalapp.model.InserirEmail;
import com.example.royalapp.model.Resultado;
import com.example.royalapp.model.Status;
import com.example.royalapp.remote.APIUtil;
import com.example.royalapp.remote.RouterInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsercaoEmail extends AppCompatActivity {

    /// 20 Recuperar os viwes

    EditText txtInsercaoEmail;
    Button btnInsercaoEmail;
    private String tipo = "Pedir";

    RouterInterface routerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insercao_email);

        ///21
        InserirEmail inserirEmail = new InserirEmail();

        txtInsercaoEmail = findViewById(R.id.txtInsercaoEmail);
        btnInsercaoEmail = findViewById(R.id.btnInsercaoEmail);


        routerInterface = APIUtil.getUInserirEmailInterface();

        btnInsercaoEmail.setOnClickListener(view -> {

            /// 21 pegar o construtor vazio


            /// 22
            inserirEmail.setEmail(txtInsercaoEmail.getText().toString());
            inserirEmail.setTipo("Pedir");

            /// 24 chama o metodo
         addInserirEmail(inserirEmail);

        });


        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_inserir_email);


    } /// FIM DO METODO onCREATE


    ///23 metodo addUsuario
    public void addInserirEmail(InserirEmail inserirEmail){
                                    /// verbo , rota
        Call<Resultado> call = routerInterface.addInserirEmail(inserirEmail);

        call.enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                if(response.isSuccessful()){
                    int status = response.body().status;

                    if(status == Status.OK.codigo) {

                        Toast.makeText(InsercaoEmail.this,"foi Cutrim",Toast.LENGTH_LONG).show();


//                        if(response.body().found) {
//                            Toast.makeText(InsercaoEmail.this, "Cutrim", Toast.LENGTH_LONG).show();
//
//                        }

                    }else{
                        Toast.makeText(InsercaoEmail.this, "ERRO DO CUTRIM",Toast.LENGTH_LONG).show();

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





}