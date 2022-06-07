package com.example.royalapp.activity;

import static com.example.royalapp.Utilidades.FORMATADOR_MOEDA;
import static com.example.royalapp.Utilidades.GSON;
import static com.example.royalapp.Variaveis.IMAGENS;
import static com.example.royalapp.activity.TransferenciaFavoritasActivity.label;
import static com.example.royalapp.activity.TransferenciaFavoritasActivity.texto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.royalapp.Constantes;
import com.example.royalapp.R;
import com.example.royalapp.Utilidades;
import com.example.royalapp.Variaveis;
import com.example.royalapp.model.Categoria;
import com.example.royalapp.model.Transferencia;
import com.example.royalapp.model.TransferenciaExtrato;
import com.example.royalapp.remote.API;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtratoUsuarioActivity extends AppCompatActivity {


    private List<TransferenciaExtrato> extratosBackup;
    RecyclerView recyclerView;

    private TextView seletorPeriodo;
    private TextView seletorCategoria;
    private BottomNavigationView menuBaixo;
    private final List<Categoria> categoriasAtivas = new ArrayList<>();

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
                case R.id.menu_baixo_graficos: {
                    Intent intent = new Intent(this, GraficosActivity.class);
                    startActivity(intent);
                    this.finish();
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
        seletorCategoria = findViewById(R.id.extratos_seletor_categoria);

        seletorCategoria.setOnClickListener(new View.OnClickListener() {
            View singletonView;

            @Override
            public void onClick(View v) {
                if(singletonView == null){
                    singletonView = getLayoutInflater().inflate(R.layout.dialog_selecionador_categoria, null, false);
                    LinearLayout receitasLayout = singletonView.findViewById(R.id.dialog_selecionador_categoria_list_receitas);
                    LinearLayout despesaLayout = singletonView.findViewById(R.id.dialog_selecionador_categoria_list_despesas);

                    for (Categoria receita : Categoria.RECEITAS) {
                        receitasLayout.addView(ExtratoUsuarioActivity.this.itemOptionCriador(receita));
                    }

                    for (Categoria despesa : Categoria.DESPESAS) {
                        despesaLayout.addView(ExtratoUsuarioActivity.this.itemOptionCriador(despesa));
                    }
                }else {
                    //evita erros
                    ((ViewGroup)singletonView.getParent()).removeView(singletonView);

                }

                new AlertDialog.Builder(ExtratoUsuarioActivity.this)

                        .setView(singletonView)
                        .create().show();
            }
        });


        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(Variaveis.mesAlvoInicio0, Variaveis.anoAlvo, "Data alvo", Utilidades.BRASIL);



        seletorPeriodo.setOnClickListener(view -> {
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

    }

    private void atualizarDoBackupPorCategorias(){
        List<TransferenciaExtrato> nova;

        if(categoriasAtivas.isEmpty()){
            nova = Collections.unmodifiableList(extratosBackup);
        } else {
            //faz backups dos ids
            List<Integer> ids = categoriasAtivas.stream().map(c -> c.idCategoria).collect(Collectors.toList());

            nova = extratosBackup.stream().filter(extrato -> ids.contains(extrato.categoria)).collect(Collectors.toList());
        }

        recyclerView.setAdapter(new ExtratoAdapter(nova));
    }

    private void atualizarMesAnoAlvo() {
        seletorPeriodo.setText(
                String.format(this.getString(R.string.mes_ano_seletor), Constantes.MESES[Variaveis.mesAlvoInicio0], String.valueOf(Variaveis.anoAlvo))
        );

        Call<List<TransferenciaExtrato>> call = API.get().getExtratos(DashboardActivity.token, Variaveis.anoAlvo, Variaveis.mesAlvoInicio0 + 1);

        call.enqueue(new Callback<List<TransferenciaExtrato>>() {
            @Override
            public void onResponse(Call<List<TransferenciaExtrato>> call, Response<List<TransferenciaExtrato>> response) {
                extratosBackup = Objects.requireNonNull(response.body());



                atualizarDoBackupPorCategorias();

            }

            @Override
            public void onFailure(Call<List<TransferenciaExtrato>> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }


    public void cliqueDaCheckboxCategoria(View checkBox1) {
        CheckBox checkBox = (CheckBox) checkBox1;
        Categoria categoria = (Categoria) checkBox.getTag();

        if(checkBox.isChecked()){
            categoriasAtivas.add(categoria);
        } else {
            categoriasAtivas.remove(categoria);
        }

        atualizarDoBackupPorCategorias();

        if(categoriasAtivas.isEmpty()){
            seletorCategoria.setText(this.getString(R.string.categoria_selecionador_vazio));
        } else {
            seletorCategoria.setText(categoriasAtivas.stream().map(c -> c.nome).collect(Collectors.joining(", ")));
        }

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

                TransferenciaExtrato transferenciaExtrato = itemExtratos.get(position);

            Log.d("teste", GSON.toJson(extratosBackup));
            Log.d("teste", GSON.toJson(transferenciaExtrato));

                holder.setExtratoData(transferenciaExtrato);


            holder.itemView.setOnClickListener(new View.OnClickListener() {


                private LinearLayout layoutPai;
                private Transferencia transferencia;
                private AlertDialog alertDialog;

                private void rodar(){
                    if(alertDialog == null) {

                        alertDialog = new AlertDialog.Builder(ExtratoUsuarioActivity.this)
                                .create();

                        if (layoutPai == null) {
                            layoutPai = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_transferencia, null);
                            LinearLayout layout = layoutPai.findViewById(R.id.dialog_transferencia_layout_itens);
                            ((TextView) layoutPai.findViewById(R.id.dialog_transferencia_titulo))
                                    .setText((transferencia.isDespesa() ? "Despesa" : "Receita"));
                            layoutPai.findViewById(R.id.dialog_transferencia_botao_sair).setOnClickListener(v2 -> {
                                alertDialog.dismiss();
                            });
                            View viewCarregando = layoutPai.findViewById(R.id.dialog_transferencia_carregando);


                            if (transferencia.anexo != null) {
                                ImageView imageView = layout.findViewById(R.id.dialog_transferencia_imagem);




                                if (IMAGENS.containsKey(transferencia.id)) { // tem o backup?, sim?, backup poderia ser do linear layout tmb
                                    imageView.setImageBitmap(IMAGENS.get(transferencia.id));

                                    viewCarregando.setVisibility(View.GONE);
                                } else { // pega o novo
                                    API.get().imagem(transferencia.anexo, DashboardActivity.token).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            final Bitmap bitmap = BitmapFactory
                                                    .decodeStream(response.body().byteStream()); // so pega a img uma vez;


                                            IMAGENS.put(transferencia.id, bitmap);
                                            imageView.setImageBitmap(bitmap);


                                            viewCarregando.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            throw new RuntimeException(t);
                                        }
                                    });
                                }
                            } else {
                                viewCarregando.setVisibility(View.GONE);
                            }

                            layout.addView(label("Valor", ExtratoUsuarioActivity.this));
                            layout.addView(texto(FORMATADOR_MOEDA.format(transferenciaExtrato.valor), ExtratoUsuarioActivity.this));

                            layout.addView(label("Descricao", ExtratoUsuarioActivity.this));
                            layout.addView(texto(transferencia.descricao, ExtratoUsuarioActivity.this));

                            layout.addView(label("Data", ExtratoUsuarioActivity.this));
                            layout.addView(texto(Utilidades.FORMATADOR_DIA_LONGO.format(java.sql.Date.valueOf(transferenciaExtrato.data)), ExtratoUsuarioActivity.this));

                            if (transferencia.observacao != null && !transferencia.observacao.isEmpty()) {
                                layout.addView(label("Observação", ExtratoUsuarioActivity.this));
                                layout.addView(texto(transferencia.observacao, ExtratoUsuarioActivity.this));
                            }


                            if (transferenciaExtrato.indice != null) {
                                if(transferencia.fixa){
                                    layout.addView(label("Parcelada", ExtratoUsuarioActivity.this));
                                    layout.addView(texto("Não", ExtratoUsuarioActivity.this));

                                    layout.addView(label("Fixa", ExtratoUsuarioActivity.this));
                                    layout.addView(texto("Sim", ExtratoUsuarioActivity.this));

                                    layout.addView(label("Parcela", ExtratoUsuarioActivity.this));
                                    layout.addView(texto((transferenciaExtrato.indice + 1) + " de " + transferencia.parcelas + " parcelas", ExtratoUsuarioActivity.this));

                                    layout.addView(label("Frequencia", ExtratoUsuarioActivity.this));
                                    layout.addView(texto(transferencia.nomeFrequencia.toString(), ExtratoUsuarioActivity.this));

                                } else {

                                    layout.addView(label("Parcelada", ExtratoUsuarioActivity.this));
                                    layout.addView(texto("Sim", ExtratoUsuarioActivity.this));

                                    layout.addView(label("Parcela", ExtratoUsuarioActivity.this));
                                    layout.addView(texto((transferenciaExtrato.indice + 1) + " de " + transferencia.parcelas + " parcelas", ExtratoUsuarioActivity.this));

                                    layout.addView(label("Frequencia", ExtratoUsuarioActivity.this));
                                    layout.addView(texto(transferencia.nomeFrequencia.toString(), ExtratoUsuarioActivity.this));

                                    layout.addView(label("Fixa", ExtratoUsuarioActivity.this));
                                    layout.addView(texto("Não", ExtratoUsuarioActivity.this));
                                }
                            } else {
                                layout.addView(label("Parcelada", ExtratoUsuarioActivity.this));
                                layout.addView(texto("Não", ExtratoUsuarioActivity.this));

                                layout.addView(label("Fixa", ExtratoUsuarioActivity.this));
                                layout.addView(texto("Não", ExtratoUsuarioActivity.this));
                            }

                            alertDialog.setView(layoutPai);
                        }


                    }
                    alertDialog.show();
                }

                @Override
                public void onClick(View v) {


                    if(transferencia == null){
                        API.get().getTransferencia(DashboardActivity.token, transferenciaExtrato.id).enqueue(new Callback<List<Transferencia>>() {
                            @Override
                            public void onResponse(Call<List<Transferencia>> call, Response<List<Transferencia>> response) {
                                transferencia = (response.body().get(0));
                                rodar();
                            }

                            @Override
                            public void onFailure(Call<List<Transferencia>> call, Throwable t) {
                                throw new RuntimeException(t);
                            }
                        });
                    } else {
                        rodar();
                    }




                }


            });


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

    }


