package com.example.royalapp.activity;

import static com.example.royalapp.Utilidades.BRASIL;
import static com.example.royalapp.Utilidades.CALENDARIO;
import static com.example.royalapp.Utilidades.FORMATADOR_MOEDA;
import static com.example.royalapp.Utilidades.GSON;
import static com.example.royalapp.activity.TransferenciaFavoritasActivity.atualizarDensidade;
import static com.example.royalapp.remote.API.OK_HTTP_CLIENT;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.royalapp.Constantes;
import com.example.royalapp.Extra;
import com.example.royalapp.R;
import com.example.royalapp.Utilidades;
import com.example.royalapp.Variaveis;
import com.example.royalapp.model.Categoria;
import com.example.royalapp.model.Transferencia;
import com.example.royalapp.remote.API;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    private static final int CODIGO_TRANSFERENCIA = 17;



    public static volatile boolean websocketFuncionante = false;
    public static WebSocket webSocket;

    static String token = null;



    private TextView viewTextSaldoGeral;
    private TextView viewTextDespesaGeral;
    private TextView viewTextReceitaGeral;
    private TextView viewSeletorMesAno;

    private TextView viewProjetadoDespesa;
    private TextView viewProjetadoReceita;

    private View buttonNovaDespesa;
    private View buttonNovaReceita;
    private View buttonFavoritos;
    private BottomNavigationView menuBaixo;
    private RadioGroup radioGroupDespesaOuReceita;

    private ShimmerFrameLayout viewEfeitoGeral;
    private ShimmerFrameLayout viewEfeitoReceita;
    private ShimmerFrameLayout viewEfeitoDespesa;


    private RecyclerView recyclerViewListaFixa;

    private TextView viewGraficoTextoVazio;

    private ShimmerFrameLayout viewEfeitoGrafico;
    private PieChart chart;
    private final PieDataSet dataSet = new PieDataSet(new ArrayList<>(), null);

    private BigDecimal despesaMensal;
    private BigDecimal receitaMensal;

    private BigDecimal despesaMensalBruto;
    private BigDecimal receitaMensalBruto;

    private BigDecimal saldoGeral;
    private String tipoGrafico = "despesa";

    public void atualizarMesAnoAlvo(){
        viewSeletorMesAno.setText(
                String.format(this.getString(R.string.mes_ano_seletor), Constantes.MESES[Variaveis.mesAlvoInicio0], String.valueOf(Variaveis.anoAlvo))
        );

        atualizarValoresPrincipais();
    }

    private void iniciarEsperaTexto(){
        viewTextSaldoGeral.setVisibility(View.INVISIBLE);
        viewTextDespesaGeral.setVisibility(View.INVISIBLE);
        viewTextReceitaGeral.setVisibility(View.INVISIBLE);

        viewEfeitoGeral.setVisibility(View.VISIBLE);
        viewEfeitoReceita.setVisibility(View.VISIBLE);
        viewEfeitoDespesa.setVisibility(View.VISIBLE);

        viewEfeitoGeral.startShimmer();
        viewEfeitoReceita.startShimmer();
        viewEfeitoDespesa.startShimmer();
    }

    private void finalizarEsperaTextoBotandoValor(){
        viewTextReceitaGeral.setText(FORMATADOR_MOEDA.format(receitaMensal));
        viewTextDespesaGeral.setText(FORMATADOR_MOEDA.format(despesaMensal));

        viewProjetadoReceita.setText(
                String.format(
                        getString(R.string.projecao_receita),
                        FORMATADOR_MOEDA.format(receitaMensal),
                        FORMATADOR_MOEDA.format(receitaMensalBruto)
                )
        );
        viewProjetadoDespesa.setText(String.format(
                getString(R.string.projecao_despesa),
                FORMATADOR_MOEDA.format(despesaMensal),
                FORMATADOR_MOEDA.format(despesaMensalBruto)
        ));

        viewTextSaldoGeral.setText(FORMATADOR_MOEDA.format(saldoGeral));

        viewTextSaldoGeral.setVisibility(View.VISIBLE);
        viewTextDespesaGeral.setVisibility(View.VISIBLE);
        viewTextReceitaGeral.setVisibility(View.VISIBLE);

        viewEfeitoGeral.stopShimmer();
        viewEfeitoReceita.stopShimmer();
        viewEfeitoDespesa.stopShimmer();

        viewEfeitoReceita.setVisibility(View.GONE);
        viewEfeitoGeral.setVisibility(View.GONE);
        viewEfeitoDespesa.setVisibility(View.GONE);
    }

    private void iniciarEsperaGrafico(){
        viewEfeitoGrafico.setVisibility(View.VISIBLE);
        viewGraficoTextoVazio.setVisibility(View.GONE);

        chart.setVisibility(View.INVISIBLE);

        viewEfeitoGrafico.startShimmer();
    }

    private void finalizarEsperaGrafico(boolean temResultado){
        if(temResultado){

            chart.setVisibility(View.VISIBLE);
        } else {
            viewGraficoTextoVazio.setVisibility(View.VISIBLE);
        }

        viewEfeitoGrafico.stopShimmer();

        viewEfeitoGrafico.setVisibility(View.GONE);
    }


    public void atualizarValoresPrincipais() {
        runOnUiThread(() -> {
            iniciarEsperaTexto();

            API.get().getSaldo(token, Variaveis.anoAlvo, Variaveis.mesAlvoInicio0 + 1).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
//                    Log.d("teste", response.body());
                    JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                    JsonObject valores = array.get(0).getAsJsonObject();
                    JsonObject bruto = array.get(2).getAsJsonObject();

                    saldoGeral = array.get(1).getAsBigDecimal();

                    receitaMensal = valores.get("receita").getAsBigDecimal();
                    despesaMensal = valores.get("despesa").getAsBigDecimal();

                    receitaMensalBruto = bruto.get("receita").getAsBigDecimal();
                    despesaMensalBruto = bruto.get("despesa").getAsBigDecimal();

                    finalizarEsperaTextoBotandoValor();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    throw new RuntimeException(t);
                }
            });

            atualizarGrafico();
        });



    }

    public void atualizarGrafico(){
        iniciarEsperaGrafico();

        API.get().graficoMensal(tipoGrafico, token, Variaveis.anoAlvo, Variaveis.mesAlvoInicio0 + 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("teste", String.valueOf(response.code()));

                ArrayList<Integer> colors = new ArrayList<>();
                JsonObject object = JsonParser.parseString(response.body()).getAsJsonObject();

                PieData data = new PieData(dataSet);
                data.getDataSet().clear();

                data.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return FORMATADOR_MOEDA.format(new BigDecimal(Float.toString(value)));
                    }
                });

                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);

                if(!object.entrySet().isEmpty()) {

                    object.entrySet().forEach(entry -> {
                        int idCategoria = Integer.parseInt(entry.getKey());
                        Categoria categoria = (tipoGrafico.equals("despesa") ? Categoria.DESPESAS : Categoria.RECEITAS).stream().filter(cat -> cat.idCategoria == idCategoria).findAny().get();

                        colors.add(Integer.parseInt(categoria.cor, 16) | 0xFF000000); // pra tirar o transparente

                        dataSet.addEntry(new PieEntry(
                                entry.getValue().getAsFloat(),
                                categoria.nome
                        ));
                    });

                    finalizarEsperaGrafico(true);

                } else {

                    viewGraficoTextoVazio.setText(
                            String.format(
                                    getString(R.string.grafico_mes_sem_dados),
                                    Constantes.MESES[Variaveis.mesAlvoInicio0],
                                    String.valueOf(Variaveis.anoAlvo)
                            )
                    );

                    finalizarEsperaGrafico(false);
                }

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

    private void instanciadores(){
        viewTextSaldoGeral = this.findViewById(R.id.dashboard_saldo_principal_texto);
        viewTextReceitaGeral = this.findViewById(R.id.dashboard_receita_principal_texto);
        viewTextDespesaGeral = this.findViewById(R.id.dashboard_despesa_principal_texto);

        viewSeletorMesAno = this.findViewById(R.id.dashboard_seletor_mes_ano);
        menuBaixo = this.findViewById(R.id.dashboard_menu_baixo);

        buttonNovaDespesa = this.findViewById(R.id.dashboard_nova_despesa);
        buttonNovaReceita = this.findViewById(R.id.dashboard_nova_receita);
        buttonFavoritos = this.findViewById(R.id.dashboard_favoritos);
        radioGroupDespesaOuReceita = this.findViewById(R.id.dashboard_radio_group_tipo);

        viewEfeitoGeral = findViewById(R.id.dashboard_saldo_principal_efeito);
        viewEfeitoReceita = findViewById(R.id.dashboard_receita_principa_efeito);
        viewEfeitoDespesa = findViewById(R.id.dashboard_despesa_principal_efeito);
        viewEfeitoGrafico = findViewById(R.id.dashboard_grafico_mes_efeito);

        recyclerViewListaFixa = findViewById(R.id.dashboard_lista_fixas);

        viewProjetadoDespesa = findViewById(R.id.dashboard_projecao_despesa);
        viewProjetadoReceita = findViewById(R.id.dashboard_projecao_receita);

        viewGraficoTextoVazio = findViewById(R.id.dashboard_grafico_mes_vazio);

        chart = findViewById(R.id.dashboard_grafico_mes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        getSupportActionBar().setCustomView(R.layout.toolbar_circle);

        atualizarDensidade(this); //famosa gambi


        getSupportActionBar().hide();

        instanciadores();

        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(Variaveis.mesAlvoInicio0, Variaveis.anoAlvo, "Data alvo", Utilidades.BRASIL);



        viewSeletorMesAno.setOnClickListener(view -> {
            assert dialogFragment.getArguments() != null;
            dialogFragment.getArguments().putInt("month", Variaveis.mesAlvoInicio0); //gambi
            dialogFragment.getArguments().putInt("year", Variaveis.anoAlvo); //gambi


            dialogFragment.show(getSupportFragmentManager(), null);
        });

        dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
            Variaveis.mesAlvoInicio0 = monthOfYear;
            Variaveis.anoAlvo = year;

            this.atualizarMesAnoAlvo();
        });

        viewSeletorMesAno.setText(
                String.format(this.getString(R.string.mes_ano_seletor), Constantes.MESES[Variaveis.mesAlvoInicio0], String.valueOf(Variaveis.anoAlvo))
        );


        menuBaixo.setSelectedItemId(R.id.menu_baixo_geral);

        menuBaixo.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_baixo_geral:
                    break;
                case R.id.menu_baixo_graficos: {
                    Intent intent = new Intent(this, GraficosActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_extratos: {
                    Intent intent = new Intent(DashboardActivity.this, ExtratoUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_perfil: {
                    Intent intent = new Intent(DashboardActivity.this, PerfilUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
            }

            return false;
        });



        radioGroupDespesaOuReceita.setOnCheckedChangeListener( (group, checkedId) -> {
            switch (checkedId){
                case R.id.dashboard_radio_group_tipo_despesa:
                    tipoGrafico = "despesa";
                    break;
                case R.id.dashboard_radio_group_tipo_receita:
                    tipoGrafico = "receita";
                    break;
            }

            atualizarGrafico();
        });

        buttonNovaDespesa.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, NovaTransferenciaActivity.class);

            intent.putExtra("modo", "despesa");

            startActivityForResult(intent, CODIGO_TRANSFERENCIA);
        });

        buttonNovaReceita.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, NovaTransferenciaActivity.class);

            intent.putExtra("modo", "receita");

            startActivityForResult(intent, CODIGO_TRANSFERENCIA);
        });

        buttonFavoritos.setOnClickListener(view ->
                startActivity(new Intent(DashboardActivity.this, TransferenciaFavoritasActivity.class))
        );

        Type tipoArrayCategorias = new TypeToken<List<Categoria>>() {}.getType();
        Type tipoArrayTransferencia = new TypeToken<List<Transferencia>>() {}.getType();

        API.get().getDashboardInfo(token, CALENDARIO.get(Calendar.YEAR), CALENDARIO.get(Calendar.MONTH) + 1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                JsonArray json = JsonParser.parseString(response.body()).getAsJsonArray();
                JsonObject valores = json.get(0).getAsJsonObject();
                JsonObject categorias = json.get(1).getAsJsonObject();
                JsonObject bruto = json.get(3).getAsJsonObject();



                Categoria.DESPESAS = (GSON.fromJson(categorias.get("despesas"), tipoArrayCategorias));
                Categoria.RECEITAS = (GSON.fromJson(categorias.get("receitas"), tipoArrayCategorias));




                saldoGeral = json.get(2).getAsBigDecimal();
                receitaMensal = valores.get("receita").getAsBigDecimal();
                despesaMensal = valores.get("despesa").getAsBigDecimal();
                receitaMensalBruto = bruto.get("receita").getAsBigDecimal();
                despesaMensalBruto = bruto.get("despesa").getAsBigDecimal();

                finalizarEsperaTextoBotandoValor();

                DashboardActivity.this.atualizarGrafico();


                novoWebsocket();

                runOnUiThread(() -> {
                    recyclerViewListaFixa.setAdapter(new FixaAdapter(GSON.fromJson(json.get(4), tipoArrayTransferencia)));
                });


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                throw new RuntimeException(t);
            }


        });



        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);


        chart.setDrawHoleEnabled(false);


        chart.setDrawCenterText(false);
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setEntryLabelColor(0xfff5f5f5); //whitesmoke
        chart.setEntryLabelTextSize(12f);

        chart.setNoDataText("Carregando as informa????es...");
        chart.setNoDataTextColor(0xff000000);

