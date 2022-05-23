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
import android.widget.TextView;

import com.example.royalapp.model.Categoria;
import com.example.royalapp.model.ItemFavorito;
import com.example.royalapp.model.TransferenciaExtrato;
import com.example.royalapp.remote.API;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferenciaFavoritas extends AppCompatActivity {
    API api;
    List<ItemFavorito> list = new ArrayList<>();
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes_favoritas);





        recyclerView = findViewById(R.id.recyclerview);

        Call<List<ItemFavorito>> call = API.get().getFavorito(DashboardActivity.token, 2022, 5);

        call.enqueue(new Callback<List<ItemFavorito>>() {
            @Override
            public void onResponse(Call<List<ItemFavorito>> call, Response<List<ItemFavorito>> response) {
               list = response.body();


                recyclerView.setAdapter(new TransferenciaFavoritoAdapter(response.body()));

            }

            @Override
            public void onFailure(Call<List<ItemFavorito>> call, Throwable t) {

                Log.d("API-ERRO",t.getMessage());


            }
        });



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);



    }



    private class TransferenciaFavoritoAdapter extends RecyclerView.Adapter<TransferenciaFavoritoAdapter.TransferenciaFavoritoViewHolder>{

        List<ItemFavorito> itemFav;

        public TransferenciaFavoritoAdapter(List<ItemFavorito> itemFav){
            this.itemFav = itemFav;
        }


        @NonNull
        @Override
        public TransferenciaFavoritoAdapter.TransferenciaFavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType){

            return new TransferenciaFavoritoAdapter.TransferenciaFavoritoViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_container_extrato, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull TransferenciaFavoritoAdapter.TransferenciaFavoritoViewHolder holder, int position) {

            ItemFavorito itemFavorito = itemFav.get(position);
            holder.setTransferenciaFavorita(itemFavorito);




        }

        @Override
        public int getItemCount(){return itemFav.size();}




        class TransferenciaFavoritoViewHolder extends RecyclerView.ViewHolder{
            private final TextView  txtCategoria, txtDescricao, txtImagem, txtData, txtPreco;

            public TransferenciaFavoritoViewHolder (@NonNull View itemView){
                super(itemView);

                txtCategoria = itemView.findViewById(R.id.txtCardCategoria);
                txtDescricao = itemView.findViewById(R.id.txtCardDescricao);
                txtPreco = itemView.findViewById(R.id.txtCardPreco);
                txtImagem = itemView.findViewById(R.id.txtCardImagem);
                txtData = itemView.findViewById(R.id.txtCardData);


            }


            public void setTransferenciaFavorita(ItemFavorito  itemFavorito){
                txtDescricao.setText(itemFavorito.getDescricao());
                txtPreco.setText(FORMATADOR_MOEDA.format(itemFavorito.getValor()));
                txtData.setText(itemFavorito.getData());

                Categoria categoria = null;


                if(itemFavorito.getValor().compareTo(BigDecimal.ZERO) > 0){
                    txtPreco.setTextColor(TransferenciaFavoritas.this.getResources().getColor(R.color.verdePositivo));


                    for(Categoria cat : DashboardActivity.despesas){
                        if(cat.idCategoria == itemFavorito.getCategoria()){
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







