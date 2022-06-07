package com.example.royalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.royalapp.Extra;
import com.example.royalapp.R;
import com.example.royalapp.Variaveis;
import com.example.royalapp.remote.API;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GraficoResultadoActivity extends AppCompatActivity {

    ConstraintLayout viewContainer;
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

        Intent intent = getIntent();

        Integer mes = (Integer) intent.getSerializableExtra("mes");
        int ano = intent.getIntExtra("ano", -1429874893);
        GraficosActivity.Tipo modo = (GraficosActivity.Tipo) intent.getSerializableExtra("modo");
        int[] idCategorias = intent.getIntArrayExtra("categorias");
        boolean receita = intent.getBooleanExtra("receita", false);
        boolean despesa = intent.getBooleanExtra("despesa", false);

        try {
            Extra.rodar(() -> {
                List<BigDecimal> decimals = API.get().graficoMensal2("despesa", DashboardActivity.token, ano, 6, idCategorias).execute().body();
                
                BarChart chart = new BarChart(this);
                BarDataSet set = new BarDataSet(new ArrayList<>(), "teste");

                set.addEntry(new BarEntry(decimals.get(0).floatValue(), 33));

                viewCarregando.setVisibility(View.GONE);
                viewContainer.addView(chart);
                viewContainer.setVisibility(View.VISIBLE);

            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}