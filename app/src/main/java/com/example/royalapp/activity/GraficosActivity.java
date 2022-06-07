package com.example.royalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.royalapp.Constantes;
import com.example.royalapp.R;
import com.example.royalapp.Utilidades;
import com.example.royalapp.Variaveis;
import com.example.royalapp.model.Categoria;
import com.example.royalapp.model.TransferenciaExtrato;
import com.example.royalapp.remote.API;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraficosActivity extends AppCompatActivity {
    static enum Tipo{
        BARRA,
        LINHA,
        LISTA,
        PIZZA
    }

    private Tipo modoGrafico;

    private BottomNavigationView menuBaixo;
    private LinearLayout botaoBarra;
    private LinearLayout botaoLista;
    private LinearLayout botaoLinha;
    private LinearLayout botaoPizza;
    private TextView seletorCategoria;

    private LinearLayout seletorMes;
    private LinearLayout seletorAno;
    private TextView seletorMesTexto;
    private EditText seletorAnoTexto;

    private View botaoEnviar;

    private CheckBox tipoTransferenciaCheckDespesa;
    private CheckBox tipoTransferenciaCheckReceita;


    private RadioGroup seletorPeriodo;

    private List<LinearLayout> botoesSelecionadores;

    private Drawable iconeBotaoPadrao;

    private final List<Categoria> categoriasAtivas = new ArrayList<>();

    public void instanciadores(){
        iconeBotaoPadrao = getResources().getDrawable(R.drawable.botao_dashboard);
//        iconeBotaoPadrao.mutate();

        menuBaixo = this.findViewById(R.id.grafico_menu_baixo);

        botaoBarra = this.findViewById(R.id.grafico_botao_barra);
        botaoLista = this.findViewById(R.id.grafico_botao_lista);
        botaoLinha = this.findViewById(R.id.grafico_botao_linha);
        botaoPizza = this.findViewById(R.id.grafico_botao_pizza);

        botoesSelecionadores = Arrays.asList(botaoBarra, botaoLista, botaoLinha, botaoPizza);

        seletorMes = this.findViewById(R.id.grafico_seletor_mes);
        seletorAno = this.findViewById(R.id.grafico_seletor_ano);
        seletorAnoTexto = this.findViewById(R.id.grafico_seletor_ano_text);
        seletorMesTexto = this.findViewById(R.id.grafico_seletor_mes_text);

        seletorPeriodo = this.findViewById(R.id.grafico_seletor_periodo);

        botaoEnviar = this.findViewById(R.id.grafico_botao_enviar);


        tipoTransferenciaCheckDespesa = this.findViewById(R.id.grafico_transferencia_checkbox_despesa);
        tipoTransferenciaCheckReceita = this.findViewById(R.id.grafico_transferencia_checkbox_receita);

        seletorCategoria = findViewById(R.id.grafico_seletor_categoria);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        instanciadores();


        seletorCategoria.setOnClickListener(new View.OnClickListener() {
            View singletonView;

            @Override
            public void onClick(View v) {
                if(singletonView == null){
                    singletonView = getLayoutInflater().inflate(R.layout.dialog_selecionador_categoria, null, false);
                    LinearLayout receitasLayout = singletonView.findViewById(R.id.dialog_selecionador_categoria_list_receitas);
                    LinearLayout despesaLayout = singletonView.findViewById(R.id.dialog_selecionador_categoria_list_despesas);

                    for (Categoria receita : Categoria.RECEITAS) {
                        receitasLayout.addView(GraficosActivity.this.itemOptionCriador(receita));
                    }

                    for (Categoria despesa : Categoria.DESPESAS) {
                        despesaLayout.addView(GraficosActivity.this.itemOptionCriador(despesa));
                    }
                }else {
                    //evita erros
                    ((ViewGroup)singletonView.getParent()).removeView(singletonView);

                }

                new AlertDialog.Builder(GraficosActivity.this)

                        .setView(singletonView)
                        .create().show();
            }
        });

        menuBaixo.setSelectedItemId(R.id.menu_baixo_graficos);

        menuBaixo.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_baixo_geral:{
                    Intent intent = new Intent(this, DashboardActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_graficos:
                    break;
                case R.id.menu_baixo_extratos: {
                    Intent intent = new Intent(this, ExtratoUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_perfil: {
                    Intent intent = new Intent(this, PerfilUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
            }

            return false;
        });

        seletorPeriodo.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    switch (checkedId){
                        case R.id.grafico_seletor_periodo_radio_mensal:
                            seletorMes.setVisibility(View.VISIBLE);
                            seletorAno.setVisibility(View.GONE);
                            break;
                        case R.id.grafico_seletor_periodo_radio_anual:
                            seletorMes.setVisibility(View.GONE);
                            seletorAno.setVisibility(View.VISIBLE);
                            break;
                    }

                }
        );

        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(Variaveis.mesAlvoInicio0, Variaveis.anoAlvo, "Data alvo", Utilidades.BRASIL);

        seletorMes.setOnClickListener(view -> {
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

        atualizarMesAnoAlvo();

        seletorAnoTexto.addTextChangedListener(new TextWatcher() {
            int cursor = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursor = seletorAnoTexto.getSelectionEnd();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();

                if(string.startsWith("0")){
                    Log.d("teste", new String(Arrays.copyOfRange(s.toString().toCharArray(), 1, s.length())));
                    seletorAnoTexto.setText(new String(Arrays.copyOfRange(s.toString().toCharArray(), 1, s.length())));
                    return;
                }

                seletorAnoTexto.removeTextChangedListener(this);

                if(string.isEmpty()){
                    seletorAnoTexto.setText("0");
                    Variaveis.anoAlvo = 0;
                    seletorAnoTexto.setSelection(1);
                } else {
                    Variaveis.anoAlvo = Integer.parseInt(s.toString());
                    atualizarMesAnoAlvo();
                    seletorAnoTexto.setSelection(Math.min( Math.max(cursor + (count - before), 0), s.length()));
                }

                seletorAnoTexto.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void enviar(View e){
        if(!tipoTransferenciaCheckReceita.isChecked() && !tipoTransferenciaCheckDespesa.isChecked()){
            new AlertDialog.Builder(this)
                    .setMessage("Escolha se as transferências serão receitas e/ou despesas.")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create().show();
        } else if(modoGrafico == null){
            new AlertDialog.Builder(this)
                    .setMessage("Escolha o tipo do gráfico.")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create().show();
        } else {

            Intent intent = new Intent(this, GraficoResultadoActivity.class);

            if(seletorPeriodo.getCheckedRadioButtonId() == R.id.grafico_seletor_periodo_radio_mensal){
                intent.putExtra("mes", (Serializable) Integer.valueOf(Variaveis.mesAlvoInicio0));
            }

            intent.putExtra("ano", Variaveis.anoAlvo);

            intent.putExtra("modo", modoGrafico);

            intent.putExtra("categorias", categoriasAtivas.stream().mapToInt(c -> c.idCategoria).toArray());

            intent.putExtra("receita", tipoTransferenciaCheckReceita.isChecked());
            intent.putExtra("despesa", tipoTransferenciaCheckDespesa.isChecked());

            startActivity(intent);
        }

    }

    private void atualizarMesAnoAlvo() {
        seletorMesTexto.setText(
                String.format(this.getString(R.string.mes_ano_seletor), Constantes.MESES[Variaveis.mesAlvoInicio0], String.valueOf(Variaveis.anoAlvo))
        );

        seletorAnoTexto.setText(String.valueOf(Variaveis.anoAlvo));
    }

    public void mudarStatus(View e){
        modoGrafico = Tipo.valueOf((String) e.getTag());

        botoesSelecionadores.forEach(botao -> {
            Drawable drawable = iconeBotaoPadrao.getConstantState().newDrawable().mutate();

            if(e == botao){
                drawable.setTint(this.getColor(R.color.corExplosiva));
                botao.setBackground(drawable);
                ((ImageView) botao.getChildAt(1)).setColorFilter(this.getColor(R.color.corPrincipalClara));
                ((TextView) botao.getChildAt(0)).setTextColor(this.getColor(R.color.white));
            } else {
                botao.setBackground(drawable);
                ((ImageView) botao.getChildAt(1)).setColorFilter(this.getColor(R.color.corPrincipalEscura));
                ((TextView) botao.getChildAt(0)).setTextColor(this.getColor(R.color.black));
            }
        });


    }

    private View itemOptionCriador(Categoria categoria){
        View selector = getLayoutInflater().inflate(R.layout.item_option_categoria, null, false);

        TextView iconeText = selector.findViewById(R.id.item_option_categoria_icone);
        TextView nomeText = selector.findViewById(R.id.item_option_categoria_nome);
        CheckBox ativadoCheckbox = selector.findViewById(R.id.item_option_categoria_checkbox);

        iconeText.setText(categoria.icone);
        iconeText.getBackground().setTint(Integer.parseInt(categoria.cor, 16) | 0xFF000000);

        nomeText.setText(categoria.nome);

        // cria so um evento
        ativadoCheckbox.setTag(categoria);
        ativadoCheckbox.setOnClickListener(this::cliqueDaCheckboxCategoria);

        return selector;
    }

    public void cliqueDaCheckboxCategoria(View checkBox1) {
        CheckBox checkBox = (CheckBox) checkBox1;
        Categoria categoria = (Categoria) checkBox.getTag();

        if(checkBox.isChecked()){
            categoriasAtivas.add(categoria);
        } else {
            categoriasAtivas.remove(categoria);
        }

        if(categoriasAtivas.isEmpty()){
            seletorCategoria.setText(this.getString(R.string.categoria_selecionador_vazio));
        } else {
            seletorCategoria.setText(categoriasAtivas.stream().map(c -> c.nome).collect(Collectors.joining(", ")));
        }

    }
}