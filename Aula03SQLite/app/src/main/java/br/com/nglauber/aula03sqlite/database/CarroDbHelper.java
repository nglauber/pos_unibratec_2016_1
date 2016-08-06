package br.com.nglauber.aula03sqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarroDbHelper extends SQLiteOpenHelper {
    public static final int VERSAO_DB = 2;
    public static final String NOME_DB = "dbCarros";

    public CarroDbHelper(Context context) {
        super(context, NOME_DB, null, VERSAO_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+ CarroContract.TABELA +" (" +
                        CarroContract._ID        +" integer primary key autoincrement, " +
                        CarroContract.MODELO     +" text not null," +
                        CarroContract.FABRICANTE +" text not null, " +
                        CarroContract.FOTO       +" text null, " +
                        CarroContract.ANO        +" integer not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2){
            db.execSQL("ALTER TABLE "+ CarroContract.TABELA +
                    " ADD COLUMN "+ CarroContract.FOTO +" text null");
        }
    }
}
