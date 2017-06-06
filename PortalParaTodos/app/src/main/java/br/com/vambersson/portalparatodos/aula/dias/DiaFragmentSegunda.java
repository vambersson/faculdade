package br.com.vambersson.portalparatodos.aula.dias;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.activity.ListaDisciplina;
import br.com.vambersson.portalparatodos.aula.disciplina.ListaDisciplinasUsuario;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vambersson on 05/06/2017.
 */

public class DiaFragmentSegunda extends Fragment {

    private static final int REQUEST_DISCIPLINA = 101;

    private Button btn_aula1;
    private Button btn_aula2;
    private Button btn_aula3;
    private Button btn_aula4;

    private TextView tv_dia;

    private Usuario usuario;
    private Disciplina disciplina;

    private  int ordem_selecao = 0;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = new Usuario();

        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
        }else{
            usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        }

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


        return view;
    }

    private void salvardisciplina(Disciplina dis){



        dis.setOrdem(ordem_selecao);
        dis.setNumerodia(2);
        usuario.setCodigo(7);
        usuario.setDisciplina(dis);
        usuario.setFoto(null);

        new Classesalvardisciplina().execute(usuario);
    }


    class Classesalvardisciplina extends AsyncTask<Usuario, Void,String> {

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            btn_aula1.setText(disciplina.getNome());
        }
    }


    private void disciplinaUsuario(){

        Intent it = new Intent(getActivity(), ListaDisciplinasUsuario.class);
        it.putExtra(ListaDisciplinasUsuario.EXTRA_USUARIO ,usuario);
        startActivityForResult(it,REQUEST_DISCIPLINA);

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
