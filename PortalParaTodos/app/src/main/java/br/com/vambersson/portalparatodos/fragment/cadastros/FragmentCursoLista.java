package br.com.vambersson.portalparatodos.fragment.cadastros;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.fragment.adapter.CursoAdapter;
import br.com.vambersson.portalparatodos.fragment.gerenciador.GerenciadorFragment;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class FragmentCursoLista extends Fragment {

    private CursoAdapter adapter;
    private  List<Curso> listaCursos;
    private Gson gson;
    private Usuario usuario;

    private ListView lv_Cursos;
    private FloatingActionButton btn_add_Curso;


    public FragmentCursoLista(){
        gson = new Gson();
        listaCursos = new ArrayList<Curso>();
        usuario = new Usuario();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        if(usuario != null){
            listaCursos();
        }


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

        return view;
    }

    private void abrirAddCurso(){

        FragmentCursoCadastro dialog = FragmentCursoCadastro.newInstancia();
        dialog.setTargetFragment(this,1);
        dialog.abrir(getFragmentManager());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




    }

    public void listaCursos(){
        new ClasseListaCursos().execute();
    }


    class ClasseListaCursos extends AsyncTask<Usuario, Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(Usuario... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexaao("listaCursos="+usuario.getFaculdade().getCodigo(),"GET",false);

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

            //List<Curso> temp = new ArrayList<Curso>();
            Curso[] lista =  gson.fromJson(result, Curso[].class);

            listaCursos = new ArrayList<Curso>(Arrays.asList(lista) );
            adapter = new CursoAdapter(getActivity(),listaCursos);
            lv_Cursos.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }






}
