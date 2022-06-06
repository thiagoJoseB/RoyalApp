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
import com.example.royalapp.remote.request.Cadastro;
import com.example.royalapp.remote.response.Resultado;
import com.example.royalapp.remote.Status;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CadastroUsuarioActivity extends AppCompatActivity {



    /*** ATRIBUTOS DE COMPONENTES DE INTERFACE GRAFICA**/
    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtConfirmarSenha;
    private Button btnCadastrar;


    ///25


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_usuario);






        /// RECUPERAR O QUE É DIGITADO NOS CAMPOS
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        txtConfirmarSenha = findViewById(R.id.txtConfirmarSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);


        this.btnCadastrar.setOnClickListener(view -> {

            /// FUNCAO DE VALIDACAO DE CAMPOS PREENCHIDOS
            if (!validate()){

                Toast.makeText(this, "Os Campos Devem Ser Preenchidos", Toast.LENGTH_LONG).show();
                return;

            }
            else if(! Pattern.matches("\\w+@\\w+\\.\\w+", txtEmail.getText().toString())){
                Toast.makeText (this, "PREENCHA OS CAMPOS CORRETAMENTE", Toast.LENGTH_LONG).show();
                return;
            } else {

                /// 21 pegar o construtor vazio
                Cadastro cadastro = new Cadastro();

               if(txtConfirmarSenha.getText().toString().equals(txtSenha.getText().toString())){
                    cadastro.setNome(txtNome.getText().toString());
                    cadastro.setEmail(txtEmail.getText().toString());
                    cadastro.setSenha(txtSenha.getText().toString());


                   /// 24 chama o metodo
                   addCadastro(cadastro);

                }
                //22




            }

        });


         //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }  /// FIM DO METODO onCREATE

    //23
    public void addCadastro(Cadastro cadastro){

        Call<Resultado> call = API.get().addCadastro(cadastro);

        call.enqueue(new Callback<Resultado>() {

            ///se deu certo

            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                if(response.code() == 200 || response.code() == 204){
                    Toast.makeText(CadastroUsuarioActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    int status = response.body().status;

                    if(status == Status.OK.codigo){
//                        Toast.makeText(CadastroUsuarioActivity.this, "BOA CUTRIM",Toast.LENGTH_LONG).show();


                    } else if(status == Status.EMAIL_REPETIDO.codigo){
                        Toast.makeText(CadastroUsuarioActivity.this, " EMAIL INSERIDO JA EXISTE",Toast.LENGTH_LONG).show();



                    } else {
                        Toast.makeText(CadastroUsuarioActivity.this, "ERRO AO CADASTRAR",Toast.LENGTH_LONG).show();

                    }
                }


            }
            ///se deu erro
            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

                Log.d("API-ERRO",t.getMessage());

            }
        });



    }


    /// FUNCAO DE VALIDADE
    public boolean validate(){
        return (
                !txtNome.getText().toString().isEmpty() &&
                        !txtEmail.getText().toString().isEmpty() &&
                        !txtSenha.getText().toString().isEmpty() &&
                        !txtConfirmarSenha.getText().toString().isEmpty()
        );

    }

}