package com.example.royalapp.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.royalapp.Constantes;
import com.example.royalapp.Extra;
import com.example.royalapp.R;
import com.example.royalapp.Utilidades;
import com.example.royalapp.Variaveis;
import com.example.royalapp.remote.API;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    MATCH_PARENT,
                    0
            );
            layoutParams.dimensionRatio = "9:16";


            Extra.rodar(() -> {
                Looper.prepare();

                List<BigDecimal> decimals;

                String tipo = receita ? (despesa ? "tudo": "receita") : "despesa";

                if(mes != null){
                    decimals = API.get().graficoMensal2(tipo, DashboardActivity.token, ano, mes + 1, idCategorias).execute().body();
                } else {
                    decimals = API.get().graficoAnual2(tipo, DashboardActivity.token, ano, idCategorias).execute().body();
                }

                View view;

                switch (modo){
                    case LINHA:{
                        LineChart chart = new LineChart(this);
                        LineDataSet dataSet = new LineDataSet(new ArrayList<>(), "");
                        LineData lineData = new LineData(dataSet);

                        dataSet.setValueFormatter(FORMATADOR_PADRAO);

                        chart.setLayoutParams(layoutParams);

                        chart.setFocusable(false);
                        chart.setScaleEnabled(false);

                        chart.setData(lineData);

                        chart.getXAxis().setGranularity(1);
                        chart.getXAxis().setAxisMinimum(0);

                        if(!"tudo".equals(tipo)){
                            chart.getAxis(YAxis.AxisDependency.LEFT).setAxisMinimum(0f);
                        }

                        if(mes == null){
                            chart.getXAxis().setValueFormatter(FORMATADOR_MESES);
                        }

                        for(int i = 0; i < decimals.size(); i++){
                            lineData.addEntry(new Entry(i, decimals.get(i).floatValue()), 0);
                        }

                        view = chart;
                        break;
                    }
                    case BARRA:{
                        BarChart chart = new BarChart(this);
                        BarDataSet dataSet = new BarDataSet(new ArrayList<>(), "");
                        BarData lineData = new BarData(dataSet);

                        dataSet.setValueFormatter(FORMATADOR_PADRAO);

                        chart.setLayoutParams(layoutParams);

                        chart.setFocusable(false);
                        chart.setScaleEnabled(false);

                        chart.setData(lineData);


                        chart.getXAxis().setGranularity(1);
                        chart.getXAxis().setAxisMinimum(0);

                        if(!"tudo".equals(tipo)){
                            chart.getAxis(YAxis.AxisDependency.LEFT).setAxisMinimum(0f);
                        }

                        if(mes == null){
                            chart.getXAxis().setValueFormatter(FORMATADOR_MESES);
                        }

                        for(int i = 0; i < decimals.size(); i++){
                            lineData.addEntry(new BarEntry(i, decimals.get(i).floatValue()), 0);
                        }

                        view = chart;
                        break;
                    }
                    case PIZZA:{
                        PieChart chart = new PieChart(this);
                        PieDataSet dataSet = new PieDataSet(new ArrayList<>(), "");
                        PieData lineData = new PieData(dataSet);



                        chart.setLayoutParams(layoutParams);

                        chart.setFocusable(false);
                        chart.setUsePercentValues(false);
                        chart.getDescription().setEnabled(false);

                        chart.setDrawHoleEnabled(false);

                        chart.setDrawCenterText(false);
                        chart.setRotationEnabled(false);
                        chart.setHighlightPerTapEnabled(false);
                        chart.setMinAngleForSlices(0f);

                        ((PieDataSet) lineData.getDataSet()).setColors(IntStream.of(ColorTemplate.PASTEL_COLORS).boxed().collect(Collectors.toList()));
                        ((PieDataSet) lineData.getDataSet()).setSliceSpace(0);

                        chart.setData(lineData);

                        if(mes == null){

                            for(int i = 0; i < decimals.size(); i++){
                                lineData.addEntry(new PieEntry(decimals.get(i).floatValue(), Constantes.MESES[i]), 0);
                            }
                        } else {
                            for(int i = 0; i < decimals.size(); i++){
                                lineData.addEntry(new PieEntry(decimals.get(i).floatValue(), "Dia " + i), 0);
                            }
                        }

                        dataSet.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getPieLabel(float value, PieEntry pieEntry) {
                                if(value <= 0f){

                                    dataSet.removeEntry(pieEntry);
                                    return "";
                                }

                                return Utilidades.FORMATADOR_MOEDA.format(value);
                            }
                        });

                        view = chart;
                        break;
                    }
                    default:{
                        throw new IllegalArgumentException("algo deu mto errado");
                    }
                }









                viewContainer.addView(view);
                viewCarregando.setVisibility(View.GONE);
                viewContainer.setVisibility(View.VISIBLE);

            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    private static final ValueFormatter FORMATADOR_MESES = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return Constantes.MESES[(int) value];
        }
    };

    private static final ValueFormatter FORMATADOR_PADRAO = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return Utilidades.FORMATADOR_MOEDA.format(new BigDecimal(Float.toString(value)));
        }
    };
}