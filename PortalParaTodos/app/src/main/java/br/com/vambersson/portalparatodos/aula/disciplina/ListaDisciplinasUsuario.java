package br.com.vambersson.portalparatodos.aula.disciplina;


import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import br.com.vambersson.portalparatodos.aula.horario.HorarioAula;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 04/06/2017.
 */

public class ListaDisciplinasUsuario extends ListActivity {

    public static final String EXTRA_RESULTADO = "selecionada";
    public static final String EXTRA_USUARIO = "usuario";

    private String disciplina_selecionada="";

    private Usuario usuario;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Disciplina> listaDisciplinas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = new Usuario();
        listaDisciplinas = new ArrayList<Disciplina>();

        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
        }else{
            usuario = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice);

        listView = getListView();
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        new ClasseListaDisciplinas().execute(usuario);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("StateUsuario",usuario);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent it = new Intent();
        it.putExtra(EXTRA_RESULTADO,listaDisciplinas.get(position));
        setResult(RESULT_OK,it);
        finish();



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

                Toast.makeText(ListaDisciplinasUsuario.this ,getResources().getString(R.string.message_alerta_disciplina_cadastrada), Toast.LENGTH_LONG).show();

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
                    }
                    adapter.notifyDataSetChanged();

                }catch(Exception e){
                    Toast.makeText(ListaDisciplinasUsuario.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }









        }

    }



}
