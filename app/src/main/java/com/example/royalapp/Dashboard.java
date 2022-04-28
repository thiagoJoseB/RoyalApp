package com.example.royalapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.royalapp.remote.APIUtil;
import com.example.royalapp.remote.response.DashboardData;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.navigation.NavigationView;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.text.NumberFormat;
import java.util.ArrayList;

import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    private String token;
    private TextView viewTextSaldoGeral;
    private TextView viewTextDespesaGeral;
    private TextView viewTextReceitaGeral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        getSupportActionBar().setCustomView(R.layout.toolbar_circle);

        getSupportActionBar().hide();
        viewTextSaldoGeral = this.findViewById(R.id.dashboard_saldo_principal_texto);
        viewTextReceitaGeral = this.findViewById(R.id.dashboard_receita_principal_texto);
        viewTextDespesaGeral = this.findViewById(R.id.dashboard_despesa_principal_texto);

        //pega o token da tela de login
        token = this.getIntent().getStringExtra("token");

        APIUtil.getApiInterface().getDashboardInfo(token).enqueue(new Callback<DashboardData>() {
            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                DashboardData data = response.body();

                viewTextSaldoGeral.setText(NumberFormat.getCurrencyInstance().format(data.saldo.longValue()));
                ShimmerFrameLayout viewEfeitoSaldoGeral = findViewById(R.id.dashboard_saldo_principal_efeito);
                viewEfeitoSaldoGeral.stopShimmer();
                viewEfeitoSaldoGeral.setVisibility(View.GONE);

                viewTextReceitaGeral.setText(NumberFormat.getCurrencyInstance().format(data.receita.longValue()));
                ShimmerFrameLayout viewEfeitoReceitaGeral = findViewById(R.id.dashboard_receita_principa_efeito);
                viewEfeitoReceitaGeral.stopShimmer();
                viewEfeitoReceitaGeral.setVisibility(View.GONE);

                viewTextDespesaGeral.setText(NumberFormat.getCurrencyInstance().format(data.despesa.longValue()));
                ShimmerFrameLayout viewEfeitoDespesaGeral = findViewById(R.id.dashboard_despesa_principal_efeito);
                viewEfeitoDespesaGeral.stopShimmer();
                viewEfeitoDespesaGeral.setVisibility(View.GONE);


                Log.d("teste", viewTextSaldoGeral.getParent().getClass().getName());

                Toast.makeText(Dashboard.this, "carregou", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                Log.e("teste", t.getClass().getName(), t);
            }
        });

        //sabe oq fazer? ce vai colocar todas as categorias naquele select, tipo: viagem e lazer pra despesa na hora
        //de selecionar, ai aquele numero 1 vira o arruma isso primeiro

        final DrawerLayout drawerLayout = findViewById(R.id.tela);

        findViewById(R.id.imgMenu).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.option);
        navigationView.setItemIconTintList(null);


        //inicia o codigo do piechard

        PieChart chart = findViewById(R.id.dashboard_grafico_mes);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);


        chart.setDrawHoleEnabled(false);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);


        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener


        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);


        //RICHARD - o valor será ejetado nessa arrayList
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(1f, "teste"));
        entries.add(new PieEntry(1f, "teste"));
        entries.add(new PieEntry(1f, "teste"));


        PieDataSet dataSet = new PieDataSet(entries, "valor pra ser mudado");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }


        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

    }


}