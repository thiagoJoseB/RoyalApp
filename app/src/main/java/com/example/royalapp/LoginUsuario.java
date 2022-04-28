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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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


            routerInterface.login(login).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        JsonObject json;

                        try {
                            json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Log.d("teste", json.toString());

                        if(json.get("status").getAsInt() == Status.OK.codigo) {
                            if(json.get("found").getAsBoolean()) {

                                Toast.makeText(LoginUsuario.this, "FOI PRA DASH", Toast.LENGTH_LONG).show();


                                Intent intent = new Intent(LoginUsuario.this, Dashboard.class);
                                intent.putExtra("token", json.get("token").getAsString());

                                LoginUsuario.this.startActivity(intent);

                                LoginUsuario.this.finish();
                            }else{
                                Toast.makeText(LoginUsuario.this, "SENHA OU LOGIN INVALIDO", Toast.LENGTH_LONG).show();

                            }

                        }else{
                            Toast.makeText(LoginUsuario.this, "ERRO DO CUTRIM",Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(LoginUsuario.this, "erro" + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("login", t.getClass().getSimpleName(), t);
                    Toast.makeText(LoginUsuario.this, "erro" + t.toString(), Toast.LENGTH_LONG).show();
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