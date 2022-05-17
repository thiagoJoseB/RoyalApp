package com.example.royalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.royalapp.model.Extrato;
import com.example.royalapp.model.ItemExtrato;
import com.example.royalapp.remote.APIUtil;
import com.example.royalapp.remote.RouterInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ExtratoUsuario extends AppCompatActivity {

    ///19
    RouterInterface routerInterface;
    List<Extrato> list = new ArrayList<>();
    RecyclerView recyclerView;

    private Spinner menu;
    private Spinner menu2;

    String[] opcoes = {"janeiro", "fevereiro", "marco", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};
    String[] ano = {"2022"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato_usuario);

        ///20
        routerInterface = APIUtil.getApiInterface();
        recyclerView = findViewById(R.id.recyclerview);

        ///21
        ///***RECEBE OS DADOS DE EXTRATO **///
        Call<List<Extrato>> call = routerInterface.getExtratos( getIntent().getStringExtra("k"), 2022, 5);
        Intent intent = new Intent(ExtratoUsuario.this, RouterInterface.class);






        call.enqueue(new Callback<List<Extrato>>() {
            @Override
            public void onResponse(Call<List<Extrato>> call, Response<List<Extrato>> response) {

                List<ItemExtrato> itemExtratos = new ArrayList<>();
                list = response.body();



                ///22
                for (int i = 0; i< list.size(); i++){


                    itemExtratos.add(new ItemExtrato(0 ,list.get(i)));
                }

                recyclerView.setAdapter(new ExtratoAdapter(itemExtratos));


            }

            @Override
            public void onFailure(Call<List<Extrato>> call, Throwable t) {

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


    /**
     * 09 passo aula de recyclerView
     *///
    private class ExtratoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ItemExtrato> itemExtratos;

        public ExtratoAdapter(List<ItemExtrato> itemExtratos) {
            this.itemExtratos = itemExtratos;


        }
        ////15
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ///18
            ///*INFLA A ESTRUTURA XML E OS DADOS REFERENTES A EXTRATO*/////
            if(viewType ==0){
                return new ExtratoAdapter.ExtratoViewHolder(
                        LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_container_extrato, parent, false)
                );
            }

            return null;

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ///17 passo aula de recyclerView
            ////**RECUPERA OS DADOS DE EXTRATO**/////
            if (getItemViewType(position) ==0){

                Extrato extrato = (Extrato) itemExtratos.get(position).getObject();
                ((ExtratoAdapter.ExtratoViewHolder) holder).setExtratoData(extrato);
            }

        }


        @Override
        public int getItemCount(){
            return itemExtratos.size();
        }

        ///16 ///***RECUPERA O TIPO DE OBJETO DE ITEM***////
        public int getItemViewCount(int position ){

               return itemExtratos.get(position).getType();
            }



    /**
     * 10 passo aula de recyclerView
     *///
    class ExtratoViewHolder extends RecyclerView.ViewHolder {
        /// 11 recycler
        private TextView txtCategoria, txtDescricao,  txtImagem , txtData , txtPreco;



        public ExtratoViewHolder(@NonNull View itemView) {

            super(itemView);

            ////12
            txtCategoria = itemView.findViewById(R.id.txtCardCategoria);
            txtDescricao = itemView.findViewById(R.id.txtCardDescricao);
            txtPreco = itemView.findViewById(R.id.txtCardPreco);
            txtImagem = itemView.findViewById(R.id.txtCardImagem);
            txtData = itemView.findViewById(R.id.txtCardData);
        }

        ///13
        public void setExtratoData(Extrato extrato){

            txtCategoria.setText(String.valueOf(extrato.getCategoria()));
            txtDescricao.setText(extrato.getDescricao());
//            txtPreco.setText(extrato.getValor());
            txtData.setText(extrato.getData());


            txtCategoria.setText(String.valueOf(extrato.getCategoria()));
        }
    }
    }

    }


