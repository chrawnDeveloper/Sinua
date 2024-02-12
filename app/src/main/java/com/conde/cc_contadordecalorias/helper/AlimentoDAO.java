package com.conde.cc_contadordecalorias.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.conde.cc_contadordecalorias.ui.newAlimento.Alimento;

import java.util.ArrayList;
import java.util.List;

public class AlimentoDAO implements IAlimentoDAO {

    private SQLiteDatabase escrever;
    private SQLiteDatabase ler;

    public AlimentoDAO(Context context) {
        DbHelper db = new DbHelper(context);
        escrever = db.getWritableDatabase();
        ler = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Alimento alimento) {

        ContentValues cv = new ContentValues();
        String nomealimento = "nomealimento";
        String caloriaalimento = "caloriaalimento";
        String dataalimento = "data";

        cv.put(nomealimento, alimento.getNomeAlimento());
        cv.put(caloriaalimento, alimento.getCaloriaAlimento());
        cv.put(dataalimento, alimento.getDataAlimento());

        try {
            escrever.insert(DbHelper.TABELA_ALIMENTO, null, cv);
            Log.e("INFO", "Sucesso ao salvar tarefa");
        } catch (Exception e) {
            Log.e("INFO", "Erro ao salvar tarefa" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Alimento alimento) {
        ContentValues cv = new ContentValues();
        String nomealimento = "nomealimento";
        String caloriaalimento = "caloriaalimento";
        String dataalimento = "data";

        cv.put(nomealimento, alimento.getNomeAlimento());
        cv.put(caloriaalimento, alimento.getCaloriaAlimento());
        cv.put(dataalimento, alimento.getDataAlimento());

        try {
            String[] args = {alimento.getId().toString()};
            escrever.update(DbHelper.TABELA_ALIMENTO, cv, "id=?", args);
            Log.e("INFO", "Sucesso ao atualizar tarefa");
        } catch (Exception e) {
            Log.e("INFO", "Erro ao atualizar tarefa" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deletar(Alimento alimento) {
        String[] args = {alimento.getId().toString()};
        try {
            escrever.delete(DbHelper.TABELA_ALIMENTO, "id=?", args);
            Log.e("INFO", "Sucesso ao remover tarefa");
        } catch (Exception e) {
            Log.e("INFO", "Erro ao remover tarefa" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deletarTudo() {
        try {
            escrever.delete(DbHelper.TABELA_ALIMENTO, null, null);
            Log.e("INFO", "Sucesso ao apagar tudo");
        } catch (Exception e) {
            Log.e("INFO", "Erro ao apagar tudo" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<Alimento> listar() {
        List<Alimento> listaAlimento = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_ALIMENTO + " ;";
        Cursor cursor = ler.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            Alimento alimento = new Alimento();

            @SuppressLint("Range") Long id = cursor.getLong(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String nomeAlimento = cursor.getString(cursor.getColumnIndex("nomealimento"));
            @SuppressLint("Range") String caloriaAlimento = cursor.getString(cursor.getColumnIndex("caloriaalimento"));
            @SuppressLint("Range") String dataAlimento = cursor.getString(cursor.getColumnIndex("data"));

            alimento.setId(id);
            alimento.setDataAlimento(dataAlimento);
            alimento.setNomeAlimento(nomeAlimento);
            alimento.setCaloriaAlimento(caloriaAlimento);
            listaAlimento.add(alimento);
        }

        return listaAlimento;
    }
}
