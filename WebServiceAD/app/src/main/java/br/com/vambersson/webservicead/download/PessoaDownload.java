package br.com.vambersson.webservicead.download;



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

public class PessoaDownload{

    private final String URlLocal = "http://localhost:8080/WSAndroid/rest/servicos";

    public  List<Pessoa> listaPessoas()throws Exception{

        HttpURLConnection conexao = NetworkUtil.conectar(URlLocal,"GET");

        InputStream is;

        try {

            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {

                is = conexao.getInputStream();
                JSONObject jsonlista  = new JSONObject(NetworkUtil.converterInputStreamToString(is));

                return lerJSON(jsonlista);
            }

        }catch(Exception e){

        }
        return null;
    }



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


}
