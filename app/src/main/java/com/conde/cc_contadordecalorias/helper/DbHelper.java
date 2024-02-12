package com.conde.cc_contadordecalorias.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static int VERSION = 1;
    public static String NOME_DB = "DB_ALIMENTO";
    public static String TABELA_ALIMENTO = "alimento";

    public DbHelper(Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_ALIMENTO + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nomealimento TEXT NOT NULL, caloriaalimento TEXT NOT NULL, data TEXT NOT NULL ); ";

        try{
            db.execSQL(sql);
            Log.i("INFO DB", "Sucesso ao criar tabela");
        }

        catch (Exception e){
            Log.i("INFO DB", "Erro ao criar tabela" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
