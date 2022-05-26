package com.example.royalapp.activity;

import static com.example.royalapp.Utilidades.CALENDARIO;
import static com.example.royalapp.Utilidades.FORMATADOR_MOEDA;
import static com.example.royalapp.Utilidades.GSON;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.royalapp.R;
import com.example.royalapp.Utilidades;
import com.example.royalapp.model.Categoria;
import com.example.royalapp.remote.API;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {





    public static WebSocket webSocket;
    public static final List<Categoria> despesas = new ArrayList<>();
    public static final List<Categoria> receitas = new ArrayList<>();
    static String token = null;



    private TextView viewTextSaldoGeral;
    private TextView viewTextDespesaGeral;
    private TextView viewTextReceitaGeral;
    private View buttonNovaDespesa;
    private View buttonNovaReceita;
    private View buttonFavoritos;
    private TextView btnExtrato;
    private BottomNavigationView menuBaixo;
    private BigDecimal despesa;
    private BigDecimal receita;
    private BigDecimal saldo;


    public void atualizarValoresPrincipais() {

        API.get().getSaldo(token, CALENDARIO.get(Calendar.YEAR), CALENDARIO.get(Calendar.MONTH) + 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JsonObject valores = JsonParser.parseString(response.body()).getAsJsonObject();

                saldo = valores.get("saldo").getAsBigDecimal();

                receita = valores.get("receita").getAsBigDecimal();

                despesa = valores.get("despesa").getAsBigDecimal();

                viewTextReceitaGeral.setText(FORMATADOR_MOEDA.format(receita));
                viewTextDespesaGeral.setText(FORMATADOR_MOEDA.format(despesa));
                viewTextSaldoGeral.setText(FORMATADOR_MOEDA.format(saldo));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        getSupportActionBar().setCustomView(R.layout.toolbar_circle);


        getSupportActionBar().hide();
        viewTextSaldoGeral = this.findViewById(R.id.dashboard_saldo_principal_texto);
        viewTextReceitaGeral = this.findViewById(R.id.dashboard_receita_principal_texto);
        viewTextDespesaGeral = this.findViewById(R.id.dashboard_despesa_principal_texto);

        menuBaixo = this.findViewById(R.id.dashboard_menu_baixo);
        menuBaixo.setSelectedItemId(R.id.menu_baixo_geral);

        menuBaixo.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_baixo_geral:
                    break;
                case R.id.menu_baixo_extratos: {
                    Intent intent = new Intent(DashboardActivity.this, ExtratoUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_perfil: {
                    Intent intent = new Intent(DashboardActivity.this, PerfilUsuario.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
            }

            return false;
        });

        buttonNovaDespesa = this.findViewById(R.id.dashboard_nova_despesa);
        buttonNovaReceita = this.findViewById(R.id.dashboard_nova_receita);
        buttonFavoritos = this.findViewById(R.id.dashboard_favoritos);
        btnExtrato = this.findViewById(R.id.btnExtrato);
        //pega o token da tela de login
        if (token == null) {
            token = this.getIntent().getStringExtra("token");
        }


        Type tipoArrayCategorias = new TypeToken<List<Categoria>>() {
        }.getType();

        API.get().getDashboardInfo(token, CALENDARIO.get(Calendar.YEAR), CALENDARIO.get(Calendar.MONTH) + 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JsonArray json = JsonParser.parseString(response.body()).getAsJsonArray();
                JsonObject valores = json.get(0).getAsJsonObject();
                JsonObject categorias = json.get(1).getAsJsonObject();

                despesas.addAll(GSON.fromJson(categorias.get("despesas"), tipoArrayCategorias));
                receitas.addAll(GSON.fromJson(categorias.get("receitas"), tipoArrayCategorias));


                saldo = valores.get("saldo").getAsBigDecimal();
                ShimmerFrameLayout viewEfeitoSaldoGeral = findViewById(R.id.dashboard_saldo_principal_efeito);
                viewEfeitoSaldoGeral.stopShimmer();
                viewEfeitoSaldoGeral.setVisibility(View.GONE);

                receita = valores.get("receita").getAsBigDecimal();
                ShimmerFrameLayout viewEfeitoReceitaGeral = findViewById(R.id.dashboard_receita_principa_efeito);
                viewEfeitoReceitaGeral.stopShimmer();
                viewEfeitoReceitaGeral.setVisibility(View.GONE);

                despesa = valores.get("despesa").getAsBigDecimal();
                ShimmerFrameLayout viewEfeitoDespesaGeral = findViewById(R.id.dashboard_despesa_principal_efeito);
                viewEfeitoDespesaGeral.stopShimmer();
                viewEfeitoDespesaGeral.setVisibility(View.GONE);

                DashboardActivity.this.atualizarValoresPrincipais();

                Log.d("teste", viewTextSaldoGeral.getParent().getClass().getName());

                Toast.makeText(DashboardActivity.this, "carregou", Toast.LENGTH_SHORT).show();

                OkHttpClient client = new OkHttpClient();

                webSocket = client.newWebSocket(
                        new Request.Builder().url(API.WS_API_URL + "dashboard/" + token).build(),
                        new DashboardWebSocket()
                );

                buttonNovaDespesa.setOnClickListener(view -> {
                    Intent intent = new Intent(DashboardActivity.this, NovaTransferenciaActivity.class);

                    intent.putExtra("modo", "despesa");


                    intent.putParcelableArrayListExtra("categorias", new ArrayList<>(despesas));


                    startActivity(intent);

                });

                buttonNovaReceita.setOnClickListener(view -> {
                    Intent intent = new Intent(DashboardActivity.this, NovaTransferenciaActivity.class);

                    intent.putExtra("modo", "receita");
                    intent.putParcelableArrayListExtra("categorias", new ArrayList<>(receitas));

                    startActivity(intent);

                });

                buttonFavoritos.setOnClickListener(view ->
                    startActivity(new Intent(DashboardActivity.this, TransferenciaFavoritasActivity.class))
                );

                client.dispatcher().executorService().shutdown();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                throw new RuntimeException(t);
            }


        });


        PieChart chart = findViewById(R.id.dashboard_grafico_mes);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDrawHoleEnabled(false);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setDrawCenterText(false);
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setEntryLabelColor(0xfff5f5f5); //whitesmoke
        chart.setEntryLabelTextSize(12f);

        PieDataSet dataSet = new PieDataSet(new ArrayList<>(), null);
        dataSet.setDrawIcons(false);

        API.get().graficoMensal("despesa", token, CALENDARIO.get(Calendar.YEAR), CALENDARIO.get(Calendar.MONTH) + 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                ArrayList<Integer> colors = new ArrayList<>();
                JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

                PieData data = new PieData(dataSet);

                data.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return FORMATADOR_MOEDA.format(value);
                    }
                });

                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);

                object.entrySet().forEach(entry -> {
                    int idCategoria = Integer.parseInt(entry.getKey());
                    Categoria categoria = despesas.stream().filter(cat -> cat.idCategoria == idCategoria).findAny().get();

                    colors.add(Integer.parseInt(categoria.cor, 16) | 0xFF000000); // pra tirar o transparente

                    dataSet.addEntry(new PieEntry(
                            entry.getValue().getAsFloat(),
                            categoria.nome
                    ));
                });

                dataSet.setColors(colors);
                chart.setData(data);
                chart.invalidate();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                throw new RuntimeException(t);
            }
        });

    }

    private class DashboardWebSocket extends WebSocketListener {
        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
            Log.d("teste", "abriu");
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {

            runOnUiThread(() -> {
                JsonObject json = JsonParser.parseString(text).getAsJsonObject();

                Log.d("teste", json.toString());

                switch (json.get("metodo").getAsString()) {
                    case "despesa": {
                        switch (json.get("arg").getAsString()) {
                            case "remover": {
                                DashboardActivity.this.atualizarValoresPrincipais();
                                break;
                            }
                        }
                        break;
                    }
                    case "receita": {
                        switch (json.get("arg").getAsString()) {
                            case "adicionar": {
                                DashboardActivity.this.atualizarValoresPrincipais();
                                break;
                            }
                        }
                        break;
                    }
                }


                Log.d("teste", "despesa=" + despesa + "receita=" + receita);
            });


        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable okhttp3.Response response) {
            Log.e("teste", t.getClass().getName(), t);
        }
    }


}