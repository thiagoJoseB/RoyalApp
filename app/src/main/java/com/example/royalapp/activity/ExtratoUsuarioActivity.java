package com.example.royalapp.activity;

import static com.example.royalapp.Constantes.anoAlvo;
import static com.example.royalapp.Constantes.mesAlvoInicio0;
import static com.example.royalapp.Utilidades.CALENDARIO;
import static com.example.royalapp.Utilidades.FORMATADOR_MOEDA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.royalapp.Constantes;
import com.example.royalapp.R;
import com.example.royalapp.Utilidades;
import com.example.royalapp.model.Categoria;
import com.example.royalapp.model.TransferenciaExtrato;
import com.example.royalapp.remote.API;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtratoUsuarioActivity extends AppCompatActivity {
    List<TransferenciaExtrato> list = new ArrayList<>();
    RecyclerView recyclerView;

    private TextView seletorPeriodo;
    private BottomNavigationView menuBaixo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato_usuario);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        menuBaixo = this.findViewById(R.id.extratos_menu_baixo);
        menuBaixo.setSelectedItemId(R.id.menu_baixo_extratos);

        menuBaixo.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.menu_baixo_extratos:
                    break;
                case R.id.menu_baixo_geral: {
                    Intent intent = new Intent(ExtratoUsuarioActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case R.id.menu_baixo_perfil: {
                    Intent intent = new Intent(ExtratoUsuarioActivity.this, PerfilUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
            }

            return false;
        });

        recyclerView = findViewById(R.id.recyclerview);




        seletorPeriodo = findViewById(R.id.extratos_seletor_periodo);

        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(mesAlvoInicio0, anoAlvo, "Data alvo", Utilidades.BRASIL);



        seletorPeriodo.setOnClickListener(view -> {
            assert dialogFragment.getArguments() != null;
            dialogFragment.getArguments().putInt("month", mesAlvoInicio0); //gambi
            dialogFragment.getArguments().putInt("year", anoAlvo); //gambi


            dialogFragment.show(getSupportFragmentManager(), null);
        });

        dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
            mesAlvoInicio0 = monthOfYear;
            anoAlvo = year;

            this.atualizarMesAnoAlvo();
        });



        atualizarMesAnoAlvo();

    }

    private void atualizarMesAnoAlvo() {
        seletorPeriodo.setText(
                String.format(this.getString(R.string.mes_ano_seletor), Constantes.MESES[mesAlvoInicio0], String.valueOf(anoAlvo))
        );

        Call<List<TransferenciaExtrato>> call = API.get().getExtratos(DashboardActivity.token, anoAlvo, mesAlvoInicio0 + 1);

        call.enqueue(new Callback<List<TransferenciaExtrato>>() {
            @Override
            public void onResponse(Call<List<TransferenciaExtrato>> call, Response<List<TransferenciaExtrato>> response) {
                list = response.body();

                recyclerView.setAdapter(new ExtratoAdapter(response.body()));
            }

            @Override
            public void onFailure(Call<List<TransferenciaExtrato>> call, Throwable t) {
            }
        });
    }

    private class ExtratoAdapter extends RecyclerView.Adapter<ExtratoAdapter.ExtratoViewHolder> {

        List<TransferenciaExtrato> itemExtratos;

        public ExtratoAdapter(List<TransferenciaExtrato> itemExtratos) {
            this.itemExtratos = itemExtratos;


        }
        ////15
        @NonNull
        @Override
        public ExtratoAdapter.ExtratoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                return new ExtratoAdapter.ExtratoViewHolder(
                        LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_container_extrato, parent, false)
                );


        }

        @Override
        public void onBindViewHolder(@NonNull ExtratoAdapter.ExtratoViewHolder holder, int position) {

                TransferenciaExtrato transferenciaExtrato =  itemExtratos.get(position);
                holder.setExtratoData(transferenciaExtrato);


        }


        @Override
        public int getItemCount(){
            return itemExtratos.size();
        }





    /**
     * 10 passo aula de recyclerView
     *///
    class ExtratoViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtCategoria, txtDescricao,  txtImagem , txtData , txtPreco;



        public ExtratoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoria = itemView.findViewById(R.id.txtCardCategoria);
            txtDescricao = itemView.findViewById(R.id.txtCardDescricao);
            txtPreco = itemView.findViewById(R.id.txtCardPreco);
            txtImagem = itemView.findViewById(R.id.txtCardImagem);
            txtData = itemView.findViewById(R.id.txtCardData);
        }

        ///13
        public void setExtratoData(TransferenciaExtrato transferenciaExtrato){
            txtDescricao.setText(transferenciaExtrato.descricao);
            txtPreco.setText(FORMATADOR_MOEDA.format(transferenciaExtrato.valor));
            txtData.setText(transferenciaExtrato.data +
                    (transferenciaExtrato.indice != null ? "\nParcela " + (transferenciaExtrato.indice + 1) + "/" + transferenciaExtrato.parcelas: ""));

            Categoria categoria = null;

            if (transferenciaExtrato.valor.compareTo(BigDecimal.ZERO) > 0) { //receita
                txtPreco.setTextColor(ExtratoUsuarioActivity.this.getResources().getColor(R.color.verdePositivo));



                for(Categoria cat : Categoria.RECEITAS){
                    if(cat.idCategoria == transferenciaExtrato.categoria){
                        categoria = cat;
                    }
                }


            } else { //despesa
                for(Categoria cat : Categoria.DESPESAS){
                    if(cat.idCategoria == transferenciaExtrato.categoria){
                        categoria = cat;
                    }
                }
            }

            assert categoria != null;
            txtCategoria.setText(categoria.nome);
            txtImagem.setText(categoria.icone);
        }
    }
    }

    }


