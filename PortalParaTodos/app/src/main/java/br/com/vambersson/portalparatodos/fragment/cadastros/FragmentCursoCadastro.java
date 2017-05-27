package br.com.vambersson.portalparatodos.fragment.cadastros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.erro.ConexaoException;
import br.com.vambersson.portalparatodos.fragment.adapter.CursoAdapter;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 23/05/2017.
 */

public class FragmentCursoCadastro extends DialogFragment {

    private static final String DIALOG_TAG = "addcurso";

    private Gson gson = new Gson();
    private Usuario usuario;
    Curso curso = new Curso();


    private EditText edt_IdNome_Curso;
    private Button btn_IdCancel_Curso;
    private Button btn_IdSave_Curso;


    public static FragmentCursoCadastro newInstancia(){
        FragmentCursoCadastro fragemnt = new FragmentCursoCadastro();
        return fragemnt;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        if(usuario != null){
            curso.getFaculdade().setCodigo(usuario.getFaculdade().getCodigo());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.curso_cadastro_fragment,container,false);

        edt_IdNome_Curso = (EditText) view.findViewById(R.id.edt_IdNome_Curso);
        btn_IdCancel_Curso = (Button) view.findViewById(R.id.btn_IdCancel_Curso);
        btn_IdCancel_Curso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_IdSave_Curso = (Button) view.findViewById(R.id.btn_IdSave_Curso);
        btn_IdSave_Curso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurso();
            }
        });

        return view;
    }

    private void saveCurso(){

        if(validaDados() == true){

            curso.setNome(edt_IdNome_Curso.getText().toString().trim());

            new ClasseCadastroCursos().execute(curso);


        }

    }

    private boolean validaDados(){

        boolean resultado = true;

        if(edt_IdNome_Curso.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "Nome não informado", Toast.LENGTH_SHORT).show();
            resultado = false;
        }
        return resultado;

    }

    class ClasseCadastroCursos extends AsyncTask<Curso, Void,String> {

        @Override
        protected String doInBackground(Curso... params) {
            String obj ="";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexaao("cadastroCurso","POST",true);

                OutputStream out = conexao.getOutputStream();

                out.write(gson.toJson(params[0]).getBytes());
                out.flush();
                out.close();

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(is);
                    conexao.disconnect();
                }

                return obj;

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            getDialog().dismiss();

        }
    }

    public void abrir(FragmentManager fm){
        show(fm,DIALOG_TAG);
    }


}
