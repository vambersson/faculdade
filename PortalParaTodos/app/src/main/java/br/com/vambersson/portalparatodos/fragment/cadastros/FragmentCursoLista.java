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

    private CursoAdapter adapter;
    private  List<Curso> listaCursos;
    private Gson gson;
    private Usuario usuario;
    private  Thread thread;

    private ListView lv_Cursos;
    private FloatingActionButton btn_add_Curso;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();
        listaCursos = new ArrayList<Curso>();

        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
        }else{
            usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        }

        listaCursos(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("StateUsuario",usuario);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.curso_lista_fragment,container,false);

        lv_Cursos = (ListView) view.findViewById(R.id.lv_Cursos);
        lv_Cursos.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){

                       // thread.interrupt();

                }

                return false;
            }
        });

        btn_add_Curso = (FloatingActionButton) view.findViewById(R.id.btn_add_Curso);
        btn_add_Curso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirAddCurso();

            }
        });

        return view;
    }

    private void abrirAddCurso(){

        FragmentCursoCadastro dialog = FragmentCursoCadastro.newInstancia();
        //dialog.setTargetFragment(this,1);
        dialog.abrir(getFragmentManager());

    }


    public void listaCursos(boolean executa) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){

                    try {
                        new ClasseListaCursos().execute(usuario);
                        sleep(2500);
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
                HttpURLConnection conexao = NetworkUtil.abrirConexao("listaCursos="+params[0].getFaculdade().getCodigo(),"GET",false);

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

            if(!result.equals("[]")){

                Curso[] lista =  gson.fromJson(result, Curso[].class);

                listaCursos = new ArrayList<Curso>(Arrays.asList(lista) );
                adapter = new CursoAdapter(getActivity(),listaCursos);
                lv_Cursos.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }


        }

    }


}
