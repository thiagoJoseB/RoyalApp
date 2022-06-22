package com.example.royalapp.activity;

import static com.example.royalapp.Utilidades.FORMATADOR_DIA;
import static com.example.royalapp.Utilidades.FORMATADOR_MOEDA;
import static com.example.royalapp.Utilidades.PATTERN_DINHEIRO_BR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.royalapp.R;
import com.example.royalapp.model.Categoria;
import com.example.royalapp.remote.API;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.RequestBody;

public class NovaTransferenciaActivity extends AppCompatActivity {
    private static final String[] TIPO_TRANSFERENCIAS = new String[]{ "DIAS", "SEMANAS", "QUINZENAS", "MESES", "BIMESTRES", "TRIMESTRES", "SEMESTRES", "ANOS"};


    private final Calendar calendario = Calendar.getInstance();
    private List<Categoria> categorias;
    private String modo;

    private EditText inputValor;
    private EditText inputDescricao;
    private TextView textTitulo;
    private TextView textData;
    private Spinner spinnerCategorias;
    private View buttonGravar;


    private EditText inputObservacao;
    private EditText inputParcelas;
    private Spinner spinnerFrequenciaRepeticao;

    private TextView textInfoParcelas;

    LinearLayout btnRepeticao;
    LinearLayout btnObservacao;
    LinearLayout btnAnexo;
    LinearLayout btnFavorito;

    View viewRepeticao;
    View viewObservacao;
    View viewAnexo;

    TextView textAnexoNome;

    ImageView imageBotaoRepeticao;
    ImageView imageBotaoObservacao;
    ImageView imageBotaoAnexo;
    ImageView imageBotaoFavorito;

    Drawable drawableBotaoPrivilegiado;
    Drawable drawableBotaoNegao;

    CheckBox fixaCheckbox;

    boolean repetida = false;
    boolean observacao = false;
    boolean anexo = false;
    boolean favorita = false;

