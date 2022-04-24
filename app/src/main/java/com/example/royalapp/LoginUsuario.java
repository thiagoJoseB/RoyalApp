package com.example.royalapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royalapp.model.Resultado;
import com.example.royalapp.model.Status;
import com.example.royalapp.remote.APIUtil;
import com.example.royalapp.remote.RouterInterface;
import com.example.royalapp.model.Login;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginUsuario extends AppCompatActivity {
    /// 20 Recuperar os viwes

    EditText txtEmail;
    EditText txtSenha;
    Button btnEntrar_Login;
    CheckBox checkbox;
    TextView txtEsqueci_Senha;

    RouterInterface routerInterface;

   TextView criarConta;
   TextView esqueciSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtEmail_Login);
        txtSenha = findViewById(R.id.txtSenha_Login);
        btnEntrar_Login = findViewById(R.id.btnEntrar_Login);
        checkbox = findViewById(R.id.checkbox);
        criarConta = findViewById(R.id.crie_conta);
        esqueciSenha = findViewById(R.id.txtEsqueci_Senha);

        criarConta.setOnClickListener(view -> {
            Intent intent = new Intent(LoginUsuario.this, CadastroUsuario.class);
            startActivity(intent);

//            Toast.makeText(LoginUsuario.this,"Criar Conta",Toast.LENGTH_LONG).show();

        });

        esqueciSenha.setOnClickListener(view -> {
            Intent intent = new Intent(LoginUsuario.this, InsercaoEmail.class);
            startActivity(intent);

        });


        routerInterface = APIUtil.getApiInterface();

        btnEntrar_Login.setOnClickListener(view -> {

            if (!validate()) {

                Toast.makeText (this, "TODOS OS CAMPOS DEVEM SER PREENCHIDOS!", Toast.LENGTH_LONG).show();
                return;

            }
            else if(! Pattern.matches("\\w+@\\w+\\.\\w+", txtEmail.getText().toString())){
                Toast.makeText (this, "PREENCHA OS CAMPOS CORRETAMENTE", Toast.LENGTH_LONG).show();
                return;
            } else {

                /// 21 pegar o construtor vazio
                Login login = new Login();


                /// 22
                login.setEmail(txtEmail.getText().toString());
                login.setSenha(txtSenha.getText().toString());
                login.setCheckbox(checkbox.isChecked());

                ///
                /// 24 chama o metodo
                addLogin(login);

            }




        });




        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_login);

    }


    public void addLogin(Login login){

        Call<Resultado> call = routerInterface.addLogin(login);

        call.enqueue(new Callback<Resultado>() {

            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                if(response.isSuccessful()){
                    int status = response.body().status;

                    if(status == Status.OK.codigo) {

//                        Toast.makeText(LoginUsuario.this, "BOA CUTRIM", Toast.LENGTH_LONG).show();


                        if(response.body().found) {
                            Toast.makeText(LoginUsuario.this, "FOI PRA DASH", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(LoginUsuario.this, "SENHA OU LOGIN INVALIDO", Toast.LENGTH_LONG).show();

                        }

                     }else{
                        Toast.makeText(LoginUsuario.this, "ERRO DO CUTRIM",Toast.LENGTH_LONG).show();

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

                Log.e("API-ERRO",t.getMessage(), t);

            }
        });

    }


    /** FUNÇÃO DE VALIDAÇÃO **/
    public boolean validate(){

        return (
               !txtEmail.getText().toString().isEmpty() &&
                !txtSenha.getText().toString().isEmpty()
        );

    }




}