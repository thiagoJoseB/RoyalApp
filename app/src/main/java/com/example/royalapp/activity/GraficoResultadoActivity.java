package com.example.royalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.royalapp.R;

public class GraficoResultadoActivity extends AppCompatActivity {

    LinearLayout viewContainer;
    View viewCarregando;

    private void instanciadores(){
        viewContainer = this.findViewById(R.id.grafico_resultado_container);
        viewCarregando = this.findViewById(R.id.grafico_resultado_carregando);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_grafico_resultado);

        instanciadores();

        new Thread(() -> {


//            switch (){
//
//            }
        }).start();
    }
}