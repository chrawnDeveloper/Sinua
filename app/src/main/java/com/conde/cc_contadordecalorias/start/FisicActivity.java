package com.conde.cc_contadordecalorias.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.conde.cc_contadordecalorias.MainActivity;
import com.conde.cc_contadordecalorias.databinding.ActivityFisicBinding;

public class FisicActivity extends AppCompatActivity {

    private ActivityFisicBinding binding;
    private RadioButton radioNenhuma, radioLeve, radioModerada, radioAvancada, radioAtleta;
    private SharedPreferences prefs;
    private Button buttonSeguir;
    private String chaveAtividade, aberto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFisicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        radioNenhuma = binding.radioNenhuma;
        radioLeve = binding.radioLeve;
        radioModerada = binding.radioModerada;
        radioAvancada = binding.radioAvancada;
        radioAtleta = binding.radioAtleta;
        chaveAtividade = "chaveAtividade";
        prefs = getSharedPreferences("banco", Context.MODE_PRIVATE);
        aberto = prefs.getString("aberto","");
        buttonSeguir = binding.buttonSeguir;

        checkAvailable();
        buttonSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void checkAvailable(){
        if (!aberto.isEmpty()){
            Intent intent= new Intent(FisicActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void save(){
        Boolean nenhumaMarcada = radioNenhuma.isChecked() || radioLeve.isChecked() || radioModerada.isChecked()
                || radioAvancada.isChecked() || radioAtleta.isChecked();

        if(nenhumaMarcada == false){//Se todas não estiverem preenchidas
            Toast.makeText(this, "Selecione um campo!", Toast.LENGTH_SHORT).show();
        }

        else{
            if (binding.radioNenhuma.isChecked()){
                prefs.edit().putString(chaveAtividade, "Nenhuma").apply();
            }

            else if(binding.radioLeve.isChecked()){
                prefs.edit().putString(chaveAtividade, "Leve").apply();
            }

            else if(binding.radioModerada.isChecked()){
                prefs.edit().putString(chaveAtividade, "Moderada").apply();
            }

            else if (binding.radioAvancada.isChecked()){
                prefs.edit().putString(chaveAtividade, "Avançada").apply();
            }

            else if (binding.radioAtleta.isChecked()){
                prefs.edit().putString(chaveAtividade, "Atleta").apply();
            }
            prefs.edit().putString("aberto", "aberto").apply();
            Intent intent= new Intent(FisicActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}