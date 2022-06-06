package com.example.royalapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.example.royalapp.remote.request.Codigo;
import com.example.royalapp.remote.response.Resultado;
import com.example.royalapp.remote.Status;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import com.example.royalapp.remote.model.InserirEmail;
public class InserirCodigoActivity extends AppCompatActivity {


    /// 20 Recuperar os viwes
    EditText txtCod1;
    EditText txtCod2;
    EditText txtCod3;
    EditText txtCod4;
    EditText txtCod5;
    EditText txtCod6;
    String email;
//    EditText txtEmail;

    Button btnInserirCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_codigo);

        txtCod1 = findViewById(R.id.txtCodigo1);
        txtCod2 = findViewById(R.id.txtCodigo2);
        txtCod3 = findViewById(R.id.txtCodigo3);
        txtCod4 = findViewById(R.id.txtCodigo4);
        txtCod5 = findViewById(R.id.txtCodigo5);
        txtCod6 = findViewById(R.id.txtCodigo6);
        btnInserirCodigo = findViewById(R.id.btnInsercaoCodigo);
//        txtEmail = findViewById(R.id.txtInsercaoEmail);

        txtCod1.addTextChangedListener(eventoIrProximo(txtCod2));
        txtCod2.addTextChangedListener(eventoIrProximo(txtCod3));
        txtCod3.addTextChangedListener(eventoIrProximo(txtCod4));
        txtCod4.addTextChangedListener(eventoIrProximo(txtCod5));
        txtCod5.addTextChangedListener(eventoIrProximo(txtCod6));



//        btnInserirCodigo.setOnClickListener(view -> {
//
////            Toast.makeText(LoginUsuario.this,"Criar Conta",Toast.LENGTH_LONG).show();
//
//        });



        btnInserirCodigo.setOnClickListener(view -> {


            if(!validate()){
                Toast.makeText (this, "TODOS OS CAMPOS DEVEM SER PREENCHIDOS!", Toast.LENGTH_LONG).show();
                return;

            }else{

                /// 21 pegar o construtor vazio
                Codigo codigo = new Codigo();

                /// 22
                codigo.setCodigo(txtCod1.getText().toString() + txtCod2.getText().toString() + txtCod3.getText().toString()
                + txtCod4.getText().toString() + txtCod5.getText().toString() + txtCod6.getText().toString());


               //// recebendo a chave da intent , chave que recebe o valor
                email = getIntent().getStringExtra("email");

//                Log.d("thiagao", String.valueOf(valor));

                codigo.setEmail(email);



//                codigo.setEmail(InsercaoEmail.getEmail());
                ///24
                addCodigo(codigo);







            }


        });



        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





    }

    public void addCodigo(Codigo codigo){

        Call<Resultado> call = API.get().addCodigo(codigo);

        call.enqueue(new Callback<Resultado>() {

            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                if(response.isSuccessful()){
                    int status = response.body().status;

                    if(status == Status.OK.codigo) {

                        if(response.body().reset) {
//                            Toast.makeText(InserirCodigoActivity.this, "BOA CUTRIM", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(InserirCodigoActivity.this, AlterarSenhaActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(InserirCodigoActivity.this, "NAO EXISTE ESSE EMAIL PARA RESETAR", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else{
                        Toast.makeText(InserirCodigoActivity.this, "ERRO AO ENVIAR CODIGO ",Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(InserirCodigoActivity.this, "ERRO AO ENVIAR CODIGO", Toast.LENGTH_SHORT).show();
                }


            }


            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

//                Log.e("API-ERRO",t.getMessage(), t);
                Toast.makeText(InserirCodigoActivity.this, "ERRO AO ENVIAR CODIGO", Toast.LENGTH_SHORT).show();

            }
        });




    }

    /** FUNÇÃO DE VALIDAÇÃO **/
    public boolean validate(){

        return (
                !txtCod1.getText().toString().isEmpty() &&
                        !txtCod2.getText().toString().isEmpty() &&
                        !txtCod3.getText().toString().isEmpty() &&
                        !txtCod4.getText().toString().isEmpty() &&
                        !txtCod5.getText().toString().isEmpty() &&
                        !txtCod6.getText().toString().isEmpty()

        );

    }

    public static TextWatcher eventoIrProximo(EditText proxima){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.length() > 1){
//                    p
//                    proxima.setText(charSequence.toString().substring(1));
//                }

                proxima.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }
}