package br.com.vambersson.portalparatodos.fragment.cadastros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 03/06/2017.
 */

public class FragmentDisciplinaCadastro extends DialogFragment {

    private static final String DIALOG_TAG = "adddisciplina";
    public static int curso_selecionado = 0;
    public static int faculdade_selecionada = 0;

    private Spinner spinner_Idperiodo;
    private ArrayAdapter<String> adapter;

    private EditText edt_nome_disciplina;
    private Button btn_IdCancel_disciplina;
    private Button btn_IdSave_disciplina;



    private Disciplina disciplina;

    public static FragmentDisciplinaCadastro newInstancia(){
        FragmentDisciplinaCadastro fragemnt = new FragmentDisciplinaCadastro();
        return fragemnt;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disciplina = new Disciplina();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.disciplina_cadastro_fragment,container,false);

        spinner_Idperiodo = (Spinner) view.findViewById(R.id.spinner_Idperiodo);
        edt_nome_disciplina = (EditText) view.findViewById(R.id.edt_nome_disciplina);
        btn_IdCancel_disciplina = (Button) view.findViewById(R.id.btn_IdCancel_disciplina);
        btn_IdSave_disciplina = (Button) view.findViewById(R.id.btn_IdSave_disciplina);

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.periodos_disciplinas));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Idperiodo.setAdapter(adapter);

        btn_IdSave_disciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDisciplina();
            }
        });

        btn_IdCancel_disciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }


    private void saveDisciplina(){

        if(validaDisciplina() == true){

            dadosDisciplina();
            new ClasseCadastroDiscipliana().execute(disciplina);

        }

    }

    private boolean validaDisciplina(){
        boolean resultado = true;

            if(edt_nome_disciplina.getText().toString().trim().equals("")){
                Toast.makeText(getActivity(),R.string.message_valida_disciplina, Toast.LENGTH_SHORT).show();
                resultado = false;
            }

        return resultado;
    }

    private void dadosDisciplina(){

        disciplina.getFaculdade().setCodigo(faculdade_selecionada);
        disciplina.getCurso().setCodigo(curso_selecionado);
        disciplina.setPeriodo(Integer.parseInt(spinner_Idperiodo.getSelectedItem().toString()));
        disciplina.setNome(edt_nome_disciplina.getText().toString().trim());

    }

    class ClasseCadastroDiscipliana extends AsyncTask<Disciplina, Void,String> {

        @Override
        protected String doInBackground(Disciplina... params) {
            String obj ="";
            Gson gson = new Gson();

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("cadastrarDisciplina","POST",true);

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

            FragmentDisciplinaLista.consulta_disciplina = true;

        }
    }

    public void abrir(FragmentManager fm){
        show(fm,DIALOG_TAG);
    }



}
