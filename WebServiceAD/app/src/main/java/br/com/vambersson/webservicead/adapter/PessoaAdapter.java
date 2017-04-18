package br.com.vambersson.webservicead.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.vambersson.webservicead.R;
import br.com.vambersson.webservicead.base.Pessoa;

/**
 * Created by Vambersson on 11/04/2017.
 */

public class PessoaAdapter extends BaseAdapter {

    private List<Pessoa> pessoas;
    private Activity activity;

    public PessoaAdapter(Activity activity,List<Pessoa> pessoas) {
        this.activity = activity;
        this.pessoas = pessoas;
    }

    public void addall(List<Pessoa> lista){
        for (int i=0;i< lista.size();i++){
            pessoas.add(lista.get(i));
        }
    }

    public void clear(){
        pessoas.clear();
    }

    @Override
    public int getCount() {
        return pessoas.size();
    }

    @Override
    public Object getItem(int position) {
        return pessoas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View v = view;

        if(view == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.pessoa_item_list,null);
        }

        Pessoa p = pessoas.get(position);

        TextView nome = ((TextView) v.findViewById(R.id.txtNome));
        nome.setText(p.getNome());

        TextView codigo = ((TextView) v.findViewById(R.id.txtCodigo));
        codigo.setText(Integer.toString(p.getId()));


        return v;
    }
}
