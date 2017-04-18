package br.com.vambersson.webservicead.download;



import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.webservicead.MainActivity;
import br.com.vambersson.webservicead.base.Pessoa;
import br.com.vambersson.webservicead.erro.DownloadException;
import br.com.vambersson.webservicead.util.NetworkUtil;

/**
 * Created by Vambersson on 06/04/2017.
 */

public class PessoaDownload  extends AsyncTask<String,Void,List<Pessoa>> {

    public String endereco = "http://192.168.0.150:8080/WSAndroid/rest/servicos";

    private Context ctx;
    private String metodo;

    private HttpURLConnection conexao = null;

    public PessoaDownload(Context ctx,String metodo){
        this.ctx = ctx;
        this.metodo = metodo;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Pessoa> doInBackground(String... params){
        List<Pessoa> lista = new ArrayList<Pessoa>();

        if(NetworkUtil.virificaConexao(ctx)){

            try {

                this.conexao = NetworkUtil.conectar(endereco,metodo);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();

                    String jsonlista  = NetworkUtil.converterInputStreamToString(is);

                    Gson gson = new Gson();

                    Pessoa[] lista2 = gson.fromJson(jsonlista, Pessoa[].class);
                    lista = Arrays.asList(lista2);

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


    @Override
    protected void onPostExecute(List<Pessoa> pessoas) {
        super.onPostExecute(pessoas);
    }






}
