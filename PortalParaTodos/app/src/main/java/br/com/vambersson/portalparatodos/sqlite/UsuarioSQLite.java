package br.com.vambersson.portalparatodos.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vambersson on 22/05/2017.
 */

public class UsuarioSQLite extends SQLiteOpenHelper{


    private static final String NOME_BANCO = "portalacademico";
    private static final int VERSAO_BANCO = 1;

    public UsuarioSQLite(Context ctx){
        super(ctx,NOME_BANCO,null,VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(" create table usuario (\n" +
                 " idcodigo integer primary key not null,\n" +
                 " foto Blob null, \n"+
                 " matricula integer not null,\n" +
                 " idfaculdade integer not null, \n" +
                 " nomefaculdade text not null, \n" +
                 " nome text not null,\n" +
                 " email text not null,\n" +
                 " tipo text not null,\n" +
                 " status text not null,\n" +
                 " statuslogin text not null,\n"+
                 " login integer not null,\n" +
                 " senha text not null );");

        db.execSQL(" create table disciplina (\n" +
                " iddisciplina integer primary key not null, \n" +
                " nome text not null);  ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists usuario");
        db.execSQL("drop table if exists disciplina");

        onCreate(db);

    }
}
