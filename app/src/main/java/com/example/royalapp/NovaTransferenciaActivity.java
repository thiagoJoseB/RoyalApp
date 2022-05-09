package com.example.royalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royalapp.model.Categoria;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.WebSocket;

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
    LinearLayout  btnRepeticao4;

    View viewRepeticao;
    View viewObservacao;
    View viewAnexo;

    ImageView imageBotaoRepeticao;
    ImageView imageBotaoObservacao;
    ImageView imageBotaoAnexo;
    ImageView imageBotaoFavorito;

    Drawable drawableBotaoPrivilegiado;
    Drawable drawableBotaoNegao;

    public void mostrarData(View v){
        DialogFragment newFragment = new DatePickerFragment(calendario, () -> {
            textData.setText(Dashboard.FORMATADOR_DIA.format(calendario.getTime()));
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");



    }





    private void alteraBotao(int numero) {
        if(numero % 2 == 0) {
            viewRepeticao.setVisibility(View.GONE);
            btnRepeticao.setBackground(drawableBotaoPrivilegiado);
            imageBotaoRepeticao.setColorFilter(this.getColor(R.color.black));
        } else {
            viewRepeticao.setVisibility(View.VISIBLE);
            btnRepeticao.setBackground(drawableBotaoNegao);
            imageBotaoRepeticao.setColorFilter(this.getColor(R.color.white));
        }

    }

    private void alteraBotao2(int numero) {
        if(numero % 2 == 0) {
            viewObservacao.setVisibility(View.GONE);
            btnObservacao.setBackground(drawableBotaoPrivilegiado);
            imageBotaoObservacao.setColorFilter(this.getColor(R.color.black));


        } else {
            viewObservacao.setVisibility(View.VISIBLE);
            btnObservacao.setBackground(drawableBotaoNegao);
            imageBotaoObservacao.setColorFilter(this.getColor(R.color.white));
        }

    }

    private void alteraBotao3(int numero) {
        if(numero % 2 == 0) {
            viewAnexo.setVisibility(View.GONE);
            btnAnexo.setBackground(drawableBotaoPrivilegiado);
            imageBotaoAnexo.setColorFilter(this.getColor(R.color.black));

        } else {
            viewAnexo.setVisibility(View.VISIBLE);
            btnAnexo.setBackground(drawableBotaoNegao);
            imageBotaoAnexo.setColorFilter(this.getColor(R.color.white));

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
        btnRepeticao4 = this.findViewById(R.id.btnRepeticao4);

        viewRepeticao =NovaTransferenciaActivity.this.findViewById(R.id.Repeticao);
        viewObservacao = NovaTransferenciaActivity.this.findViewById(R.id.Observacao);
        viewAnexo = NovaTransferenciaActivity.this.findViewById(R.id.Anexo);

        imageBotaoRepeticao = this.findViewById(R.id.nova_transferencia_image_repeticao);
        imageBotaoObservacao = this.findViewById(R.id.nova_transferencia_image_observacao);
        imageBotaoAnexo = this.findViewById(R.id.nova_transferencia_image_anexo);
        imageBotaoFavorito = this.findViewById(R.id.nova_transferencia_image_favorito);

        drawableBotaoPrivilegiado = this.getDrawable(R.drawable.botoes_transferencia2);
        drawableBotaoNegao = this.getDrawable(R.drawable.botoes_transferencia);







        btnRepeticao.setOnClickListener(view -> {
//                System.out.println("dsfsdfdsffdsfds");
//            System.out.println("dsfsdfdsffdsfds");
                TransitionManager.beginDelayedTransition(NovaTransferenciaActivity.this.findViewById(R.id.Repeticao));
                alteraBotao(numero);
                numero += 1;



        });

        btnObservacao.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(NovaTransferenciaActivity.this.findViewById(R.id.Observacao));
            alteraBotao2(numero);
            numero += 1;


        });
        btnAnexo.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(NovaTransferenciaActivity.this.findViewById(R.id.Observacao));
            alteraBotao3(numero);
            numero += 1;



        });























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

            json.put("metodo", modo);
            json.put("arg", "inserir");
            json.put("pendente", null);
            json.put("data", new java.sql.Date(calendario.getTime().getTime()).toString());
            json.put("descricao", inputDescricao.getText().toString());
            json.put("valor", Double.parseDouble(inputValor.getText().toString()));
            json.put("favorito", false);
            json.put("idCategoria", categorias.stream().filter(categoria -> valorSpinner.equals(categoria.nome)).findAny().get().idCategoria);

            Log.d("teste", new Gson().toJson(json));

            Dashboard.webSocket.send(new Gson().toJson(json));

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