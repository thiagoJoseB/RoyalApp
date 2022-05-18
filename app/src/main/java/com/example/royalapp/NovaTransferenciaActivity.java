package com.example.royalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.royalapp.model.Categoria;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NovaTransferenciaActivity extends AppCompatActivity {

    private final Calendar calendario = Calendar.getInstance();
    private List<Categoria> categorias;
    private String modo;
    int numero = 0;

    private EditText inputValor;
    private EditText inputDescricao;
    private TextView textTitulo;
    private TextView textData;
    private Spinner spinnerCategorias;
    private View buttonGravar;




    ViewGroup repeticao;
    TextView repeticao1;
    LinearLayout  btnRepeticao;
    LinearLayout  btnObservacao;
    LinearLayout  btnAnexo;
    LinearLayout  btnFavorito;

    View viewRepeticao;
    View viewObservacao;
    View viewAnexo;

    ImageView imageBotaoRepeticao;
    ImageView imageBotaoObservacao;
    ImageView imageBotaoAnexo;
    ImageView imageBotaoFavorito;

    Drawable drawableBotaoPrivilegiado;
    Drawable drawableBotaoNegao;

    boolean repetida = false;
    boolean observacao = false;
    boolean anexo = false;
    boolean favorita = false;

    boolean checked = true;

    public void mostrarData(View v){
        DialogFragment newFragment = new DatePickerFragment(calendario, () -> {
            textData.setText(Dashboard.FORMATADOR_DIA.format(calendario.getTime()));
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");



    }


    public void onCheckboxClicked(View view) {


    }




    private void alteraRepeticao() {
        if(repetida) {
            repetida = false;
            viewRepeticao.setVisibility(View.GONE);
            btnRepeticao.setBackground(drawableBotaoPrivilegiado);
            imageBotaoRepeticao.setColorFilter(this.getColor(R.color.black));
        } else {

            repetida = true;
            viewRepeticao.setVisibility(View.VISIBLE);
            btnRepeticao.setBackground(drawableBotaoNegao);
            imageBotaoRepeticao.setColorFilter(this.getColor(R.color.white));
        }

    }

    private void alteraObservacao() {
        if(observacao) {
            observacao = false;
            viewObservacao.setVisibility(View.GONE);
            btnObservacao.setBackground(drawableBotaoPrivilegiado);
            imageBotaoObservacao.setColorFilter(this.getColor(R.color.black));


        } else {
            observacao = true;
            viewObservacao.setVisibility(View.VISIBLE);
            btnObservacao.setBackground(drawableBotaoNegao);
            imageBotaoObservacao.setColorFilter(this.getColor(R.color.white));
        }

    }

    private void alteraAnexo() {
        if(anexo) {
            viewAnexo.setVisibility(View.GONE);
            btnAnexo.setBackground(drawableBotaoPrivilegiado);
            imageBotaoAnexo.setColorFilter(this.getColor(R.color.black));
            anexo = false;
        } else {
            anexo = true;
            viewAnexo.setVisibility(View.VISIBLE);
            btnAnexo.setBackground(drawableBotaoNegao);
            imageBotaoAnexo.setColorFilter(this.getColor(R.color.white));

        }

    }


    private void alteraFavorito() {
        if(favorita) {
            btnFavorito.setBackground(drawableBotaoPrivilegiado);
            imageBotaoFavorito.setColorFilter(this.getColor(R.color.black));
            favorita = false;

        } else {
            favorita = true;
            btnFavorito.setBackground(drawableBotaoNegao);
            imageBotaoFavorito.setColorFilter(this.getColor(R.color.white));

        }

    }



















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_transferencia);

//                 btnRepeticao = (TextView) findViewById(R.id.btnRepeticao);


        this.findViewById(R.id.Repeticao);
        this.findViewById(R.id.Observacao);
        this.findViewById(R.id.Anexo);

        btnRepeticao = this.findViewById(R.id.btnRepeticao);
        btnObservacao = this.findViewById(R.id.btnObservacao);
        btnAnexo = this.findViewById(R.id.btnAnexo);
        btnFavorito = this.findViewById(R.id.btnFavorito);

        viewRepeticao =NovaTransferenciaActivity.this.findViewById(R.id.Repeticao);
        viewObservacao = NovaTransferenciaActivity.this.findViewById(R.id.Observacao);
        viewAnexo = NovaTransferenciaActivity.this.findViewById(R.id.Anexo);
//        viewFavorito = NovaTransferenciaActivity.this.findViewById(R.id.Fa);

        imageBotaoRepeticao = this.findViewById(R.id.nova_transferencia_image_repeticao);
        imageBotaoObservacao = this.findViewById(R.id.nova_transferencia_image_observacao);
        imageBotaoAnexo = this.findViewById(R.id.nova_transferencia_image_anexo);
        imageBotaoFavorito = this.findViewById(R.id.nova_transferencia_image_favorito);

        drawableBotaoPrivilegiado = this.getDrawable(R.drawable.botoes_transferencia2);
        drawableBotaoNegao = this.getDrawable(R.drawable.botoes_transferencia);







        btnRepeticao.setOnClickListener(view -> alteraRepeticao());

        btnObservacao.setOnClickListener(view -> alteraObservacao());
        btnAnexo.setOnClickListener(view -> alteraAnexo());
        btnFavorito.setOnClickListener(view -> alteraFavorito());































//        btnRepeticao.setOnClickListener(view -> {
////                System.out.println("dsfsdfdsffdsfds");
//            TransitionManager.beginDelayedTransition(NovaTransferenciaActivity.this.findViewById(R.id.Repeticao));
//            alteraBotao(numero);
//            numero += 1;
//
//
//
//        });





//ri














        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_transferencia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categorias = this.getIntent().getParcelableArrayListExtra("categorias");
        modo = this.getIntent().getStringExtra("modo");


        Log.d("teste", categorias.toString());

        inputValor = findViewById(R.id.nova_transferencia_valor);
        inputDescricao = findViewById(R.id.nova_transferencia_descricao);

        textTitulo = findViewById(R.id.nova_transferencia_titulo);
        textTitulo.setText("Nova " + modo);

        textData = findViewById(R.id.nova_transferencia_data);
        textData.setText(Dashboard.FORMATADOR_DIA.format(calendario.getTime()));

        spinnerCategorias = findViewById(R.id.nova_transferencia_spinner_categorias);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias.stream().map(c -> c.nome).collect(Collectors.toList()));
        spinnerCategorias.setAdapter(adapter);

        buttonGravar = findViewById(R.id.nova_transferencia_gravar);
        buttonGravar.setOnClickListener(view -> {
            Map<String, Object> json = new HashMap<>();
            String valorSpinner = spinnerCategorias.getSelectedItem().toString();

//            metodo: transferencia, arg: 'inserir', valor: valor, data: data, descricao: descricao, favorito: favoritado, fixa: parcelaFixa,
//                    inicioRepeticao: dataInicio.value !== '' ? dataInicio.value : null, totalParcelas: (repetido && duracao.value ? parseInt(duracao.value) : null),
//                    frequencia: dataFR.value !== '' ? dataFR.value : null, observacao: observacao, idCategoria: idCategoria, parcelada: repetido


            json.put("metodo", modo);
            json.put("arg", "inserir");
            json.put("valor", Double.parseDouble(inputValor.getText().toString()));
            json.put("data", new java.sql.Date(calendario.getTime().getTime()).toString());
            json.put("descricao", inputDescricao.getText().toString());
            json.put("favorito", favorita);
            json.put("fixa", false);
            json.put("inicioRepeticao", null);
            json.put("totalParcelas", null);
            json.put("frequencia", null);
            json.put("observacao", null);
            json.put("parcelada", false);
            json.put("idCategoria", categorias.stream().filter(categoria -> valorSpinner.equals(categoria.nome)).findAny().get().idCategoria);

            Dashboard.webSocket.send(new GsonBuilder().serializeNulls().create().toJson(json));

            this.finish();
        });
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private final Calendar calendario;
        private final Runnable rodavel;

        public DatePickerFragment(Calendar calendario, Runnable callback) {
            this.calendario = calendario;
            this.rodavel = callback;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year = calendario.get(Calendar.YEAR);
            int month = calendario.get(Calendar.MONTH);
            int day = calendario.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, month);
            calendario.set(Calendar.DAY_OF_MONTH, day);

            rodavel.run();
        }
    }
}