package br.com.nglauber.aula03sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.nglauber.aula03sqlite.model.Carro;

public class CarroRepositorio {
    CarroDbHelper mHelper;

    public CarroRepositorio(Context ctx) {
        mHelper = new CarroDbHelper(ctx);
    }

    public void salvar(Carro carro){
        if (carro.getId() == 0){
            inserir(carro);
        } else {
            atualizar(carro);
        }
    }

    private void atualizar(Carro carro) {
        ContentValues cv = getValuesFromCarro(carro);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = db.update(CarroContract.TABELA,
                cv,
                CarroContract._ID +" = ?",
                new String[]{ String.valueOf(carro.getId())});
        db.close();

        if (result <= 0) throw new RuntimeException("Erro ao atualizar!");
    }

    private void inserir(Carro carro) {
        ContentValues cv = getValuesFromCarro(carro);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(CarroContract.TABELA, null, cv);
        if (id <= 0) throw new RuntimeException("Erro ao inserir!");

        carro.setId(id);
        db.close();
    }

    public void excluir(Carro carro){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(CarroContract.TABELA,
                CarroContract._ID +" = ?",
                new String[]{ String.valueOf(carro.getId())});
        db.close();
    }

    public List<Carro> listar(){
        List<Carro> carros = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM "+ CarroContract.TABELA +
                        " ORDER BY "+ CarroContract.MODELO, null);

        if (cursor.getCount() > 0) {
            int idxId         = cursor.getColumnIndex(CarroContract._ID);
            int idxModelo     = cursor.getColumnIndex(CarroContract.MODELO);
            int idxFabricante = cursor.getColumnIndex(CarroContract.FABRICANTE);
            int idxAno        = cursor.getColumnIndex(CarroContract.ANO);
            int idxFoto       = cursor.getColumnIndex(CarroContract.FOTO);
            while (cursor.moveToNext()){
                Carro carro = new Carro();
                carro.setId(cursor.getLong(idxId));
                carro.setModelo(cursor.getString(idxModelo));
                carro.setFabricante(cursor.getInt(idxFabricante));
                carro.setAno(cursor.getInt(idxAno));
                carro.setFoto(cursor.getString(idxFoto));
                carros.add(carro);
            }
        }
        cursor.close();

        db.close();

        return carros;
    }

    private ContentValues getValuesFromCarro(Carro carro){
        ContentValues cv = new ContentValues();
        cv.put(CarroContract.MODELO, carro.getModelo());
        cv.put(CarroContract.FABRICANTE, carro.getFabricante());
        cv.put(CarroContract.ANO, carro.getAno());
        cv.put(CarroContract.FOTO, carro.getFoto());
        return  cv;
    }
}
