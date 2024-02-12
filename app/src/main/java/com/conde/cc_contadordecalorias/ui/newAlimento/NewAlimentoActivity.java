package com.conde.cc_contadordecalorias.ui.newAlimento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conde.cc_contadordecalorias.databinding.ActivityNewAlimentoBinding;
import com.conde.cc_contadordecalorias.helper.AlimentoDAO;
import com.conde.cc_contadordecalorias.ui.data.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewAlimentoActivity extends AppCompatActivity {
    private ActivityNewAlimentoBinding binding;
    private Button buttonSalvarAlimento;
    private EditText editAlimento;
    private EditText editCalorias;
    private Alimento alimentoAtual; //Recuperar o alimento atual
    private Data data;
    private SharedPreferences prefs;
    private String chaveCaloriasDiarias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Adicionar calorias");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityNewAlimentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editAlimento = binding.editAddAlimento;
        editCalorias = binding.editAddCalorias;
        alimentoAtual = (Alimento) getIntent().getSerializableExtra("alimentoSelecionado");
        buttonSalvarAlimento = binding.buttonSalvarAlimento;
        prefs = getSharedPreferences("banco", MODE_PRIVATE);
        chaveCaloriasDiarias = "chaveCaloriasDiarias";

        data = new Data();
        buttonSalvarAlimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        //Configurar tarefa na caixa de texto

        if (alimentoAtual != null) {
            editAlimento.setText(alimentoAtual.getNomeAlimento());
            editCalorias.setText(alimentoAtual.getCaloriaAlimento());
            data.setDataSalva(alimentoAtual.getDataAlimento()); //Transforma data na que pegar
        } else {
            Date date = Calendar.getInstance().getTime(); //PEGAR O DIA DE HOJE
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateTexto = dateTimeFormat.format(date);
            data.setDataSalva(dateTexto); // Seta a data para o dia de hoje
        }

    }

    public void salvar() {

        String nomeAlimento = editAlimento.getText().toString();
        String quantCalorias = editCalorias.getText().toString();
        String dia = data.getDataSalva();
        AlimentoDAO alimentoDAO = new AlimentoDAO(getApplicationContext());
        int quantCaloriasInt = Integer.parseInt(quantCalorias);

        if (quantCaloriasInt <1 || quantCaloriasInt >12000){
            Toast.makeText(this, "Insira uma quantidade válida", Toast.LENGTH_SHORT).show();
        }
        else{
            if (alimentoAtual != null) {//Editando

                if (nomeAlimento.isEmpty() || quantCalorias.isEmpty()) {
                    Toast.makeText(this, "Digite todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    Alimento alimento = new Alimento();
                    alimento.setId(alimentoAtual.getId());
                    alimento.setNomeAlimento(nomeAlimento);
                    alimento.setCaloriaAlimento(quantCalorias);
                    alimento.setDataAlimento(dia);

                    //Database
                    if (alimentoDAO.atualizar(alimento)) {
                        Toast.makeText(getApplicationContext(), "Atualizado com sucesso :)", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao atualizar :(", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {//Salvando
                if (nomeAlimento.isEmpty() || quantCalorias.isEmpty()) {
                    Toast.makeText(this, "Digite todos os campos!", Toast.LENGTH_SHORT).show();
                } else {

                    Alimento alimento = new Alimento();
                    alimento.setNomeAlimento(nomeAlimento);
                    alimento.setCaloriaAlimento(quantCalorias);
                    alimento.setDataAlimento(dia);
                    if (alimentoDAO.salvar(alimento)) { // Traz um toast
                        adicionarCalorias(); //Adiciona as calorias em alimento novo
                        Toast.makeText(getApplicationContext(), "Salvo com sucesso :)", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao salvar :(", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

    public void adicionarCalorias(){
        String caloriasString = binding.editAddCalorias.getText().toString();
        int caloriasAtuais = prefs.getInt(chaveCaloriasDiarias, 0);
        int caloriasInt = Integer.parseInt(caloriasString) + caloriasAtuais;
        prefs.edit().putInt(chaveCaloriasDiarias, caloriasInt).apply();

    }
}