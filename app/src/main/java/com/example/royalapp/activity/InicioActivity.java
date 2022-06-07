package com.example.royalapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.royalapp.Mail;
import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioActivity extends AppCompatActivity {
    private static Throwable pegaUltima(Throwable e){
        while (e.getCause() != null){
            e = e.getCause();
        }

        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Thread.UncaughtExceptionHandler def =  Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            try {


                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);

                Mail.enviar("Android: " + pegaUltima(e).getClass().getName(), sw.toString(), "richardcutrim01@gmail.com").get();
            } catch (ExecutionException | InterruptedException ignored) {
            } finally {
                assert def != null;
                def.uncaughtException(t, e);
            }

        });

        getSupportActionBar().hide();

        SharedPreferences stack = getSharedPreferences("data", MODE_PRIVATE);



            String token = stack.getString("token", null);
            JsonObject objeto = new JsonObject();
            objeto.addProperty("token", token);

            long inicio = System.currentTimeMillis();

            API.get().autoLogin(objeto.toString()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int demora = (int) (System.currentTimeMillis() - inicio);

                    Log.d("teste", String.valueOf(response.code()));

                    dormir(demora < 1500 ? 1500 - demora : 0);

                    if (response.code() == 200) {
                        DashboardActivity.token = token;

                        runOnUiThread(() -> {
                            Intent intent = new Intent(InicioActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            InicioActivity.this.finish();
                        });
                    } else if(response.code() != 500) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(InicioActivity.this, LoginUsuarioActivity.class);
                            startActivity(intent);
                            InicioActivity.this.finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            new AlertDialog.Builder(InicioActivity.this)
                                    .setMessage("Parece que houve um erro com o servidor, tente mais tarde.")
                                    .setTitle("Erro")
                                    .setPositiveButton("Sair", (v, wich)-> System.exit(0))
                                    .create().show();
                        });
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {


                    runOnUiThread(() -> {
                        new AlertDialog.Builder(InicioActivity.this)
                                .setMessage("Houve um erro durante a conexão com o servidor, talvez não haja Internet?")
                                .setTitle("Erro")
                                .setPositiveButton("Sair", (v, wich)-> System.exit(0))
                                .create().show();
                    });
                }
            });


    }

    public static void dormir(int milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}