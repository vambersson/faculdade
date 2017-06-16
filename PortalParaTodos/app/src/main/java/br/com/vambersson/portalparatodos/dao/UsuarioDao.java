package br.com.vambersson.portalparatodos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.sqlite.UsuarioSQLite;

/**
 * Created by Vambersson on 22/05/2017.
 */

public class UsuarioDao {

    private UsuarioSQLite usuarioSQLite;

    public UsuarioDao(Context ctx){
        usuarioSQLite = new UsuarioSQLite(ctx);
    }


    public void inserir(Usuario usuario,String statuslogin){

        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("idcodigo",usuario.getCodigo());
        cv.put("foto",usuario.getFoto());
        cv.put("matricula",usuario.getMatricula());
        cv.put("idfaculdade",usuario.getFaculdade().getCodigo());
        cv.put("nomefaculdade",usuario.getFaculdade().getNome());
        cv.put("nome",usuario.getNome());
        cv.put("email",usuario.getEmail());
        cv.put("tipo",usuario.getTipo());
        cv.put("status",usuario.getStatus());
        cv.put("statuslogin",statuslogin);
        cv.put("login",usuario.getLogin());
        cv.put("senha",usuario.getSenha());

        db.insert("usuario",null,cv);

    }

    public void inserirDisciplinas(Disciplina dis){

        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("iddisciplina",dis.getCodigo());
        cv.put("nome",dis.getNome());
        cv.put("curso",dis.getCurso().getCodigo());
        cv.put("selecionou",dis.getSelecionou());
        cv.put("dia",dis.getNumerodia());
        cv.put("ordem",dis.getOrdem());

        db.insert("disciplina",null,cv);

    }


    public void deletarDisciplinas(int dia){
        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();
        db.delete("disciplina"," dia = "+dia+" ",null);
    }
    public void deletarDisciplinas(){
        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();
        db.delete("disciplina",null,null);
    }

    public void deletarDisciplinas(Disciplina dis){
        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();
        db.delete("disciplina","iddisciplina = "+dis.getCodigo()+" and dia = "+dis.getNumerodia()+" and ordem = "+dis.getOrdem()+" ",null);
    }

    public List<Disciplina>  getDisciplinas(int dia){

        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();
        List<Disciplina> lista = new ArrayList<Disciplina>();
        Disciplina dis = null;

        String[] colunas = new String[]{"iddisciplina","nome","curso","selecionou","dia","ordem"};
        Cursor cursor = db.query("disciplina",colunas,"dia = "+dia+" ",null,null,null,null );

       while(cursor.moveToNext()){
            dis = new Disciplina();

            dis.setCodigo(cursor.getInt(0));
            dis.setNome(cursor.getString(1));
            dis.getCurso().setCodigo(cursor.getInt(2));
            dis.setSelecionou(cursor.getString(3));
            dis.setNumerodia(cursor.getInt(4));
            dis.setOrdem(cursor.getInt(5));

            lista.add(dis);
        }

        return lista;
    }


    public void atualizar(Usuario usuario){

        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("nome",usuario.getNome());
        cv.put("email",usuario.getEmail());
        cv.put("senha",usuario.getSenha());

        db.update("usuario",cv,"matricula = ?",new String[]{ String.valueOf(usuario.getMatricula() )});
    }

    public void atualizarFoto(Usuario usuario){

        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("foto",usuario.getFoto());

        db.update("usuario",cv,"matricula = ?",new String[]{ String.valueOf(usuario.getMatricula() )});
    }

    public void deletar(){
        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();
        db.delete("usuario",null,null);
    }


    public Usuario getUsuario(){

        SQLiteDatabase db = usuarioSQLite.getWritableDatabase();
        Usuario usuario = null;

        String[] colunas = new String[]{"idfaculdade","nomefaculdade","matricula","nome","email","tipo","statuslogin","login","senha","foto","idcodigo"};
         Cursor cursor = db.query("usuario",colunas,null,null,null,null,null);

        if(cursor.moveToNext()){
            usuario = new Usuario();


            usuario.getFaculdade().setCodigo(cursor.getInt(0));
            usuario.getFaculdade().setNome(cursor.getString(1));
            usuario.setMatricula(cursor.getInt(2));
            usuario.setNome(cursor.getString(3));
            usuario.setEmail(cursor.getString(4));
            usuario.setTipo(cursor.getString(5));
            usuario.setStatuslogin(cursor.getString(6));
            usuario.setLogin(cursor.getInt(7));
            usuario.setSenha(cursor.getString(8));
            usuario.setFoto(cursor.getBlob(9));
            usuario.setCodigo(cursor.getInt(10));


        }

        return usuario;
    }



}
