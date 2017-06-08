package br.com.vambersson.portalparatodos.aula.disciplina;


import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 04/06/2017.
 */

public class ListaDisciplinasUsuario extends ListActivity {

    public static final String EXTRA_DISCIPLINA_TROCA = "troca";
    public static final String EXTRA_RESULTADO = "selecionada";
    public static final String EXTRA_USUARIO = "usuario";

    private Usuario usuario;
    private String codigoDisciplinaTroca = "";

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Disciplina> listaDisciplinas;

    private int totaldisciplinaMarcada = 0;
    private Intent it = new Intent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = new Usuario();
        listaDisciplinas = new ArrayList<Disciplina>();

        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
        }else{
            usuario = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
            codigoDisciplinaTroca = getIntent().getStringExtra(EXTRA_DISCIPLINA_TROCA);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice);

        listView = getListView();
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        new ClasseListaDisciplinas().execute(usuario);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("StateUsuario",usuario);
    }

    private void verificaSelecao(){


//      Verifica total de seleção das disciplinas
        for(int i=0;i < listView.getCount();i++){
            if(listView.isItemChecked(i) == true){
                totaldisciplinaMarcada ++;
            }
        }

        if(totaldisciplinaMarcada > 1){
            it.putExtra("tipo","update0");
            Snackbar.make(getListView(),"Selecionar apenas uma disciplina!" , Snackbar.LENGTH_LONG).setAction("Action", null).show();
            totaldisciplinaMarcada = 0;
        }else if(totaldisciplinaMarcada == 0){
            //              DELETE DISCIPLINA ou não fez nada


            for(int i=0;i < listView.getCount();i++){

                if(codigoDisciplinaTroca != null){
                    //              DELETE DISCIPLINA

                    it.putExtra(EXTRA_RESULTADO,listaDisciplinas.get(i));
                    it.putExtra("tipo","delete");
                    Snackbar.make(getListView(), "Delete", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }else if(codigoDisciplinaTroca == null){
                    it.putExtra("tipo","update0");
                    Snackbar.make(getListView(), "Não selecionou nada...", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }

            }

            totaldisciplinaMarcada = 0;
        }else if(totaldisciplinaMarcada == 1){

            for(int i=0;i < listView.getCount();i++){

                if(listView.isItemChecked(i) == true && codigoDisciplinaTroca == null){
                    //              INSERT DISCIPLINA

                    it.putExtra(EXTRA_RESULTADO,listaDisciplinas.get(i));
                    it.putExtra("tipo","insert");
                    totaldisciplinaMarcada = 0;
                    Snackbar.make(getListView(), "Insert", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else  if(listView.isItemChecked(i) == true && codigoDisciplinaTroca != null && !listaDisciplinas.get(i).getCodigo().toString().equals(codigoDisciplinaTroca) ){
                    //              UPDATE DISCIPLINA

                    it.putExtra(EXTRA_RESULTADO,listaDisciplinas.get(i));
                    it.putExtra("tipo","update");
                    totaldisciplinaMarcada = 0;
                    Snackbar.make(getListView(), "Update", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else  if(listView.isItemChecked(i) == true && codigoDisciplinaTroca != null && listaDisciplinas.get(i).getCodigo().toString().equals(codigoDisciplinaTroca) ){
                    //             NÃO FEZ ALTERAÇÃO DISCIPLINA
                    it.putExtra("tipo","update0");
                    totaldisciplinaMarcada = 0;
                    Snackbar.make(getListView(), "Não fez alterações!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }

            totaldisciplinaMarcada = 0;
        }

        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK){
                    setResult(RESULT_OK,it);
                    finish();
                }
                return false;
            }
        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        verificaSelecao();

    }

    class ClasseListaDisciplinas extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {

            Gson gson = new Gson();
            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("disciplinasUsuario="+params[0].getFaculdade().getCodigo() +"=" +params[0].getCodigo(),"GET",false);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(is);
                    conexao.disconnect();
                }

                return obj;

            } catch (Exception e) {
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if("[]".equals(result)){

                //Toast.makeText(ListaDisciplinasUsuario.this ,getResources().getString(R.string.message_alerta_disciplina_cadastrada), Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){
                Gson gson = new Gson();
                try{

                    Disciplina[] lista =  gson.fromJson(result, Disciplina[].class);

                    List<Disciplina> temp = new ArrayList<Disciplina>(Arrays.asList(lista) );
                    listaDisciplinas.clear();
                    listaDisciplinas = temp;
                    adapter.clear();
                    for (int i = 0;i < temp.size();i++){

                        adapter.add(temp.get(i).getPeriodo() +" "+ temp.get(i).getNome());
                        adapter.notifyDataSetChanged();

                        if(codigoDisciplinaTroca != null){

                            if(Integer.parseInt(codigoDisciplinaTroca) == temp.get(i).getCodigo()){
                                listView.setItemChecked(i,true);
                            }

                        }

                    }

                }catch(Exception e){
                    Toast.makeText(ListaDisciplinasUsuario.this, R.string.message_alerta_webservice, Toast.LENGTH_SHORT).show();
                }

            }

        }

    }



}
