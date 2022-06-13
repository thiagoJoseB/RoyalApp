package com.example.royalapp.activity;

import static com.example.royalapp.activity.InicioActivity.dormir;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.royalapp.Extra;
import com.example.royalapp.R;
import com.example.royalapp.remote.API;
import com.example.royalapp.remote.Imagem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private static String nome;
    private static String email;
    private static String arquivo;
    private static Bitmap imagem;

    EditText txtNomeCompleto;
    EditText txtEmailPerfil;
    ImageView imagePerfil;
    ProgressBar progressPerfil;
    Button btnAlterarSenha;
    Button btnMudarAlteracoes;

    Uri anexoUri = null;
    Uri anexoUriBackup = null;
    boolean foiMudadoAlgo = false;

    private BottomNavigationView menuBaixo;

    private android.app.AlertDialog dialogoSeletorImagem;
    private ImageView imagemSeletorImagem;

    public void enviarAlteracoes(View e){
        if(foiMudadoAlgo){

                android.app.AlertDialog a = new android.app.AlertDialog.Builder(this)
                        .setView(new ProgressBar(this))
                        .setMessage("Aguarde enquanto atualizamos perfil")
                        .setTitle("Enviar mensagem").create();
                a.show();


                Extra.rodar(() -> {
                    long inicio = System.currentTimeMillis();

                    if(anexoUri != null){
                        imagem = MediaStore.Images.Media.getBitmap(this.getContentResolver(), anexoUri);
                        InputStream inputStream = this.getContentResolver().openInputStream(anexoUri);

                        byte[] buf = new byte[inputStream.available()];
                        while (inputStream.read(buf) != -1);

                        arquivo = Imagem.get().enviaFoto(DashboardActivity.token, RequestBody.create(buf)).execute().body();
                    }

                    nome = txtNomeCompleto.getText().toString();
                    API.get().setPerfil(new NomeDuasEtapasFoto(nome, false, arquivo), DashboardActivity.token).execute();

                    int demora = (int) (System.currentTimeMillis() - inicio);

                    if(demora < 1500) {
                        dormir(1500 - demora);
                    }

                    a.dismiss();
                });






            foiMudadoAlgo = false;
            bloquearAlteracoes();
        }
    }

    private void instanciadores(){
        txtNomeCompleto = findViewById(R.id.txtNomeCompleto);
        txtEmailPerfil = findViewById(R.id.txtEmailPerfil);
        btnAlterarSenha = findViewById(R.id.perfil_botao_editar);
        btnMudarAlteracoes = findViewById(R.id.perfil_botao_mudar_alteracoes);

        menuBaixo = findViewById(R.id.perfil_menu_baixo);
        progressPerfil = findViewById(R.id.perfil_foto_progressbar);
        imagePerfil = findViewById(R.id.perfil_foto_imageview);
    }

    private void botaInfos(){

        txtNomeCompleto.setText(nome);
        txtEmailPerfil.setText(email);

        progressPerfil.setVisibility(View.GONE);

        imagePerfil.setForeground(null);

        if(imagem != null){
            imagePerfil.setImageBitmap(imagem);
        }

        txtNomeCompleto.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    foiMudadoAlgo = true;
                    liberarAlteracoes();
                    txtNomeCompleto.removeTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        instanciadores();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);



        menuBaixo.setSelectedItemId(R.id.menu_baixo_perfil);


        btnAlterarSenha.setOnClickListener(view -> {
            Intent intent = new Intent(PerfilUsuarioActivity.this, AlterarSenhaPerfil.class);
            startActivity(intent);
        });



        menuBaixo.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_baixo_geral: {
                    Intent intent = new Intent(PerfilUsuarioActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_graficos: {
                    Intent intent = new Intent(this, GraficosActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_extratos: {
                    Intent intent = new Intent(PerfilUsuarioActivity.this, ExtratoUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                    break;
                }
                case R.id.menu_baixo_perfil: {
                    break;
                }
            }

            return false;
        });

        if(email == null){
            //primeira vez
                Extra.rodar(() -> {
                    String jsonString = API.get().getPerfil(DashboardActivity.token).execute().body();

                    assert jsonString != null;
                    JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

                    email = json.get("email").getAsString();
                    nome = json.get("nome").getAsString();

                    runOnUiThread(() -> {
                        txtNomeCompleto.setText(nome);
                        txtEmailPerfil.setText(email);
                    });

                    if(!json.get("foto").isJsonNull()){
                        arquivo = json.get("foto").getAsString();

                        imagem = BitmapFactory.decodeStream(
                                Imagem.get().imagem(arquivo, DashboardActivity.token).execute()
                                        .body().byteStream()
                        );

                        runOnUiThread(() -> {

                        imagePerfil.setImageBitmap(imagem);
                        });
                    }

                    runOnUiThread(() -> {
                        progressPerfil.setVisibility(View.GONE);

                        imagePerfil.setForeground(null);

                        txtNomeCompleto.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                foiMudadoAlgo = true;
                                liberarAlteracoes();
                                txtNomeCompleto.removeTextChangedListener(this);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                    });


                });
        } else {

            botaInfos();
        }

    }

    public void deslogar(View a) {
        new AlertDialog.Builder(this)
                .setMessage("Deseja sair da sua conta?")
                .setNegativeButton("Cancelar", (v, i) -> {
                })
                .setPositiveButton("Sair", (v, i) -> {
                    DashboardActivity.token = null;

                    getSharedPreferences("data", MODE_PRIVATE).edit().remove("token").apply();

                    Intent intent = new Intent(PerfilUsuarioActivity.this, LoginUsuarioActivity.class);
                    startActivity(intent);
                    this.finish();
                }).create().show();
    }



    public void buscarArquivo(View v){
        if(dialogoSeletorImagem == null){
            imagemSeletorImagem = new ImageView(this);
            imagemSeletorImagem.setAdjustViewBounds(true);
            imagemSeletorImagem.setImageDrawable(this.getDrawable(R.drawable.sem_foto));

            dialogoSeletorImagem = new android.app.AlertDialog.Builder(this)
                    .setTitle("Escolha uma foto")
                    .setView(imagemSeletorImagem)
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    })
                    .setItems(new String[]{"Escolher da galeria", "Usar a câmera", "Confirmar"}, null).create();

            dialogoSeletorImagem.getListView().setOnItemClickListener((dialog, view, index, id) -> {
                switch (index){
                    case 0:{
                        Intent ixx = new Intent(Intent.ACTION_GET_CONTENT);
                        ixx.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(ixx, 69);
                        break;
                    }
                    case 1:{
                        String imageFileName = String.valueOf(System.currentTimeMillis());
                        File image = null;
                        try {
                            image = File.createTempFile(
                                    imageFileName,
                                    ".jpg",
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        anexoUriBackup = anexoUri;
                        anexoUri = Uri.fromFile(image);

                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, anexoUri);
                        startActivityForResult(i, 666);
                        break;
                    }
                    case 2:{
                        if(anexoUri != null){
                            foiMudadoAlgo = true;
                            try {
                                imagePerfil.setImageBitmap(
                                        MediaStore.Images.Media.getBitmap(this.getContentResolver(), anexoUri)
                                );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            liberarAlteracoes();
                        }

                        dialogoSeletorImagem.dismiss();
                    }
                }
            });
        }

        dialogoSeletorImagem.show();
    }

    private void liberarAlteracoes() {
        btnMudarAlteracoes.setVisibility(View.VISIBLE);
    }

    private void bloquearAlteracoes() {
        btnMudarAlteracoes.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("teste", String.valueOf(requestCode));
        Log.d("teste", String.valueOf(resultCode));

        if(requestCode == 69){
            if(resultCode == RESULT_OK) {
                anexoUri = data.getData();

                Toast.makeText(this, "teste", Toast.LENGTH_SHORT).show();
                try {
                    imagemSeletorImagem.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), anexoUri));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if(requestCode == 666){
            if(resultCode == RESULT_OK) {
                try {
                    imagemSeletorImagem.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), anexoUri));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                anexoUri = anexoUriBackup;
            }
        }
    }

    public static class NomeDuasEtapasFoto{
        public String nome;
        public boolean duasetapas;
        public String foto;

        public NomeDuasEtapasFoto(String nome, boolean duasetapas, String foto) {
            this.nome = nome;
            this.duasetapas = duasetapas;
            this.foto = foto;
        }
    }

}





