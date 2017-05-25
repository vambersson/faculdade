package br.com.vambersson.portalparatodos.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;

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
    public View getView(int position, View convertView, ViewGroup parent) {

         LayoutInflater inf = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View  v = inf.inflate(R.layout.curso_adapter,parent,false);

        Curso curso = lista.get(position);
        tv_nome = (TextView) v.findViewById(R.id.tv_nome);
        btn_remover = (Button) v.findViewById(R.id.btn_remover);

        tv_nome.setText(curso.getNome());

        return v;
    }


}
