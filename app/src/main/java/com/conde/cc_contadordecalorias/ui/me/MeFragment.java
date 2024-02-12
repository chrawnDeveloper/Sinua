package com.conde.cc_contadordecalorias.ui.me;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.conde.cc_contadordecalorias.R;
import com.conde.cc_contadordecalorias.databinding.FragmentMeBinding;

public class MeFragment extends Fragment {

    private FragmentMeBinding binding;
    private String chaveSexo, chaveIdade, chavePeso, chaveAltura, chaveNome, chaveNcd, chaveAtividade,
            sexo, idade, peso, altura, nome, atividade, ncd;
    private TextView textPeso, textAltura, textIdade, textGenero, textNcd, textAtividade, textPrimeiroNome, textNome;
    private SharedPreferences prefs;
    private ImageView imageGenero;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        textPeso = binding.textPeso;
        textAltura = binding.textAltura;
        textIdade = binding.textIdade;
        textGenero = binding.textGenero;
        textNome = binding.textWelcome;
        textNcd = binding.textNcd;
        textAtividade = binding.textAtividade;
        imageGenero = binding.imageGenero;
        textPrimeiroNome = binding.textPrimeiroNome;
        prefs = getActivity().getSharedPreferences("banco", Context.MODE_PRIVATE);
        chaveSexo = "chaveSexo";
        chaveIdade = "chaveIdade";
        chavePeso = "chavePeso";
        chaveAltura = "chaveAltura";
        chaveNome = "chaveNome";
        chaveNcd = "chaveNcd";
        chaveAtividade = "chaveAtividade";
        sexo = prefs.getString(chaveSexo, "");
        idade = prefs.getString(chaveIdade, "");
        peso = prefs.getString(chavePeso, "");
        altura = prefs.getString(chaveAltura, "");
        nome = prefs.getString(chaveNome, "");
        atividade = prefs.getString(chaveAtividade, "");
        ncd = prefs.getString(chaveNcd, "");

        textPeso.setText(peso);
        textIdade.setText(idade);
        textAltura.setText(altura);
        textAtividade.setText(atividade);
        textNcd.setText(ncd);
        textPrimeiroNome.setText((Html.fromHtml("Olá " + "<b>" + nome + "</b>" + "!")));
        textGenero.setText(sexo);

        calcular();
        verifyNcd();
        verifyGender();
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        calcular();
        verifyNcd();
        String idadeResume = prefs.getString(chaveIdade, "");
        String pesoResume = prefs.getString(chavePeso, "");
        String alturaResume = prefs.getString(chaveAltura, "");
        String sexoResume = prefs.getString(chaveSexo, "");
        String nomeResume = prefs.getString(chaveNome, "");
        String atividadeResume = prefs.getString(chaveAtividade, "");

        textPrimeiroNome.setText((Html.fromHtml("Olá " + "<b>" + nomeResume + "</b>" + "!")));
        textPeso.setText(pesoResume);
        textIdade.setText(idadeResume);
        textAltura.setText(alturaResume);
        textAtividade.setText(atividadeResume);
        textGenero.setText(sexoResume);

        if (sexoResume.equals("Masculino")) {
            imageGenero.setImageResource(R.drawable.ic_baseline_male_24);
        } else if (sexoResume.equals("Feminino")) {
            imageGenero.setImageResource(R.drawable.ic_baseline_female_24);
        }
    }

    public void calcular(){
        String sexoCalcular, idadeCalcular, pesoCalcular, alturaCalcular, atividadeCalcular;
        double idadeDouble = 0;
        double pesoDouble =0;
        double alturaDouble = 0;
        double ndcDouble = 0;
        double basalDouble = 0;
        String ndcString = "";



        sexoCalcular = prefs.getString(chaveSexo, "");
        idadeCalcular = prefs.getString(chaveIdade, "");
        pesoCalcular = prefs.getString(chavePeso, "");
        alturaCalcular = prefs.getString(chaveAltura, "");
        atividadeCalcular = prefs.getString(chaveAtividade, "");

        idadeDouble = Double.parseDouble(idadeCalcular);
        pesoDouble = Double.parseDouble(pesoCalcular);
        alturaDouble = Double.parseDouble(alturaCalcular);

        //Nenhuma, Leve, Moderada, Avançada, Atleta

        if (sexoCalcular.equals("Masculino")) {
            basalDouble = (10 * pesoDouble) + (6.25 * alturaDouble) - (5 * idadeDouble);
            if (atividadeCalcular.equals("Nenhuma")) {
                ndcDouble = basalDouble * 1.2;
            } else if (atividadeCalcular.equals("Leve")) {
                ndcDouble = basalDouble * 1.375;
            } else if (atividadeCalcular.equals("Moderada")) {
                ndcDouble = basalDouble * 1.5;
            } else if (atividadeCalcular.equals("Avançada")) {
                ndcDouble = basalDouble * 1.725;
            } else if (atividadeCalcular.equals("Atleta")) {
                ndcDouble = basalDouble * 1.9;
            }
        } else if (sexoCalcular.equals("Feminino")) {
            basalDouble = (10 * pesoDouble) + (6.25 * alturaDouble) - (5 * idadeDouble) - 161;

            if (atividadeCalcular.equals("Nenhuma")) {
                ndcDouble = basalDouble * 1.2;
            } else if (atividadeCalcular.equals("Leve")) {
                ndcDouble = basalDouble * 1.375;
            } else if (atividadeCalcular.equals("Moderada")) {
                ndcDouble = basalDouble * 1.5;
            } else if (atividadeCalcular.equals("Avançada")) {
                ndcDouble = basalDouble * 1.725;
            } else if (atividadeCalcular.equals("Atleta")) {
                ndcDouble = basalDouble * 1.9;
            }

        }
        ndcString = String.format("%.0f", ndcDouble);

        prefs.edit().putString(chaveNcd, ndcString).apply();

    }

    public void verifyNcd(){
        String ncdResume = prefs.getString(chaveNcd, "");
        textNcd.setText(ncdResume);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_me, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.i_editar) {
            editInfo();
        } else if (item.getItemId() == R.id.i_editar_fisica) {
            editInfoFisica();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void verifyGender() {
        if (sexo.equals("Masculino")) {
            imageGenero.setImageResource(R.drawable.ic_baseline_male_24);
        } else if (sexo.equals("Feminino")) {
            imageGenero.setImageResource(R.drawable.ic_baseline_female_24);
        }
    }

    public void editInfo() {
        Intent intent = new Intent(getActivity(), EditActivity.class);
        startActivity(intent);
//        getActivity().finish();
    }

    public void editInfoFisica() {
        Intent intent = new Intent(getActivity(), EditFisicActivity.class);
        startActivity(intent);
    }
}