package br.com.vambersson.portalparatodos.fragment.cadastros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import br.com.vambersson.portalparatodos.activity.ListaDisciplina;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.fragment.adapter.DisciplinaAdapter;
import br.com.vambersson.portalparatodos.util.NetworkUtil;
import static java.lang.Thread.sleep;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class FragmentDisciplinaLista extends Fragment {

    public static final String EXTRA_DISCIPLINA = "disciplina";

    public static boolean consulta_disciplina = false;
    public static int curso_selecionado = 0;
    public static int faculdade_selecionada = 0;

    private ListView listView;
    private FloatingActionButton btn_add_disciplina;

    private DisciplinaAdapter adapter;

    private List<Disciplina> listaDisciplinas;

    private static FragmentDisciplinaLista instancia;

    public static FragmentDisciplinaLista getInstancia(){
        if(instancia == null){
            instancia = new FragmentDisciplinaLista();

        }
        return instancia;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            consulta_disciplina = true;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disciplina_lista_fragment,container,false);

        listView = (ListView) view.findViewById(R.id.lista_disciplina);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        btn_add_disciplina = (FloatingActionButton) view.findViewById(R.id.btn_add_disciplina);
        btn_add_disciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarDisciplina();
            }
        });


        verificaCursoSelecionado();

        return view;
    }

    private void verificaCursoSelecionado(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){

                    if(consulta_disciplina == true){
                            new ClasseListaDisciplinas().execute();
                        consulta_disciplina = false;
                    }

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        thread.start();
    }


    class ClasseListaDisciplinas extends AsyncTask<Disciplina, Void,String> {


        @Override
        protected String doInBackground(Disciplina... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("listaDisciplinas="+faculdade_selecionada+"="+curso_selecionado,"GET",false);

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

                Toast.makeText(getActivity(),R.string.message_alerta_disciplina_cadastrada, Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){

                try{

                    if(!result.equals("[]")){
                        Gson gson = new Gson();
                        Disciplina[] lista =  gson.fromJson(result, Disciplina[].class);

                        listaDisciplinas = new ArrayList<Disciplina>(Arrays.asList(lista));
                        adapter = new DisciplinaAdapter(getActivity(),listaDisciplinas);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                }catch(Exception e){
                    // Toast.makeText(ListaDisciplina.this, "Erro de comunicação"+ e, Toast.LENGTH_LONG).show();
                }

            }










        }

    }

    private void adicionarDisciplina(){

        FragmentDisciplinaCadastro dialog = FragmentDisciplinaCadastro.newInstancia();
        dialog.abrir(getFragmentManager());
        FragmentDisciplinaCadastro.curso_selecionado = curso_selecionado;
        FragmentDisciplinaCadastro.faculdade_selecionada = faculdade_selecionada;

    }























}
