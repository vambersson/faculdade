package br.com.vambersson.portalparatodos.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

public class ListaDisciplina extends ListActivity {

    public static final String EXTRA_DISCIPLINA = "disciplina";
    public static final String EXTRA_RESULTADO = "selecionadas";
    public static final String EXTRA_ID_CURSO = "codigocurso";
    public static final String EXTRA_ID_FACULDADE = "codigofaculdade";


    private ListView listView;

    private List<Disciplina> listaDisciplinas;

    private ArrayAdapter<String> adapter;

    private String disciplinas_selecionadas = "";
    private String codigo_curso = "";
    private String codigo_faculdade = "";


    public ListaDisciplina(){

        listaDisciplinas = new ArrayList<Disciplina>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new ClasseListaDisciplinas().execute();

        codigo_curso = getIntent().getStringExtra(EXTRA_ID_CURSO);
        codigo_faculdade = getIntent().getStringExtra(EXTRA_ID_FACULDADE);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked);

        listView = getListView();
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(listView.isItemChecked(position) == true){
            if(disciplinas_selecionadas.equals("")){
                disciplinas_selecionadas =""+ listaDisciplinas.get(position).getCodigo();
            }else{
                disciplinas_selecionadas +=" "+ listaDisciplinas.get(position).getCodigo();
            }

        }


        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK){

                    Intent it = new Intent();

                    it.putExtra(EXTRA_RESULTADO,disciplinas_selecionadas);
                    setResult(RESULT_OK,it);

                    finish();
                }

                return false;
            }
        });

    }

    protected void criarLista(){


        //String disciplina = getIntent().getStringExtra(EXTRA_DISCIPLINA);

       // if(disciplina != null){
           // int position = Arrays.asList(disciplinas).indexOf(disciplina);
           // listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
           // listView.setItemChecked(position,true);
       // }

    }



    class ClasseListaDisciplinas extends AsyncTask<Disciplina, Void,String> {


        @Override
        protected String doInBackground(Disciplina... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexaao("listaDisciplinas="+Integer.parseInt(codigo_faculdade)+"="+Integer.parseInt(codigo_curso),"GET",false);

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

            if(result == ""){

                //Toast.makeText(getActivity(),"Dados invalídos", Toast.LENGTH_LONG).show();

            }else if("null".equals(result)){

                //Toast.makeText(getActivity(),"Usuário não encontrado", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(ListaDisciplina.this, "Erro de comunicação", Toast.LENGTH_SHORT).show();
                }

            }









        }

    }


}
