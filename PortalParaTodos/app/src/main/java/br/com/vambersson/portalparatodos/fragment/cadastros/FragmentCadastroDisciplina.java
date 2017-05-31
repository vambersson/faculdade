package br.com.vambersson.portalparatodos.fragment.cadastros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by Vambersson on 21/05/2017.
 */

public class FragmentCadastroDisciplina extends Fragment {

    public static final String EXTRA_DISCIPLINA = "disciplina";
    public static final String EXTRA_RESULTADO = "selecionadas";
    public static final String EXTRA_ID_CURSO = "codigocurso";
    public static final String EXTRA_ID_FACULDADE = "codigofaculdade";



    private ListView listView;
    private DisciplinaAdapter adapter;

    private List<Disciplina> listaDisciplinas;
    private String disciplinas_selecionadas = "";
    private String codigo_curso = "";
    private String codigo_faculdade = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disciplina_lista_fragment,container,false);

        listView = (ListView) view.findViewById(R.id.lista_disciplina);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        codigo_curso = "6";
        codigo_faculdade = "2";
        new ClasseListaDisciplinas().execute();

        return view;
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

                Toast.makeText(getActivity(),getResources().getString(R.string.message_alerta_disciplina_cadastrada), Toast.LENGTH_LONG).show();

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

                    String selecionadas = getActivity().getIntent().getStringExtra(EXTRA_DISCIPLINA);

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
