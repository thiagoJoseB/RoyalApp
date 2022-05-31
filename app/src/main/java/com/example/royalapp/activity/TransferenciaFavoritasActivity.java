package com.example.royalapp.activity;

import static com.example.royalapp.Utilidades.FORMATADOR_MOEDA;
import static android.widget.LinearLayout.LayoutParams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royalapp.R;
import com.example.royalapp.Utilidades;
import com.example.royalapp.model.Categoria;
import com.example.royalapp.model.ItemFavorito;
import com.example.royalapp.remote.API;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferenciaFavoritasActivity extends AppCompatActivity {
    List<ItemFavorito> list = new ArrayList<>();
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes_favoritas);

        atualizarDensidade(this); //famosa gambi



        recyclerView = findViewById(R.id.favoritas_recycler_view);

        Call<List<ItemFavorito>> call = API.get().getFavorito(DashboardActivity.token, 2022, 5);

        call.enqueue(new Callback<List<ItemFavorito>>() {
            @Override
            public void onResponse(Call<List<ItemFavorito>> call, Response<List<ItemFavorito>> response) {
                list = response.body();


                recyclerView.setAdapter(new TransferenciaFavoritoAdapter(response.body()));

            }

            @Override
            public void onFailure(Call<List<ItemFavorito>> call, Throwable t) {

                Log.d("API-ERRO", t.getMessage());


            }
        });


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


    }


    private class TransferenciaFavoritoAdapter extends RecyclerView.Adapter<TransferenciaFavoritoAdapter.TransferenciaFavoritoViewHolder> {

        List<ItemFavorito> itemFav;

        public TransferenciaFavoritoAdapter(List<ItemFavorito> itemFav) {
            this.itemFav = itemFav;
        }


        @NonNull
        @Override
        public TransferenciaFavoritoAdapter.TransferenciaFavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new TransferenciaFavoritoAdapter.TransferenciaFavoritoViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_container_extrato, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull TransferenciaFavoritoAdapter.TransferenciaFavoritoViewHolder holder, int position) {

            ItemFavorito itemFavorito = itemFav.get(position);
            holder.setTransferenciaFavorita(itemFavorito);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                private Bitmap backup;

                @Override
                public void onClick(View v) {
                    LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_transferencia, null);

                    if (itemFavorito.anexo != null) {
                        ImageView imageView =  layout.findViewById(R.id.dialog_transferencia_imagem);

                        if(backup == null) { // tem o backup?, NAO?, backup poderia ser do linear layout tmb

                            API.get().imagem(itemFavorito.anexo, DashboardActivity.token).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    backup = BitmapFactory
                                            .decodeStream(response.body().byteStream()); // so pega a img uma vez;


                                    imageView.setImageBitmap(backup);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    throw new RuntimeException(t);
                                }
                            });
                        } else { // TEM SIM
                            imageView.setImageBitmap(backup);
                        }
                    }

                    layout.addView(label("Valor", TransferenciaFavoritasActivity.this));
                    layout.addView(texto(FORMATADOR_MOEDA.format(itemFavorito.valor), TransferenciaFavoritasActivity.this));

                    layout.addView(label("Descricao", TransferenciaFavoritasActivity.this));
                    layout.addView(texto(itemFavorito.descricao, TransferenciaFavoritasActivity.this));

                    layout.addView(label("Data", TransferenciaFavoritasActivity.this));
                    layout.addView(texto(Utilidades.FORMATADOR_DIA_LONGO.format(java.sql.Date.valueOf(itemFavorito.data)), TransferenciaFavoritasActivity.this));

                    if (itemFavorito.observacao != null) {
                        layout.addView(label("Observação", TransferenciaFavoritasActivity.this));
                        layout.addView(texto(itemFavorito.observacao, TransferenciaFavoritasActivity.this));
                    }

                    if (itemFavorito.parcelada) {
                        layout.addView(label("Parcelada", TransferenciaFavoritasActivity.this));
                        layout.addView(texto("Sim", TransferenciaFavoritasActivity.this));

                        layout.addView(label("Parcelas", TransferenciaFavoritasActivity.this));
                        layout.addView(texto(String.valueOf(itemFavorito.parcelas), TransferenciaFavoritasActivity.this));

                        layout.addView(label("Frequencia", TransferenciaFavoritasActivity.this));
                        layout.addView(texto(String.valueOf(itemFavorito.nomeFrequencia.toString()), TransferenciaFavoritasActivity.this));
                    } else {
                        layout.addView(label("Parcelada", TransferenciaFavoritasActivity.this));
                        layout.addView(texto("Não", TransferenciaFavoritasActivity.this));
                    }

                    new AlertDialog.Builder(TransferenciaFavoritasActivity.this)
                            .setView(layout)
                            .create().show();
                }


            });
        }

        @Override
        public int getItemCount() {
            return itemFav.size();
        }


        class TransferenciaFavoritoViewHolder extends RecyclerView.ViewHolder {
            private final TextView txtCategoria, txtDescricao, txtImagem, txtData, txtPreco;

            public TransferenciaFavoritoViewHolder(@NonNull View itemView) {
                super(itemView);

                txtCategoria = itemView.findViewById(R.id.txtCardCategoria);
                txtDescricao = itemView.findViewById(R.id.txtCardDescricao);
                txtPreco = itemView.findViewById(R.id.txtCardPreco);
                txtImagem = itemView.findViewById(R.id.txtCardImagem);
                txtData = itemView.findViewById(R.id.txtCardData);


            }


            public void setTransferenciaFavorita(ItemFavorito itemFavorito) {
                txtDescricao.setText(itemFavorito.descricao);
                txtPreco.setText(FORMATADOR_MOEDA.format(itemFavorito.valor));
                txtData.setText(itemFavorito.data);

                Categoria categoria = null;


                if (itemFavorito.valor.compareTo(BigDecimal.ZERO) > 0) { //receita
                    txtPreco.setTextColor(TransferenciaFavoritasActivity.this.getResources().getColor(R.color.verdePositivo));


                    for (Categoria cat : Categoria.RECEITAS) {
                        if (cat.idCategoria == itemFavorito.categoria) {
                            categoria = cat;
                        }
                    }


                } else { //despesa
                    for (Categoria cat : Categoria.DESPESAS) {
                        if (cat.idCategoria == itemFavorito.categoria) {
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

    public static TextView label(String texto, Context context){
        TextView textView = new TextView(context);
        textView.setLayoutParams(PARAMS_DISTANCIA_CIMA);
        textView.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelLarge);
        textView.setText(texto);
        return textView;
    }

    public static TextView texto(String texto, Context context){
        TextView textView = new TextView(context);
        textView.setLayoutParams(PARAMS);
        textView.setText(texto);
        return textView;
    }

    public static final LayoutParams PARAMS = new LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
    );

    public static final LayoutParams PARAMS_DISTANCIA_CIMA = new LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
    );

    public static void atualizarDensidade(Context context){
        PARAMS.leftMargin = (int) ( 16 * context.getResources().getDisplayMetrics().density);
        PARAMS_DISTANCIA_CIMA.leftMargin = (int) ( 16 * context.getResources().getDisplayMetrics().density);
        PARAMS_DISTANCIA_CIMA.topMargin = (int) ( 8 * context.getResources().getDisplayMetrics().density);
    }
}