    Uri anexoUri = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_transferencia);

        instanciadores();

        btnRepeticao.setOnClickListener(view -> alteraRepeticao());
        btnObservacao.setOnClickListener(view -> alteraObservacao());
        btnAnexo.setOnClickListener(view -> alteraAnexo());
        btnFavorito.setOnClickListener(view -> alteraFavorito());

        inputParcelas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() > 0 && charSequence.charAt(0) == '0'){
                inputParcelas.setText("");
                }else {
                    calcularParcelas(charSequence.toString(), spinnerFrequenciaRepeticao.getSelectedItemPosition());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        spinnerFrequenciaRepeticao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                calcularParcelas(inputParcelas.getText().toString(),i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Centralizar texto da toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modo = this.getIntent().getStringExtra("modo");
        categorias = modo.equals("despesa") ? Categoria.DESPESAS : Categoria.RECEITAS;

        fixaCheckbox.setOnClickListener(v -> {
            inputParcelas.setEnabled(!fixaCheckbox.isChecked());

            calcularParcelas(inputParcelas.getText().toString(), spinnerFrequenciaRepeticao.getSelectedItemPosition());
        });

        inputValor.addTextChangedListener(new TextWatcher() {
            int cursor = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cursor = inputValor.getSelectionEnd();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                inputValor.removeTextChangedListener(this);

                String formatted = FORMATADOR_MOEDA.format(parsearCaixaDeTexto(PATTERN_DINHEIRO_BR.matcher(charSequence).replaceAll("")));

                inputValor.setText(formatted);
                inputValor.setSelection(Math.min(cursor, formatted.length()));
                inputValor.addTextChangedListener(this);

                calcularParcelas(inputParcelas.getText().toString(), spinnerFrequenciaRepeticao.getSelectedItemPosition());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        textTitulo.setText("Nova " + modo);

        textData = findViewById(R.id.nova_transferencia_data);
        textData.setText(FORMATADOR_DIA.format(calendario.getTime()));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias.stream().map(c -> c.nome).collect(Collectors.toList()));
        spinnerCategorias.setAdapter(adapter);
    }

    public void gravar(View v){
        String valorSpinner = spinnerCategorias.getSelectedItem().toString();
        boolean fixa = fixaCheckbox.isChecked();

        new Thread(() -> {
            Map<String, Object> json = new HashMap<>();

            final String anexo;

            if(anexoUri != null){
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(anexoUri);

                    byte[] buf = new byte[inputStream.available()];
                    while (inputStream.read(buf) != -1);

                    anexo = API.get().enviaFoto(DashboardActivity.token, RequestBody.create(buf)).execute().body();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                anexo = null;
            }

            json.put("metodo", modo);
            json.put("arg", "inserir");
            json.put("valor", parsearCaixaDeTexto(inputValor.getText().toString()));
            json.put("data", new java.sql.Date(calendario.getTime().getTime()).toString());
            json.put("descricao", inputDescricao.getText().toString());
            json.put("favorito", favorita);
            json.put("fixa", fixa);
            json.put("anexo", anexo);
            json.put("totalParcelas", repetida ^ fixa ? Integer.parseInt(inputParcelas.getText().toString()) : null);
            json.put("frequencia", repetida ? TIPO_TRANSFERENCIAS[spinnerFrequenciaRepeticao.getSelectedItemPosition()] : null);
            json.put("observacao", observacao ? inputObservacao.getText().toString() : null);
            json.put("parcelada", repetida ^ fixa);
            json.put("idCategoria", categorias.stream().filter(categoria -> valorSpinner.equals(categoria.nome)).findAny().get().idCategoria);

            DashboardActivity.webSocket.send(new GsonBuilder().serializeNulls().create().toJson(json));
        }).start();




        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 69){

            if(resultCode == RESULT_OK) {

                anexoUri = data.getData();

                Cursor c = getContentResolver().query(anexoUri, null, null, null, null);
                c.moveToFirst();
                String nome = c.getString(c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                c.close();

                textAnexoNome.setText(nome);
                textAnexoNome.setTypeface(null, Typeface.BOLD);
            } else {
                anexoUri = null;

                textAnexoNome.setText("Nenhum arquivo selecionado.");
                textAnexoNome.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public void mostrarData(View v) {
        DialogFragment newFragment = new DatePickerFragment(calendario, () -> {
            textData.setText(FORMATADOR_DIA.format(calendario.getTime()));
            calcularParcelas(inputParcelas.getText().toString(), spinnerFrequenciaRepeticao.getSelectedItemPosition());
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void alteraRepeticao() {
        if (repetida) {
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
        if (observacao) {
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
        if (anexo) {
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
        if (favorita) {
            btnFavorito.setBackground(drawableBotaoPrivilegiado);
            imageBotaoFavorito.setColorFilter(this.getColor(R.color.black));
            favorita = false;

        } else {
            favorita = true;
            btnFavorito.setBackground(drawableBotaoNegao);
            imageBotaoFavorito.setColorFilter(this.getColor(R.color.white));

        }

    }

    private void instanciadores(){

        btnRepeticao = this.findViewById(R.id.btnRepeticao);
        btnObservacao = this.findViewById(R.id.btnObservacao);
        btnAnexo = this.findViewById(R.id.btnAnexo);
        btnFavorito = this.findViewById(R.id.btnFavorito);

        viewRepeticao = this.findViewById(R.id.nova_transferencia_campo_repeticao);
        viewObservacao = this.findViewById(R.id.nova_transferencia_campo_observacao);
        viewAnexo = this.findViewById(R.id.nova_transferencia_campo_anexo);

        textAnexoNome = this.findViewById(R.id.nova_transferencia_anexo_nome);

        imageBotaoRepeticao = this.findViewById(R.id.nova_transferencia_image_repeticao);
        imageBotaoObservacao = this.findViewById(R.id.nova_transferencia_image_observacao);
        imageBotaoAnexo = this.findViewById(R.id.nova_transferencia_image_anexo);
        imageBotaoFavorito = this.findViewById(R.id.nova_transferencia_image_favorito);

        drawableBotaoPrivilegiado = this.getDrawable(R.drawable.botoes_transferencia2);
        drawableBotaoNegao = this.getDrawable(R.drawable.botoes_transferencia);

        inputObservacao = findViewById(R.id.nova_transferencia_observacao);

        spinnerFrequenciaRepeticao = findViewById(R.id.nova_transferencia_spinner_frequencia_repeticao);
        inputParcelas = findViewById(R.id.nova_transferencia_parcelas);
        textInfoParcelas = findViewById(R.id.nova_transferencia_texto_informacao);

        inputValor = findViewById(R.id.nova_transferencia_valor);

        inputDescricao = findViewById(R.id.nova_transferencia_descricao);

        textTitulo = findViewById(R.id.nova_transferencia_titulo);

        spinnerCategorias = findViewById(R.id.nova_transferencia_spinner_categorias);

        buttonGravar = findViewById(R.id.nova_transferencia_gravar);

        fixaCheckbox = findViewById(R.id.nova_transferencia_check_transferencia);
    }

    public void buscarArquivo(View v){
        Intent i = new Intent(Intent.ACTION_PICK);

        i.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        startActivityForResult(i, 69);
    }

    public void calcularParcelas(@NonNull String parcelas, int indiceFrequencia){
        if(fixaCheckbox.isChecked()){
            textInfoParcelas.setText("Serão criadas várias parcelas a partir do dia " + FORMATADOR_DIA.format(calendario.getTime()));
        } else {

            if (parcelas.isEmpty()) {
                textInfoParcelas.setText("Insira o valor da parcela");
            } else {
                String frequencia = TIPO_TRANSFERENCIAS[indiceFrequencia];
                int parsa = Integer.parseInt(parcelas);
                Calendar calendar = (Calendar) calendario.clone();

                switch (frequencia) {
                    case "DIAS":
                        calendar.add(Calendar.DAY_OF_MONTH, parsa - 1);
                        break;

                    case "SEMANAS":
                        calendar.add(Calendar.WEEK_OF_YEAR, parsa - 1);
                        break;
                    case "QUINZENAS":
                        calendar.add(Calendar.DAY_OF_MONTH, (parsa - 1) * 15);
                        break;
                    case "MESES":
                        calendar.add(Calendar.MONTH, (parsa - 1));
                        break;
                    case "BIMESTRES":
                        calendar.add(Calendar.MONTH, (parsa - 1) * 2);
                        break;
                    case "TRIMESTRES":
                        calendar.add(Calendar.MONTH, (parsa - 1) * 3);
                        break;
                    case "SEMESTRES":
                        calendar.add(Calendar.MONTH, (parsa - 1) * 6);
                        break;
                    case "ANOS":
                        calendar.add(Calendar.YEAR, (parsa - 1));
                        break;

                }

                BigDecimal[] resuntado =
                        parsearCaixaDeTexto(inputValor.getText().toString()).multiply(CEM).divideAndRemainder(new BigDecimal(parsa));

                resuntado[1] = resuntado[1].setScale(0, RoundingMode.UNNECESSARY);
                resuntado[0] = resuntado[0].divide(CEM, 2, RoundingMode.DOWN);

                String mensagem;

                Log.d("teste", Arrays.toString(resuntado));

                if (resuntado[1].equals(BigDecimal.ZERO)) {
                    mensagem = parcelas + " parcelas de " + FORMATADOR_MOEDA.format(resuntado[0]) + " cada";
                } else {
                    mensagem = resuntado[1] + " parcelas de " + FORMATADOR_MOEDA.format(resuntado[0].add(new BigDecimal("0.01"))) + " e " + new BigDecimal(parsa).subtract(resuntado[1]) + " parcelas de " + FORMATADOR_MOEDA.format(resuntado[0]);
                }


                textInfoParcelas.setText("Serão " + mensagem + " com final em " + FORMATADOR_DIA.format(calendar.getTime()));
            }
        }
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

    public static BigDecimal parsearCaixaDeTexto(String texto){
        String cleanString = texto.replaceAll("[R$,.\\s]", "");

        return new BigDecimal(cleanString).divide(CEM, 2, RoundingMode.DOWN);
    }

    private static final BigDecimal CEM = new BigDecimal(100);

}