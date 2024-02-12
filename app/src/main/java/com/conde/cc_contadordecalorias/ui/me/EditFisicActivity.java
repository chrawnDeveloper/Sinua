package com.conde.cc_contadordecalorias.ui.me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.conde.cc_contadordecalorias.databinding.ActivityEditFisicBinding;

public class EditFisicActivity extends AppCompatActivity {

    private ActivityEditFisicBinding binding;
    private Button buttonSalvar;

    private RadioButton radioNenhuma, radioLeve, radioModerada, radioAvancada, radioAtleta;
    private SharedPreferences prefs;
    private String chaveAtividade, atividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditFisicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        radioNenhuma = binding.radioNenhuma;
        radioLeve = binding.radioLeve;
        radioModerada = binding.radioModerada;
        radioAvancada = binding.radioAvancada;
        radioAtleta = binding.radioAtleta;
        buttonSalvar = binding.buttonSalvar;
        prefs = getSharedPreferences("banco", Context.MODE_PRIVATE);
        chaveAtividade = "chaveAtividade";
        atividade = prefs.getString(chaveAtividade, "");



        changeRadio();
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

    }

    public void salvar(){
        Boolean nenhuma = radioNenhuma.isChecked();
        Boolean leve = radioLeve.isChecked();
        Boolean moderada = radioModerada.isChecked();
        Boolean avancada = radioAvancada.isChecked();
        Boolean atleta = radioAtleta.isChecked();
        if(!nenhuma&&leve&&moderada&&avancada&&atleta){//Se todas não estiverem preenchidas
            Toast.makeText(this, "Selecione um campo!", Toast.LENGTH_SHORT).show();
        }

        else{
            if (nenhuma){
                prefs.edit().putString(chaveAtividade, "Nenhuma").apply();
            }

            else if(leve){
                prefs.edit().putString(chaveAtividade, "Leve").apply();
            }

            else if(moderada){
                prefs.edit().putString(chaveAtividade, "Moderada").apply();
            }

            else if (avancada){
                prefs.edit().putString(chaveAtividade, "Avançada").apply();
            }

            else if (atleta){
                prefs.edit().putString(chaveAtividade, "Atleta").apply();
            }
            finish();
        }

    }

    @Override
    protected void onResume() {
        changeRadio();
        super.onResume();
    }

    public void changeRadio(){
        if (atividade.equals( "Nenhuma")){
            radioNenhuma.setChecked(true);
        }

        else if (atividade.equals("Leve") ){
            radioLeve.setChecked(true);
        }

        else if (atividade.equals("Moderada")){
            radioModerada.setChecked(true);
        }

        else if (atividade.equals("Avançada")){
            radioAvancada.setChecked(true);
        }

        else if(atividade.equals("Atleta")){
           radioAtleta.setChecked(true);
        }
    }
}