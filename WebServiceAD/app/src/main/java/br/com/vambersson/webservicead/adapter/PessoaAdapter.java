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

import java.util.List;

import br.com.vambersson.webservicead.R;
import br.com.vambersson.webservicead.base.Pessoa;

/**
 * Created by Vambersson on 11/04/2017.
 */

public class PessoaAdapter extends BaseAdapter {

    private List<Pessoa> pessoas;
    private Context ctx;

    public PessoaAdapter(List<Pessoa> pessoas,Context ctx) {
        this.pessoas = pessoas;
        this.ctx = ctx;
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

        return (long) pessoas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View linha = LayoutInflater.from(ctx).inflate(R.layout.pessoa_item_list,null);

        Pessoa p = pessoas.get(position);

        ImageView img = (ImageView) linha.findViewById(R.id.imgFoto);
        TextView nome = (TextView) linha.findViewById(R.id.txtNome);
        TextView codigo = (TextView) linha.findViewById(R.id.txtCodigo);

        //Resources res = ctx.getResources();

        img.setImageResource(R.mipmap.ic_launcher);
        nome.setText(p.getNome());
        codigo.setText(p.getId());

        return linha;
    }
}
