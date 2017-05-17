package br.com.vambersson.webservicead.download;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.webservicead.MainActivity;
import br.com.vambersson.webservicead.base.Pessoa;
import br.com.vambersson.webservicead.base.Usuario;
import br.com.vambersson.webservicead.util.NetworkUtil;

/**
 * Created by Vambersson on 16/05/2017.
 */

public class UsuarioDownload {

    Usuario usuario;
    String enderecoBase = "";

    public UsuarioDownload(Usuario usuario){
            this.usuario = usuario;
    }

    public Usuario chamar(){

        new verificarPrimeiroAcesso();


        return usuario;
    }



    class verificarPrimeiroAcesso extends AsyncTask<Usuario, Void,String>{

        @Override
        protected String doInBackground(Usuario... params) {

            Gson gson = new Gson();

            String objJson = gson.toJson(usuario);

            HttpURLConnection conexao = null;
            String jsonlista = "";

            try {

                conexao = NetworkUtil.conectar(enderecoBase + "verificarPrimeiroAcesso=" + objJson ,"GET");

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    jsonlista  = NetworkUtil.converterInputStreamToString(is);
                }

                return jsonlista;

            } catch (Exception e) {
                return e.getMessage();
            }finally {
                conexao.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Gson gson = new Gson();
            usuario = gson.fromJson(result,Usuario.class);

        }


    }



}
