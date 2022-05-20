package com.example.royalapp;

import static com.example.royalapp.DashboardActivity.FORMATADOR_MOEDA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.royalapp.model.Categoria;
import com.example.royalapp.model.TransferenciaExtrato;
import com.example.royalapp.remote.API;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtratoUsuarioActivity extends AppCompatActivity {
    List<TransferenciaExtrato> list = new ArrayList<>();
    RecyclerView recyclerView;

    private Spinner menu;
    private Spinner menu2;

    String[] opcoes = {"janeiro", "fevereiro", "marco", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};
    String[] ano = {"2022"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato_usuario);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);

        Call<List<TransferenciaExtrato>> call = API.get().getExtratos( getIntent().getStringExtra("k"), 2022, 5);

        call.enqueue(new Callback<List<TransferenciaExtrato>>() {
            @Override
            public void onResponse(Call<List<TransferenciaExtrato>> call, Response<List<TransferenciaExtrato>> response) {
                list = response.body();


                recyclerView.setAdapter(new ExtratoAdapter(response.body()));


            }

            @Override
            public void onFailure(Call<List<TransferenciaExtrato>> call, Throwable t) {

                Log.d("API-ERRO",t.getMessage());

            }
        });


        menu = findViewById(R.id.spiner_meses);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, opcoes);
        menu.setAdapter(adapter);

        menu2 = findViewById(R.id.spiner_ano);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ano);
        menu2.setAdapter(adapter2);


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
            txtDescricao.setText(transferenciaExtrato.getDescricao());
            txtPreco.setText(FORMATADOR_MOEDA.format(transferenciaExtrato.getValor()));
            txtData.setText(transferenciaExtrato.getData());

            Categoria categoria = null;

            if (transferenciaExtrato.getValor().compareTo(BigDecimal.ZERO) > 0) { //receita
                txtPreco.setTextColor(ExtratoUsuarioActivity.this.getResources().getColor(R.color.verdePositivo));



                for(Categoria cat : DashboardActivity.receitas){
                    if(cat.idCategoria == transferenciaExtrato.getCategoria()){
                        categoria = cat;
                    }
                }


            } else { //despesa
                for(Categoria cat : DashboardActivity.despesas){
                    if(cat.idCategoria == transferenciaExtrato.getCategoria()){
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


