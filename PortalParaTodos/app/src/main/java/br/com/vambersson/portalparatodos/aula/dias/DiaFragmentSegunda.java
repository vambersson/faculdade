package br.com.vambersson.portalparatodos.aula.dias;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.activity.ListaDisciplina;
import br.com.vambersson.portalparatodos.aula.disciplina.ListaDisciplinasUsuario;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.main.MainActivity;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vambersson on 05/06/2017.
 */

public class DiaFragmentSegunda extends Fragment {

    private static final int REQUEST_DISCIPLINA = 101;
    private final int NUMERO_DIA = 2;

    private Button btn_aula1;
    private Button btn_aula2;
    private Button btn_aula3;
    private Button btn_aula4;

    private TextView tv_dia;

    private Usuario usuario;
    private Disciplina disciplina;
    private List<Disciplina> listaDisciplina;

    private  int ordem_selecao = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaDisciplina = new ArrayList<Disciplina>();
        usuario = new Usuario();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("StateUsuario",usuario);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dia_segunda_fragment,container,false);

        btn_aula1 = (Button) view.findViewById(R.id.btn_aula1);
        btn_aula2 = (Button) view.findViewById(R.id.btn_aula2);
        btn_aula3 = (Button) view.findViewById(R.id.btn_aula3);
        btn_aula4 = (Button) view.findViewById(R.id.btn_aula4);

        tv_dia = (TextView) view.findViewById(R.id.tv_dia);

        btn_aula1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordem_selecao = 1;
                disciplinaUsuario();
            }
        });
        btn_aula2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordem_selecao = 2;
                disciplinaUsuario();
            }
        });
        btn_aula3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordem_selecao = 3;
                disciplinaUsuario();
            }
        });
        btn_aula4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordem_selecao = 4;
                disciplinaUsuario();
            }
        });


        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
        }else{
            usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
            carregarDisciplinas();
        }

        return view;
    }

    private void resultadoOrdemSelecao(String nome,int numero){

        switch (numero){
            case 1:
                btn_aula1.setText(nome);
                break;
            case 2:
                btn_aula2.setText(nome);
                break;
            case 3:
                btn_aula3.setText(nome);
                break;
            case 4:
                btn_aula4.setText(nome);
                break;
            default:
                break;
        }

    }

    private void salvardisciplina(Disciplina dis){

        dis.setOrdem(ordem_selecao);
        dis.setNumerodia(NUMERO_DIA);
        usuario.getCurso().setCodigo(dis.getCurso().getCodigo());
        usuario.setDisciplina(dis);
        usuario.setFoto(null);

        new ClasseSavarDisciplinaSelecionada().execute(usuario);
        Snackbar.make(getView(), "Salvando...", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        ordem_selecao = 0;

    }

    private void carregarDisciplinas(){
        new ClasseListaDisciplinasDasAulas().execute(usuario);
    }

    class ClasseSavarDisciplinaSelecionada extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {
            String obj ="";
            Gson gson = new Gson();
            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("usuarioSelecionouD","POST",true);

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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("1")){
                carregarDisciplinas();
                Snackbar.make(getView(), "Disciplina salva com sucesso!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }

        }

    }

    private void disciplinaUsuario(){

        Intent it = new Intent(getActivity(), ListaDisciplinasUsuario.class);

        for(int i=0;i< listaDisciplina.size();i++){

            if(ordem_selecao == listaDisciplina.get(i).getOrdem()){
                it.putExtra(ListaDisciplinasUsuario.EXTRA_DISCIPLINA_TROCA,listaDisciplina.get(i).getCodigo().toString());
            }

        }

        it.putExtra(ListaDisciplinasUsuario.EXTRA_USUARIO ,usuario);
        startActivityForResult(it,REQUEST_DISCIPLINA);

    }

    class ClasseListaDisciplinasDasAulas extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("disciplinasDasAulas="+params[0].getFaculdade().getCodigo() +"=" +params[0].getCodigo()+"="+NUMERO_DIA,"GET",false);

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

                Toast.makeText(getActivity() ,getResources().getString(R.string.message_alerta_disciplina_cadastrada), Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){
                Gson gson = new Gson();
                try{

                    Disciplina[] lista =  gson.fromJson(result, Disciplina[].class);

                    List<Disciplina> temp = new ArrayList<Disciplina>(Arrays.asList(lista) );
                    listaDisciplina = temp;
                    for(int i = 0; i < temp.size(); i++){

                        resultadoOrdemSelecao(temp.get(i).getNome(),temp.get(i).getOrdem());

                    }

                }catch(Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }









        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_DISCIPLINA && resultCode == RESULT_OK){
            disciplina = new Disciplina();
            disciplina = (Disciplina) data.getSerializableExtra(ListaDisciplinasUsuario.EXTRA_RESULTADO);
            disciplina.setSelecionou("S");
            salvardisciplina(disciplina);

        }





    }
}
