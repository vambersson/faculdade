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
            new ClasseListaCursos().execute();
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


        lv_Cursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "lista", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void abrirAddCurso(){

        FragmentCursoCadastro fragment = FragmentCursoCadastro.newInstancia();
        fragment.abrir(getFragmentManager());
    }

    private void startFragment(String CodigoFragment){
        Intent it = new Intent(getActivity(), GerenciadorFragment.class);
        it.putExtra("CodigoFragment",CodigoFragment);
        startActivity(it);
    }


    class ClasseListaCursos extends AsyncTask<Usuario, Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(Usuario... params) {

            HttpURLConnection conexao = null;
            String obj = "";

            try {

                conexao = NetworkUtil.conectar("GET","listaCursos="+usuario.getFaculdade().getCodigo());

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.converterInputStreamToString(is);
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

        }

    }




}
