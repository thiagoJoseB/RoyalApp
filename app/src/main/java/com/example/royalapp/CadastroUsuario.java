package com.example.royalapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.royalapp.model.Cadastro;
import com.example.royalapp.model.Resultado;
import com.example.royalapp.model.Status;
import com.example.royalapp.remote.APIUtil;
import com.example.royalapp.remote.RouterInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CadastroUsuario extends AppCompatActivity {



    /*** ATRIBUTOS DE COMPONENTES DE INTERFACE GRAFICA**/
    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtConfirmarSenha;
    private Button btnCadastrar;


    ///25
   RouterInterface routerInterface;


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

       //// 26
        routerInterface = APIUtil.getCadastroInterface();

        this.btnCadastrar.setOnClickListener(view -> {

            /// FUNCAO DE VALIDACAO DE CAMPOS PREENCHIDOS
            if (!validate()){

                Toast.makeText(this, "Os Campos Devem Ser Preenchidos", Toast.LENGTH_LONG).show();
                return;

            } else{

                /// 21 pegar o construtor vazio
                Cadastro cadastro = new Cadastro();

               if(txtConfirmarSenha.getText().toString().equals(txtSenha.getText().toString())){
                    cadastro.setNome(txtNome.getText().toString());
                    cadastro.setEmail(txtEmail.getText().toString());
                    cadastro.setSenha(txtSenha.getText().toString());



                }
                //22

                /// 24 chama o metodo
                addCadastro(cadastro);



            }

        });


         //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_cadastro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }  /// FIM DO METODO onCREATE

    //23
    public void addCadastro(Cadastro cadastro){

        Call<Resultado> call = routerInterface.addCadastro(cadastro);

        call.enqueue(new Callback<Resultado>() {

            ///se deu certo

            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                if(response.isSuccessful()){
                    int status = response.body().status;

                    if(status == Status.OK.codigo){
                        Toast.makeText(CadastroUsuario.this, "BOA CUTRIM",Toast.LENGTH_LONG).show();


                    } else if(status == Status.EMAIL_REPETIDO.codigo){
                        Toast.makeText(CadastroUsuario.this, " EMAIL REPETIDO CUTRIM",Toast.LENGTH_LONG).show();



                    } else {
                        Toast.makeText(CadastroUsuario.this, "ERRO DO CUTRIM",Toast.LENGTH_LONG).show();

                    }
//                    //Log.d("REPOSNSE-", String.valueOf(response.raw()));
//                    Toast.makeText(CadastroUsuario.this,
//                            "",
//                            Toast.LENGTH_LONG).show();
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