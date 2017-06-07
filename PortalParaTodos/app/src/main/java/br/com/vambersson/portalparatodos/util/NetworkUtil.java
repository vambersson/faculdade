package br.com.vambersson.portalparatodos.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.vambersson.portalparatodos.erro.ConexaoException;


/**
 * Created by Vambersson on 06/04/2017.
 */

public class NetworkUtil {

      private static String urlBase = "http://177.105.72.150:8888/PortalAcademico/servicos/";
      //private static String urlBase = "http://10.0.0.57:8080/PortalAcademico/servicos/";



    public static boolean verificarConexao(Context ctx){
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }


    public static String streamToString(InputStream is){

        byte[] buffer = new byte[1024];

        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();

        int bytesLidos;

        try {

            while((bytesLidos = is.read(buffer)) != -1  ){
                bufferzao.write(buffer,0,bytesLidos);
            }

            return new String(bufferzao.toByteArray(),"UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpURLConnection abrirConexao(String metodoURL, String requestMethod , boolean doOutPut) throws Exception{

        URL url = new URL(urlBase+metodoURL);
        HttpURLConnection  conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(10000);
        conexao.setConnectTimeout(10000);
        conexao.setRequestMethod(requestMethod);
        conexao.setDoInput(true);
        conexao.setDoOutput(doOutPut);

        if(doOutPut){
            conexao.addRequestProperty("Content-Type","application/json");
        }

        conexao.connect();

        return conexao;

    }







}
