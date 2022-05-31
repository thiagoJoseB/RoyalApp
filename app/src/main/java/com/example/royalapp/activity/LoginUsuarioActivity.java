package com.example.royalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.example.royalapp.remote.Status;
import com.example.royalapp.remote.request.Login;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginUsuarioActivity extends AppCompatActivity {
    /// 20 Recuperar os viwes

    EditText txtEmail;
    EditText txtSenha;
    Button btnEntrar_Login;
    CheckBox checkbox;
    TextView txtEsqueci_Senha;


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
            Intent intent = new Intent(LoginUsuarioActivity.this, CadastroUsuarioActivity.class);
            startActivity(intent);

//            Toast.makeText(LoginUsuario.this,"Criar Conta",Toast.LENGTH_LONG).show();

        });

        esqueciSenha.setOnClickListener(view -> {
            Intent intent = new Intent(LoginUsuarioActivity.this, InsercaoEmailActivity.class);
            startActivity(intent);

        });


        btnEntrar_Login.setOnClickListener(view -> {

            if (!validate()) {

                Toast.makeText(this, "TODOS OS CAMPOS DEVEM SER PREENCHIDOS!", Toast.LENGTH_LONG).show();
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
        getSupportActionBar().setCustomView(R.layout.toolbar_app);

    }


    public void addLogin(Login login) {


        API.get().login(login).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("teste", "cacildes");

                if (response.isSuccessful()) {
                    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

                    Log.d("teste", json.toString());

                    if (json.get("status").getAsInt() == Status.OK.codigo) {
                        if (json.get("found").getAsBoolean()) {
                            Toast.makeText(LoginUsuarioActivity.this, "FOI PRA DASH", Toast.LENGTH_LONG).show();

                            String token = json.get("token").getAsString();

                            Intent intent = new Intent(LoginUsuarioActivity.this, DashboardActivity.class);
                            DashboardActivity.token = token;

                            if(checkbox.isChecked()){
                                getSharedPreferences("data", MODE_PRIVATE).edit().putString("token", token).apply();
                                Log.d("teste", "a terra é plana");
                            }

                            LoginUsuarioActivity.this.startActivity(intent);

                            LoginUsuarioActivity.this.finish();
                        } else {
                            Toast.makeText(LoginUsuarioActivity.this, "SENHA OU LOGIN INVALIDO", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(LoginUsuarioActivity.this, "ERRO DO CUTRIM", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(LoginUsuarioActivity.this, "erro" + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("login", t.getClass().getSimpleName(), t);
                Toast.makeText(LoginUsuarioActivity.this, "erro" + t, Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * FUNÇÃO DE VALIDAÇÃO
     **/
    public boolean validate() {

        return (
                !txtEmail.getText().toString().isEmpty() &&
                        !txtSenha.getText().toString().isEmpty()
        );

    }


}