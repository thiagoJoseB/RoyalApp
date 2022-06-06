package com.example.royalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.royalapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class GraficosActivity extends AppCompatActivity {
    private static enum Tipo{
        BARRA,
        LINHA,
        LISTA,
        PIZZA
    }

    static Tipo modoGrafico;

    private BottomNavigationView menuBaixo;
    private LinearLayout botaoBarra;
    private LinearLayout botaoLista;
    private LinearLayout botaoLinha;
    private LinearLayout botaoPizza;

    private TextView seletorMes;
    private EditText seletorAno;

    private List<LinearLayout> botoesSelecionadores;

    private Drawable iconeBotaoPadrao;

    public void instanciadores(){
        iconeBotaoPadrao = getResources().getDrawable(R.drawable.botao_dashboard);
//        iconeBotaoPadrao.mutate();

        menuBaixo = this.findViewById(R.id.grafico_menu_baixo);

        botaoBarra = this.findViewById(R.id.grafico_botao_barra);
        botaoLista = this.findViewById(R.id.grafico_botao_lista);
        botaoLinha = this.findViewById(R.id.grafico_botao_linha);
        botaoPizza = this.findViewById(R.id.grafico_botao_pizza);

        botoesSelecionadores = Arrays.asList(botaoBarra, botaoLista, botaoLinha, botaoPizza);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        instanciadores();

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
}