package br.com.vambersson.portalparatodos.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

        codigo_curso = getIntent().getStringExtra(EXTRA_ID_CURSO);
        codigo_faculdade = getIntent().getStringExtra(EXTRA_ID_FACULDADE);



        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked);

        listView = getListView();
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        new ClasseListaDisciplinas().execute();










    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);




        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK){

                    for(int i =0; i < listView.getCount();i++){

                        if(listView.isItemChecked(i) == true){
                            if(disciplinas_selecionadas.equals("")){
                                disciplinas_selecionadas =""+ listaDisciplinas.get(i).getCodigo();
                            }else{
                                disciplinas_selecionadas +=" "+ listaDisciplinas.get(i).getCodigo();
                            }

                        }

                    }


                    Intent it = new Intent();
                    it.putExtra("codigoCursoSelecionado",codigo_curso);
                    it.putExtra(EXTRA_RESULTADO,disciplinas_selecionadas);
                    setResult(RESULT_OK,it);

                    finish();
                }

                return false;
            }
        });

    }


    class ClasseListaDisciplinas extends AsyncTask<Disciplina, Void,String> {


        @Override
        protected String doInBackground(Disciplina... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("listaDisciplinas="+Integer.parseInt(codigo_faculdade)+"="+Integer.parseInt(codigo_curso),"GET",false);

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

                //Toast.makeText(ListaDisciplina.this,"ok1", Toast.LENGTH_LONG).show();

            }else if("[]".equals(result)){

                Toast.makeText(ListaDisciplina.this,getResources().getString(R.string.message_alerta_disciplina_cadastrada), Toast.LENGTH_LONG).show();

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

                    String selecionadas = getIntent().getStringExtra(EXTRA_DISCIPLINA);

                    if(selecionadas != ""){

                        String[] idcodigos = selecionadas.split(" ");

                        for(int i=0; i < listView.getCount(); i++){

                            for(int b= 0;b < idcodigos.length; b++ ){

                                if(listaDisciplinas.get(i).getCodigo() == Integer.parseInt(idcodigos[b])){
                                    listView.setItemChecked(i,true);
                                }

                            }

                        }

                    }

                }catch(Exception e){
                   // Toast.makeText(ListaDisciplina.this, "Erro de comunicação"+ e, Toast.LENGTH_LONG).show();
                }

            }









        }

    }


}
