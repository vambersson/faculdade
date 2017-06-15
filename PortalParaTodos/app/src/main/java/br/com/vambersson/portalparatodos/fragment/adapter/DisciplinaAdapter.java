package br.com.vambersson.portalparatodos.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentDisciplinaLista;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 30/05/2017.
 */

public class DisciplinaAdapter extends BaseAdapter {

    private TextView tv_nome;
    private Button btn_remover;

    private List<Disciplina> lista;
    private Activity act;

    public DisciplinaAdapter(Activity act){
        this.act = act;

    }

    public void clear(){
        lista.clear();
    }

    public void setLista(List<Disciplina> lista) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inf = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  v = inf.inflate(R.layout.disciplina_adapter,parent,false);

        final Disciplina disciplina  = lista.get(position);

        tv_nome = (TextView) v.findViewById(R.id.tv_nome);
        btn_remover =(Button) v.findViewById(R.id.btn_remover);


        tv_nome.setText(disciplina.getNome());

        btn_remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClasseDeleta().execute(disciplina);
            }
        });


        return  v;
    }

    class ClasseDeleta extends AsyncTask<Disciplina, Void,String> {

        @Override
        protected String doInBackground(Disciplina... params) {
            String obj ="";
            Gson gson = new Gson();

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("deleteDisciplina="+ params[0].getFaculdade().getCodigo() +"="+ params[0].getCodigo(),"GET",false);

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
                Toast.makeText(act,act.getResources().getString(R.string.message_alerta_disciplina_exclui)  , Toast.LENGTH_SHORT).show();
                FragmentDisciplinaLista.consulta_disciplina = true;

            }
        }
    }




}
