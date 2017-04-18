package br.com.vambersson.webservicead.download;


import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.webservicead.MainActivity;
import br.com.vambersson.webservicead.base.Pessoa;
import br.com.vambersson.webservicead.util.NetworkUtil;

/**
 * Created by Vambersson on 06/04/2017.
 */

public class PessoaDownload   extends AsyncTask<String ,Pessoa, List<Pessoa>>{

    private final String ENDERECO = "http://192.168.43.123:8080/WSAndroid/rest/servicos";
    private Context ctx;

    public PessoaDownload(Context ctx){
        this.ctx = ctx;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Pessoa> doInBackground(String... params) {


       if(params[0] == "1"){
           List<Pessoa> lista = new ArrayList<Pessoa>();
         return  listaPessoasPrivada();

       }else if(params[1] == "2"){

       }






        return null;
    }

    @Override
    protected void onPostExecute(List<Pessoa> pessoas) {
        super.onPostExecute(pessoas);
    }

    public List<Pessoa> listaPessoas(){
        List<Pessoa> lista = new ArrayList<Pessoa>();

        this.execute("1");

        return null;
    }
    private List<Pessoa> listaPessoasPrivada(){

        List<Pessoa> lista = new ArrayList<Pessoa>();
        HttpURLConnection conexao = null;

        if(NetworkUtil.virificaConexao(ctx)){

            try {

                conexao = NetworkUtil.conectar(ENDERECO,"GET");

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();

                    String jsonlista  = NetworkUtil.converterInputStreamToString(is);

                    Gson gson = new Gson();

                    Pessoa[] lista2 = gson.fromJson(jsonlista, Pessoa[].class);
                    lista = Arrays.asList(lista2) ;

                    return lista;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                conexao.disconnect();
            }


        }

        return null;
    }



    public Pessoa pesquisaPessoa(){




        return null;
    }



}
