package br.com.vambersson.portalparatodos.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentCursoLista;
import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentDisciplinaLista;
import br.com.vambersson.portalparatodos.fragment.gerenciador.GerenciadorFragment;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class CursoAdapter extends BaseAdapter {

    private TextView tv_nome;
    private Button btn_remover;

    private List<Curso> lista;
    private Activity act;


    public CursoAdapter(Activity act, List<Curso> lista){
        this.act = act;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

         LayoutInflater inf = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View  v = inf.inflate(R.layout.curso_adapter,parent,false);

        final Curso curso = lista.get(position);
        tv_nome = (TextView) v.findViewById(R.id.tv_nome);

        tv_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentDisciplinaLista.consulta_disciplina = true;
                FragmentDisciplinaLista.faculdade_selecionada = curso.getFaculdade().getCodigo();
                FragmentDisciplinaLista.curso_selecionado = curso.getCodigo();

            }
        });

        btn_remover = (Button) v.findViewById(R.id.btn_remover);
        btn_remover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new ClasseDeleta().execute(curso);

            }
        });
        tv_nome.setText(curso.getNome());

        return v;
    }

    class ClasseDeleta extends AsyncTask<Curso, Void,String> {

        @Override
        protected String doInBackground(Curso... params) {
            String obj ="";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("deleteCurso="+ params[0].getFaculdade().getCodigo() +"="+ params[0].getCodigo(),"GET",false);

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
            if(result.equals("1") ){
                Toast.makeText(act,act.getResources().getString(R.string.message_alerta_curso_exclui)  , Toast.LENGTH_SHORT).show();
                FragmentCursoLista.consulta_curso = true;

            }
        }
    }







}
