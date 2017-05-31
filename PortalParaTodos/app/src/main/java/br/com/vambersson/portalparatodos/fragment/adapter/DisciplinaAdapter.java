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
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 30/05/2017.
 */

public class DisciplinaAdapter extends BaseAdapter {

    private TextView tv_nome;
    private Button btn_remover;
    private CheckedTextView checked_selecao;

    private List<Disciplina> lista;
    private Activity act;

    public DisciplinaAdapter(Activity act, List<Disciplina> lista){
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inf = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  v = inf.inflate(R.layout.disciplina_adapter,parent,false);

        Disciplina disciplina  = lista.get(position);

        tv_nome = (TextView) v.findViewById(R.id.tv_nome);
        btn_remover =(Button) v.findViewById(R.id.btn_remover);
        checked_selecao = (CheckedTextView) v.findViewById(R.id.checked_selecao);

        tv_nome.setText(disciplina.getNome());


        tv_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                checked_selecao.setBackground(act.getResources().getDrawable(R.drawable.ic_check_box_accent_24dp));

            }
        });

        btn_remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                HttpURLConnection conexao = NetworkUtil.abrirConexao("deleteCurso="+ params[0].getCodigo(),"GET",false);


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

            }
        }
    }




}
