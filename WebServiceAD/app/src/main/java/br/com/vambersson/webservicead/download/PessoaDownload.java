package br.com.vambersson.webservicead.download;



import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.vambersson.webservicead.base.Pessoa;
import br.com.vambersson.webservicead.util.NetworkUtil;

/**
 * Created by Vambersson on 06/04/2017.
 */

public class PessoaDownload  extends AsyncTask<String,Void,List<Pessoa>> {

    private final String URlLocal = "http://10.0.0.57:8080/WSAndroid/rest/servicos";


    private List<Pessoa> lerJSON(JSONObject json) throws Exception{
        List<Pessoa> lista = new ArrayList<>();
        Pessoa pessoa;

        JSONArray jsonArray = new JSONArray(json);

        for(int i=0;i < jsonArray.length();i++){

        JSONObject pessoaAtual  =  jsonArray.getJSONObject(i);

            for(int p = 0; p < pessoaAtual.length();p++){
                pessoa = new Pessoa();

                pessoa.setId(pessoaAtual.getInt("id"));
                pessoa.setNome(pessoaAtual.getString("nome"));

                lista.add(pessoa);

            }

        }
        return lista;
    }


    @Override
    protected List<Pessoa> doInBackground(String... params) {
        try {

            HttpURLConnection conexao = NetworkUtil.conectar(URlLocal,"GET");
            InputStream is;

            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {

                is = conexao.getInputStream();
                JSONObject jsonlista  = new JSONObject(NetworkUtil.converterInputStreamToString(is));

                return lerJSON(jsonlista);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
