package br.com.vambersson.portalparatodos.fragment.cadastros;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.fragment.adapter.CursoAdapter;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static java.lang.Thread.sleep;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class FragmentCursoLista extends Fragment {


    public static boolean consulta_curso = false;

    private CursoAdapter adapter;
    private  ArrayList<Curso> listaCursos;

    private Usuario usuario;
    private Thread thread ;

    private ListView lv_Cursos;
    private FloatingActionButton btn_add_Curso;

    private static FragmentCursoLista instancia;

    public static  FragmentCursoLista getInstancia(){
        if(instancia == null){
            instancia = new FragmentCursoLista();
        }
        return instancia;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.curso_lista_fragment,container,false);
        lv_Cursos = (ListView) view.findViewById(R.id.lv_Cursos);

        btn_add_Curso = (FloatingActionButton) view.findViewById(R.id.btn_add_Curso);
        btn_add_Curso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirAddCurso();

            }
        });

        adapter = new CursoAdapter(getActivity());

        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");

            listaCursos = (ArrayList<Curso>) savedInstanceState.getSerializable("StateListaCurso");

            if(listaCursos != null){
                carregarLista(listaCursos);
            }else {
                listaCursos();
            }


        }else{

            if(getActivity().getIntent().getSerializableExtra("usuario") != null){
                usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");

            }else if(getActivity().getIntent().getSerializableExtra("usuarioAlterar") != null ){
                usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuarioAlterar");
            }

            //usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
            consulta_curso = true;
        }

        listaCursos();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("StateUsuario",usuario);
        outState.putSerializable("StateListaCurso",listaCursos);

    }


    private void carregarLista(List<Curso> lista){

        adapter.setLista(lista);

        lv_Cursos.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    private void abrirAddCurso(){

        FragmentCursoCadastro dialog = FragmentCursoCadastro.newInstancia();
        dialog.abrir(getFragmentManager());

    }


    public void listaCursos() {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){

                    if(consulta_curso == true){
                        new ClasseListaCursos().execute();
                        consulta_curso = false;
                    }

                    try {

                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }

    class ClasseListaCursos extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("listaCursos="+usuario.getFaculdade().getCodigo(),"GET",false);

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

            try{

                if(!result.equals("[]")){

                    Gson gson = new Gson();
                    Curso[] lista =  gson.fromJson(result, Curso[].class);

                    listaCursos = new ArrayList<Curso>(Arrays.asList(lista) );

                    carregarLista(listaCursos);

                }

            }catch (Exception e){
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.message_alerta_webservice), Toast.LENGTH_LONG).show();

            }




        }

    }




}