//        chart.setRenderer(new CustomPieChartRenderer(chart, 10f));



        Legend legend = chart.getLegend();

        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        dataSet.setDrawIcons(false);


    }

    private void novoWebsocket(){
        if(webSocket != null && websocketFuncionante){
            webSocket.close(1000, null);
            return;
        }

        webSocket = OK_HTTP_CLIENT.newWebSocket(
                new Request.Builder().url(API.WS_API_URL + "dashboard/" + token).build(),
                new DashboardWebSocket()
        );

    }

    private class DashboardWebSocket extends WebSocketListener {
        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
            Log.d("teste", "abriu");
            websocketFuncionante = true;
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            websocketFuncionante = false;
            findViewById(R.id.dashboard_websockets_caiu).setVisibility(View.VISIBLE);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                JsonObject json = JsonParser.parseString(text).getAsJsonObject();

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


                Log.d("teste", "despesa=" + despesaMensal + "receita=" + receitaMensal);



        }



        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable okhttp3.Response response) {
            //tenta reconectar, na verdade nao
            DashboardWebSocket.this.onClosed(
                    webSocket,
                    48230947,
                    ""
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODIGO_TRANSFERENCIA){
            if(resultCode == RESULT_OK){
                if(!websocketFuncionante){
                    this.atualizarValoresPrincipais();
                }
            } else if(resultCode == NovaTransferenciaActivity.RESULTADO_TEVE_FIXA){
                Extra.rodar(() -> {
                    List<Transferencia> transferencias = API.get().fixa(DashboardActivity.token).execute().body();

                    runOnUiThread(() -> {
                        assert transferencias != null;
                        recyclerViewListaFixa.setAdapter(
                                new FixaAdapter(transferencias)
                        );
                    });
                });



                if(!websocketFuncionante){
                    this.atualizarValoresPrincipais();
                }
            }
        }
    }



    private class FixaAdapter extends RecyclerView.Adapter<FixaAdapter.FixaViewHolder>{
        List<Transferencia> fixas;

        public FixaAdapter(List<Transferencia> fixas) {
            super();
            Log.d("teste", fixas.toString());
            this.fixas = fixas;
        }

        @NonNull
        @Override
        public FixaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("teste", "log");

            return new FixaViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_container_graficos, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull FixaViewHolder holder, int position) {
            Transferencia transferencia = fixas.get(position);

            Log.d("teste", transferencia.toString());

            holder.textViewIcone.setText(Categoria.para(transferencia.valor).stream().filter(c -> c.idCategoria == transferencia.categoria).findAny().get().icone);
            holder.textViewValor.setText(
                    String.format(BRASIL, "%s x %d = %s",
                            FORMATADOR_MOEDA.format(transferencia.valor),
                            transferencia.parcelas,
                            FORMATADOR_MOEDA.format(transferencia.valor.multiply(new BigDecimal(transferencia.parcelas)))
                    )
            );

            holder.textViewDescricao.setText(transferencia.descricao);
            holder.viewCancelar.setOnClickListener(v -> {
                new AlertDialog.Builder(DashboardActivity.this)
                        .setMessage("Voc?? tem certeza que deseja desfixar?")
                        .setTitle("Desfixar")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            try {

                                Extra.rodar(() -> {
                                    Log.d("teste",
                                            API.get().desfixar(
                                                    "{\"id\": " + transferencia.id + "}",
                                                    DashboardActivity.token
                                            ).execute().body()
                                    );
                                }).get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }

                            fixas.remove(position);

                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, fixas.size());
                        })
                        .setNegativeButton("N??o", (dialog, which) -> {
                            dialog.dismiss();
                        }).create().show();
            });
        }

        @Override
        public int getItemCount() {
            return fixas.size();
        }

        private class FixaViewHolder extends RecyclerView.ViewHolder{
            public TextView textViewIcone;
            public TextView textViewValor;
            public TextView textViewDescricao;
            public View viewCancelar;

            public FixaViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewIcone = itemView.findViewById(R.id.item_fixa_icone);
                textViewValor = itemView.findViewById(R.id.item_fixa_valor);
                textViewDescricao = itemView.findViewById(R.id.item_fixa_descricao);
                viewCancelar = itemView.findViewById(R.id.item_fixa_cancelar);
            }
        }
    }
}