package com.conde.cc_contadordecalorias.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.conde.cc_contadordecalorias.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding binding;
    private Button buttonSeguir;
    private String chaveSexo, chaveIdade, chavePeso, chaveAltura;
    private EditText editPeso, editAltura, editIdade;
    private RadioGroup radioGroup;
    private RadioButton radioMasculino, radioFeminino;
    private SharedPreferences prefs;
    private Boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Adicionar calorias");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityInfoBinding.inflate(getLayoutInflater());

        prefs = getSharedPreferences("banco", MODE_PRIVATE);
        buttonSeguir = binding.buttonSeguir;
        radioMasculino = binding.radioMasculino;
        radioFeminino = binding.radioFeminino;
        editPeso = binding.editPeso;
        editAltura = binding.editAltura;
        editIdade = binding.editIdade;
        radioGroup = binding.radioGroup;

        chaveSexo = "chaveSexo";
        chaveIdade = "chaveIdade";
        chavePeso = "chavePeso";
        chaveAltura = "chaveAltura";

        verifyInfo();

        buttonSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        setContentView(binding.getRoot());
    }



    public void save() {
        String idade = editIdade.getText().toString();
        String altura = editAltura.getText().toString();
        String peso = editPeso.getText().toString();


        Boolean c1 = idade.isEmpty();
        Boolean c2 = altura.isEmpty();
        Boolean c3 = peso.isEmpty();
        Boolean c4 = radioMasculino.isChecked() || radioFeminino.isChecked();
        Boolean c5 = radioMasculino.isChecked();
        Boolean c6 = radioFeminino.isChecked();

        if (c1 || c2 || c3 || c4 == false) { // Manda preencher todos os campo
            Toast.makeText(getApplicationContext(), "Preencha todos os campos!",
                    Toast.LENGTH_SHORT).show();
        } else {

            Double alturaDouble = Double.parseDouble(altura);
            Double pesoDouble = Double.parseDouble(peso);
            Double idadeDouble = Double.parseDouble(idade);
            if(idadeDouble < 10 || idadeDouble > 110 ){
                Toast.makeText(this, "Insira uma idade válida", Toast.LENGTH_SHORT).show();
            }

            else if (pesoDouble < 30 || pesoDouble > 400 ){
                Toast.makeText(this, "Insira um peso válido", Toast.LENGTH_SHORT).show();
            }

            else if (alturaDouble < 100 || alturaDouble > 230){
                Toast.makeText(this, "Insira uma altura válida", Toast.LENGTH_SHORT).show();
            }
            else{
                prefs.edit().putString("conde", "preenchido").apply();
                prefs.edit().putString(chaveAltura, altura).apply();
                prefs.edit().putString(chavePeso, peso).apply();
                prefs.edit().putString(chaveIdade, idade).apply();

                if (c5) { //Salva na chave masculino
                    String masculino = "Masculino";
                    prefs.edit().putString(chaveSexo, masculino).apply();
                } else if (c6) { //Salva na chave feminino
                    String feminino = "Feminino";
                    prefs.edit().putString(chaveSexo, feminino).apply();
                }
                Intent intent = new Intent(InfoActivity.this, FisicActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    public void verifyInfo(){
        if (!prefs.getString("conde", "").isEmpty()){
            Intent intent = new Intent(InfoActivity.this, FisicActivity.class);
            startActivity(intent);
            finish();
        }
    }

}