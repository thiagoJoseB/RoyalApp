package com.example.royalapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.royalapp.remote.APIUtil;
import com.example.royalapp.remote.RouterInterface;
import com.example.royalapp.model.Login;


public class LoginUsuario extends AppCompatActivity {
    /// 20 Recuperar os viwes

    EditText txtEmail;
    EditText txtSenha;
    Button btnEntrar_Login;

    RouterInterface routerInterface;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtEmail_Login);
        txtSenha = findViewById(R.id.txtSenha_Login);
        btnEntrar_Login = findViewById(R.id.btnEntrar_Login);

//        routerInterface = APIUtil.getLoginInterface()Interface();

        btnEntrar_Login.setOnClickListener(view -> {

            /// 21 pegar o construtor vazio
            Login login = new Login();


            /// 22
            login.setEmail(txtEmail.getText().toString());
            login.setSenha(txtSenha.getText().toString());





        });




        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_login);

    }




}