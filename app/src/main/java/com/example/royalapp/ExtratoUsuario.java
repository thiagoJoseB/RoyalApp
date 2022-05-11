package com.example.royalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExtratoUsuario extends AppCompatActivity   {

    private Spinner menu;
    private Spinner menu2;

    String[] opcoes = {"janeiro" , "fevereiro", "marco" , "abril" , "maio" , "junho", "julho" , "agosto",  "setembro" , "outubro" , "novembro" , "dezembro"};
    String[] ano = {"2022"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato_usuario);

        menu = findViewById(R.id.spiner_meses);
        ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,opcoes);
        menu.setAdapter(adapter);

        menu2 = findViewById(R.id.spiner_ano);
        ArrayAdapter<String>  adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,ano);
        menu2.setAdapter(adapter2);




}




    /**09 passo aula de recyclerView*///
    private class  ExtratoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }


        /**10 passo aula de recyclerView*///
        class ExtratoViewHolder extends RecyclerView.ViewHolder{
            /// 11 recycler



            public ExtratoViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}
