package com.conde.cc_contadordecalorias.ui.me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.conde.cc_contadordecalorias.databinding.ActivityEditBinding;
import com.conde.cc_contadordecalorias.databinding.ActivityNewAlimentoBinding;

public class EditActivity extends AppCompatActivity {
    private ActivityEditBinding binding;
    private String chaveSexo, chaveNome, chaveIdade, chavePeso, chaveAltura, sexo, idade, peso, altura, nome;
    private SharedPreferences prefs;
    Button buttonSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Editar dados");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEditBinding.inflate(getLayoutInflater());
        prefs = getSharedPreferences("banco", Context.MODE_PRIVATE);
        chaveSexo = "chaveSexo";
        chaveIdade = "chaveIdade";
        chavePeso = "chavePeso";
        chaveAltura = "chaveAltura";
        chaveNome = "chaveNome";
        sexo = prefs.getString(chaveSexo, "");
        idade = prefs.getString(chaveIdade, "");
        peso = prefs.getString(chavePeso, "");
        altura = prefs.getString(chaveAltura, "");
        nome = prefs.getString(chaveNome, "");
        buttonSalvar = binding.buttonSalvar;
        RadioButton radioFem = binding.radioFeminino;
        RadioButton radioMasc = binding.radioMasculino;

        EditText editAltura = binding.editAltura;
        EditText editPeso = binding.editPeso;
        EditText editIdade = binding.editIdade;
        EditText editNome = binding.editNome;


        changeData(editPeso, editAltura, editIdade, editNome);
        changeRadio(radioMasc, radioFem);

        setContentView(binding.getRoot());

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        EditText editNome, editAltura, editIdade, editPeso;
        RadioButton radioMasc, radioFem;

        radioFem = binding.radioFeminino;
        radioMasc = binding.radioMasculino;
        editNome = binding.editNome;
        editAltura = binding.editAltura;
        editPeso = binding.editPeso;;
        editIdade = binding.editIdade;

        String idadeResume = prefs.getString(chaveIdade, "");
        String pesoResume = prefs.getString(chavePeso, "");
        String alturaResume = prefs.getString(chaveAltura, "");
        String sexoResume = prefs.getString(chaveSexo, "");
        String nomeResume = prefs.getString(chaveNome, "");

        editNome.setText(nomeResume);
        editAltura.setText(alturaResume);
        editPeso.setText(pesoResume);
        editIdade.setText(idadeResume);

        if(sexoResume.equals("Masculino")){
            radioMasc.setChecked(true);
        }
        else if (sexoResume.equals("Feminino")){
            radioFem.setChecked(true);
        }



    }

    public void changeRadio(RadioButton radioMasc, RadioButton radioFem) {
        if (sexo.equals("Masculino")) {
            radioMasc.setChecked(true);
        } else if (sexo.equals("Feminino")) {
            radioFem.setChecked(true);
        }
    }

    public void changeData(EditText editPeso, EditText editAltura, EditText editIdade, EditText editNome) {
        editPeso.setText(peso);
        editAltura.setText(altura);
        editIdade.setText(idade);
        editNome.setText(nome);
    }

    public void salvar() {
        RadioButton radioMasc = binding.radioMasculino;
        RadioButton radioFem = binding.radioFeminino;
        String idadeFinal, pesoFinal, alturaFinal, nomeFinal, sexoFinalMasc, sexoFinalFem;
        nomeFinal = binding.editNome.getText().toString();
        alturaFinal = binding.editAltura.getText().toString();
        pesoFinal = binding.editPeso.getText().toString();
        idadeFinal = binding.editIdade.getText().toString();
        Double alturaDouble = Double.parseDouble(alturaFinal);
        Double pesoDouble = Double.parseDouble(pesoFinal);
        Double idadeDouble = Double.parseDouble(idadeFinal);
        sexoFinalFem = "Feminino";
        sexoFinalMasc = "Masculino";

        Boolean c1 = nomeFinal.isEmpty();
        Boolean c2 = alturaFinal.isEmpty();
        Boolean c3 = idadeFinal.isEmpty();
        Boolean c4 = pesoFinal.isEmpty();
        Boolean c5 = radioMasc.isChecked() || radioFem.isChecked();
        Boolean c6 = radioMasc.isChecked();
        Boolean c7 = radioFem.isChecked();

        if (c1 || c2 || c3 || c4 || c5 == false) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else {

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

                prefs.edit().putString(chaveAltura, alturaFinal).commit();
                prefs.edit().putString(chaveIdade, idadeFinal).apply();
                prefs.edit().putString(chavePeso, pesoFinal).apply();
                prefs.edit().putString(chaveNome, nomeFinal).apply();

                if (c6) {
                    prefs.edit().putString(chaveSexo, sexoFinalMasc).apply();
                } else if (c7) {
                    prefs.edit().putString(chaveSexo, sexoFinalFem).apply();
                }
                Toast.makeText(this, "Salvo com sucesso :)", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
                finish();
            }

        }


    }

}
