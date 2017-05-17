package br.com.vambersson.portalacademico.download;



import android.os.AsyncTask;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;

import br.com.vambersson.portalacademico.base.Usuario;
import br.com.vambersson.portalacademico.util.NetworkUtil;


/**
 * Created by Vambersson on 16/05/2017.
 */

public class UsuarioDownload {

    private Usuario usuario;
    private String enderecoBase = "http://10.0.0.40:8080/PortalAcademico/servicos/";

    public UsuarioDownload(){
        teste();
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void teste(){
        new ClassePrimeiroAcesso().execute();
    }

    class ClassePrimeiroAcesso extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {

            Gson gson = new Gson();

            String objJson = gson.toJson(usuario);

            HttpURLConnection conexao = null;
            String obj = "";

            try {

                conexao = NetworkUtil.conectar(enderecoBase+"verificarPrimeiroAcesso="+objJson,"GET");

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.converterInputStreamToString(is);

                }

                return obj;

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
