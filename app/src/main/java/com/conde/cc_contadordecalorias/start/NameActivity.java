package com.conde.cc_contadordecalorias.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conde.cc_contadordecalorias.databinding.ActivityFisicBinding;
import com.conde.cc_contadordecalorias.databinding.ActivityNameBinding;

public class NameActivity extends AppCompatActivity {

    private ActivityNameBinding binding;
    private Button buttonSeguir;
    private EditText editNome;
    private SharedPreferences prefs;
    private String chaveNome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        buttonSeguir = binding.buttonSeguir;
        editNome = binding.editNome;
        prefs = getSharedPreferences("banco", MODE_PRIVATE);
        chaveNome = "chaveNome";

        verifyName();

        buttonSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    public void verifyName(){
        if (!prefs.getString("nomeAberto", "").isEmpty()){
            Intent intent = new Intent(NameActivity.this, InfoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void save(){
        String nome = editNome.getText().toString();
        if(nome.isEmpty()){
            Toast.makeText(this, "Digite um nome!", Toast.LENGTH_SHORT).show();
        }

        else {
            prefs.edit().putString(chaveNome, nome).apply();
            prefs.edit().putString("nomeAberto", "nomeAberto").apply();
            Intent intent = new Intent(NameActivity.this, InfoActivity.class);
            startActivity(intent);
            finish();
        }
    }
}