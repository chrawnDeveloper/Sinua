package com.conde.cc_contadordecalorias.ui.home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.conde.cc_contadordecalorias.ui.newAlimento.NewAlimentoActivity;
import com.conde.cc_contadordecalorias.R;
import com.conde.cc_contadordecalorias.databinding.FragmentInicioBinding;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InicioFragment extends Fragment {


    private int progress = 0;
    Button buttonIncrement;
    Button buttonDecrement;
    private ProgressBar progressBar;
    private TextView textDia, textDataHoje, textCaloriasDiarias;
    private TextView textLimite, textNdcBot, textBasal, textNdcTopo, textPorcentagem, textViewNome;
    private Button buttonNewAlimento;
    private String chaveCaloriasDiarias, chaveSexo, chaveIdade, chavePeso, chaveAltura, chaveNome,
            chaveBasal, chaveNcd, chaveAtividade, chaveDiaHoje;
    private String sexoShared, idadeShared, pesoShared, alturaShared, nomeShared, atividadeShared;
    private ImageView imageProgress;
    private String diaDeHoje;
    private SharedPreferences prefs;


    private FragmentInicioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//
//        buttonDecrement = binding.buttonDecr;
//        buttonIncrement = binding.buttonIncr;

        chaveAtividade = "chaveAtividade";
        chaveBasal = "chaveBasal";
        chaveNcd = "chaveNcd";
        chaveSexo = "chaveSexo";
        chaveIdade = "chaveIdade";
        chavePeso = "chavePeso";
        chaveAltura = "chaveAltura";
        chaveNome = "chaveNome";
        chaveDiaHoje = "chaveDiaHoje";
        textViewNome = binding.textViewNome;
        textNdcTopo = binding.textNdcTopo;
        textBasal = binding.textBasal;
        textNdcBot = binding.textNdcBot;
        textPorcentagem = binding.textPorcentagem;
        progressBar = binding.progressBar;
        textLimite = binding.textLimite;
        buttonNewAlimento = binding.buttonNewAlimento;
        textDia = binding.textDia;
        textDataHoje = binding.textDataHoje;
        chaveCaloriasDiarias = "chaveCaloriasDiarias";
        textCaloriasDiarias = binding.textCaloriasDiarias;
        imageProgress = binding.imageProgress;
        prefs = getActivity().getSharedPreferences("banco", Context.MODE_PRIVATE);


        Date dataHoje = Calendar.getInstance().getTime(); //PEGAR O DIA DE HOJE
        SimpleDateFormat dataHojeFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataHojeTexto = dataHojeFormat.format(dataHoje);
        diaDeHoje = dataHojeTexto;

        alterarNome();
        verificarData();
        calcular();
        dataDeHoje();
        diaDaSemana();
        alterarCaloriasDiarias();

        buttonNewAlimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNewAlimento();
            }
        });


                         ////// PDF //////
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
//
//        }
        String permissao[] = new String []{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        Util.validate((AppCompatActivity) getActivity(), 17, permissao);



        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {

        Date dataHoje = Calendar.getInstance().getTime(); //PEGAR O DIA DE HOJE
        SimpleDateFormat dataHojeFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataHojeTexto = dataHojeFormat.format(dataHoje);
        String diaDeHojeResume= dataHojeTexto;
        String diaHojePrefs = prefs.getString(chaveDiaHoje, "");

        if (!diaDeHojeResume.equals(diaHojePrefs)) { //Se o dia de hoje for diferente do que ta no prefs, deixa igual
            prefs.edit().putString(chaveDiaHoje, diaDeHoje).apply();
            prefs.edit().putInt(chaveCaloriasDiarias, 0).apply();
        }

        int calorias = prefs.getInt(chaveCaloriasDiarias, 0);
        String caloriasString = String.valueOf(calorias);
        textCaloriasDiarias.setText(caloriasString);

        alterarNome();
        calcular();
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inicio, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.i_pdf) {
            try {
                gerarPdf();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permissao = true;
        for (int result: grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                permissao = false;
                break;

            }
        }

        if(permissao == false){
            Toast.makeText(getContext(), "Aceite as permissões necessárias " +
                    "para o aplicativo funcionar", Toast.LENGTH_SHORT).show();
            getActivity().finish();

        }

    }

    public void gerarPdf()
            throws IOException, DocumentException {
        String nomePdf = prefs.getString(chaveNome, "");
        String alturaPdf = prefs.getString(chaveAltura, "");
        String idadePdf = prefs.getString(chaveIdade, "");
        String pesoPdf = prefs.getString(chavePeso, "");
        String ndcPdf = prefs.getString(chaveNcd, "");
        String sexoPdf = prefs.getString(chaveSexo, "");

        ParcelFileDescriptor descriptor = null;
        OutputStream outputStream;
        File pdf = null;
        Uri uri = null;



               if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                   ContentValues cv = new ContentValues();
                   cv.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                   cv.put(MediaStore.MediaColumns.DISPLAY_NAME, "DadosUsuario");
                   cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS +"/Dados/");

                   ContentResolver cr = getContext().getContentResolver();
                   uri = cr.insert(MediaStore.Downloads.getContentUri("external"), cv);

                   descriptor = cr.openFileDescriptor(uri, "rw");
                   FileDescriptor fileDescriptor = descriptor.getFileDescriptor();

                   outputStream = new FileOutputStream(fileDescriptor);

        }else{
                   File diretorioRaiz = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                   File diretorio = new File(diretorioRaiz.getPath() + "/Dados/");

                   if(!diretorio.exists()){//Se nao existir, cria
                       diretorio.mkdir();
                   }

                   String nomeArquivo = diretorio.getPath() + "/DadosUsuario" +".pdf";
                   pdf = new File(nomeArquivo);

                   outputStream = new FileOutputStream(pdf);

               }


                    //Onde começa o Documento
        Document document = new Document(PageSize.A4,10,10,10,10);
               PdfCreator pdfCreator = new PdfCreator();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);

        pdfWriter.setBoxSize("box", new Rectangle(0,0,0,0));
        pdfWriter.setPageEvent(pdfCreator);

        document.open();

        String nomedoPdf = "Nome: " + nomePdf;
        String alturadoPdf = "Altura: " + alturaPdf + " cm";
        String sexodoPdf = "Sexo: " + sexoPdf;
        String pesodoPdf = "Peso: " + pesoPdf +  " kg";
        String idadedoPdf = "Idade: " + idadePdf + " anos";
        String ndcdoPdf = "Necessidade diária de calorias: " + ndcPdf + " kcal";

        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);


        //Parágrafo1
        Paragraph paragraph = new Paragraph("Dados Gerais", font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);

        //Parágrafo2
        paragraph = new Paragraph(nomedoPdf, font2);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);

        //Parágrafo3
        paragraph = new Paragraph(idadedoPdf, font2);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);

        //Parágrafo4
        paragraph = new Paragraph(sexodoPdf, font2);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);

        //Parágrafo5
        paragraph = new Paragraph(pesodoPdf, font2);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);

        //Parágrafo5
        paragraph = new Paragraph(alturadoPdf, font2);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);

        //Parágrafo6
        paragraph = new Paragraph(ndcdoPdf, font2);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);

        document.close(); //Fecha o documento

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            outputStream.close();
            descriptor.close();
            visualizarPdfUri(uri);
        }else{
            outputStream.close();
            visualizarPdfFile(pdf);
        }


    }

    public void visualizarPdfFile(File pdf){

        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");

        List<ResolveInfo>lista = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

//        if(lista.size()<=0){
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(getContext(), "com.conde.cc_contadordecalorias.ui.home", pdf);
            intent1.setDataAndType(uri, "application/pdf");
            startActivityForResult(intent1, 1234);
//        }else{
//            Toast.makeText(getContext(), "Você não possui nenhum Leitor PDF no seu dispositivo :/", Toast.LENGTH_SHORT).show();
//        }

    }

    public void visualizarPdfUri(Uri uri){
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");

        List<ResolveInfo>lista = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

//        if(lista.size()<=0){
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent1.setDataAndType(uri, "application/pdf");

            startActivityForResult(intent1, 1234);
//        }else{
//            Toast.makeText(getContext(), "Você não possui nenhum Leitor PDF no seu dispositivo :/", Toast.LENGTH_SHORT).show();
//        }
    }

    public void alterarNome(){
        String nome = prefs.getString(chaveNome, "");
        textViewNome.setText((Html.fromHtml("Olá, " + "<b>" +
                "<font color = #730D1F>" + nome + "!" + "</b>" +  "</font>")));
    }

    public void verificarData() {
        String diaHojePrefs = prefs.getString(chaveDiaHoje, "");
        if (!diaDeHoje.equals(diaHojePrefs)) { //Se o dia de hoje for diferente do que ta no prefs, deixa igual
            prefs.edit().putString(chaveDiaHoje, diaDeHoje).apply();
            prefs.edit().putInt(chaveCaloriasDiarias, 0).apply();
        }
    }


    public void alterarCaloriasDiarias() {
        int calorias = prefs.getInt(chaveCaloriasDiarias, 0);
        String caloriasString = String.valueOf(calorias);
        textCaloriasDiarias.setText(caloriasString);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void abrirNewAlimento() {
        Intent intent = new Intent(getActivity(), NewAlimentoActivity.class);
        startActivity(intent);
    }

    public void diaDaSemana() {
        Date dataDia = Calendar.getInstance().getTime(); //PEGAR O DIA DE HOJE
        SimpleDateFormat diaFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String diaTexto = diaFormat.format(dataDia);
        String diaTexto2 = diaTexto.substring(0, 1).toUpperCase(Locale.ROOT) + diaTexto.substring(1);
        textDia.setText(diaTexto2);
    }

    public void dataDeHoje() {
        Date dataHoje = Calendar.getInstance().getTime(); //PEGAR O DIA DE HOJE
        SimpleDateFormat dataHojeFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataHojeTexto = dataHojeFormat.format(dataHoje);
        textDataHoje.setText(dataHojeTexto);
    }

    public void calcular() {
        int divisaoInt;
        int caloriasDiarias = prefs.getInt(chaveCaloriasDiarias, 0);
        double caloriasDiariasDouble = caloriasDiarias;
        double divisao;
        double idadeDouble, pesoDouble, alturaDouble;
        double ndcDouble = 0;
        double basalDouble = 0;
        double subTotalDouble = 0;
        String subTotalString = "";
        String ndcString = "";
        String basalString = "";
        String divisaoString;


        sexoShared = prefs.getString(chaveSexo, "");
        idadeShared = prefs.getString(chaveIdade, "");
        pesoShared = prefs.getString(chavePeso, "");
        alturaShared = prefs.getString(chaveAltura, "");
        atividadeShared = prefs.getString(chaveAtividade, "");

        idadeDouble = Double.parseDouble(idadeShared);
        pesoDouble = Double.parseDouble(pesoShared);
        alturaDouble = Double.parseDouble(alturaShared);

        //Nenhuma, Leve, Moderada, Avançada, Atleta

        if (sexoShared.equals("Masculino")) {
            basalDouble = (10 * pesoDouble) + (6.25 * alturaDouble) - (5 * idadeDouble);
            if (atividadeShared.equals("Nenhuma")) {
                ndcDouble = basalDouble * 1.2;
            } else if (atividadeShared.equals("Leve")) {
                ndcDouble = basalDouble * 1.375;
            } else if (atividadeShared.equals("Moderada")) {
                ndcDouble = basalDouble * 1.5;
            } else if (atividadeShared.equals("Avançada")) {
                ndcDouble = basalDouble * 1.725;
            } else if (atividadeShared.equals("Atleta")) {
                ndcDouble = basalDouble * 1.9;
            }
        } else if (sexoShared.equals("Feminino")) {
            basalDouble = (10 * pesoDouble) + (6.25 * alturaDouble) - (5 * idadeDouble) - 161;

            if (atividadeShared.equals("Nenhuma")) {
                ndcDouble = basalDouble * 1.2;
            } else if (atividadeShared.equals("Leve")) {
                ndcDouble = basalDouble * 1.375;
            } else if (atividadeShared.equals("Moderada")) {
                ndcDouble = basalDouble * 1.5;
            } else if (atividadeShared.equals("Avançada")) {
                ndcDouble = basalDouble * 1.725;
            } else if (atividadeShared.equals("Atleta")) {
                ndcDouble = basalDouble * 1.9;
            }

        }
        basalString = String.format("%.0f", basalDouble);
        ndcString = String.format("%.0f", ndcDouble);

        prefs.edit().putString(chaveNcd, ndcString).apply();

        prefs.edit().putString(chaveBasal, basalString).apply();
        prefs.edit().putString(chaveNcd, ndcString);

        textNdcTopo.setText(ndcString);
        textNdcBot.setText(ndcString);
        textBasal.setText(basalString);

        divisao = (caloriasDiariasDouble / ndcDouble) * 100;

        divisaoString = String.format("%.0f", divisao);

        //Barra de progresso
        divisaoInt = (int) divisao;

        if (divisaoInt == 0) {
            textPorcentagem.setText(divisaoString + "%");

            textPorcentagem.setText((Html.fromHtml("<font color=#D0312D>" + "<b>"
                    + divisaoString+ "%" +
                    "</b>" + " </font>")));

            textLimite.setText((Html.fromHtml("<font color=#D0312D>" + "<b>"
                    + "Você ainda não consumiu nenhuma caloria hoje." +
                    "</b>" + " </font>")));
            imageProgress.setImageResource(R.drawable.ic_none);
            progressBar.setProgress(0);

        } else if (divisaoInt >= 100) {
            textPorcentagem.setText((Html.fromHtml("<font color=#228C22>" + "<b>"
                    + "100%" +
                    "</b>" + " </font>")));
            textLimite.setText((Html.fromHtml("<font color=#228C22>" + "<b>"
                    + "Parabéns!!! Você já consumiu todas as calorias diárias :)" +
                    "</b>" + " </font>")));
            imageProgress.setImageResource(R.drawable.ic_complete);
            progressBar.setProgress(100);
//            textPorcentagem.setText(divisaoString + "%");
        } else if (divisaoInt > 0 && divisaoInt <= 30) {
            subTotalDouble = ndcDouble - caloriasDiariasDouble;
            subTotalString = String.format("%.0f", subTotalDouble);
            textPorcentagem.setText(divisaoString + "%");
            textLimite.setText((Html.fromHtml("Você ainda precisa consumir " + "<b>" +
                    subTotalString + "</b>" + " calorias.")));
            imageProgress.setImageResource(R.drawable.ic_low_battery);
            progressBar.setProgress(divisaoInt);
        } else if (divisaoInt > 30 && divisaoInt <= 50) {
            subTotalDouble = ndcDouble - caloriasDiariasDouble;
            subTotalString = String.format("%.0f", subTotalDouble);
            textPorcentagem.setText(divisaoString + "%");
            textLimite.setText((Html.fromHtml("Você precisa se alimentar com " + "<b>" +
                    subTotalString + "</b>" + " calorias.")));
            imageProgress.setImageResource(R.drawable.ic_battery);
            progressBar.setProgress(divisaoInt);
        } else if (divisaoInt > 50 && divisaoInt < 100) {
            subTotalDouble = ndcDouble - caloriasDiariasDouble;
            subTotalString = String.format("%.0f", subTotalDouble);
            textPorcentagem.setText(divisaoString + "%");
            textLimite.setText((Html.fromHtml("Você só precisa consumir mais " + "<b>" +
                    subTotalString + "</b>" + " calorias.")));
            imageProgress.setImageResource(R.drawable.ic_high_battery);
            progressBar.setProgress(divisaoInt);
        }


    }

}
