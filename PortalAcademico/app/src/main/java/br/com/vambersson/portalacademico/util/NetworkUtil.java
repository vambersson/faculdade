package br.com.vambersson.portalacademico.util;

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

import br.com.vambersson.portalacademico.erro.ConexaoException;


/**
 * Created by Vambersson on 06/04/2017.
 */

public class NetworkUtil {

    public static HttpURLConnection conectar(String endereco,String requestMethhod) throws ConexaoException {


        try{
            URL url = new URL(endereco);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod(requestMethhod);
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.setDoInput(true);
            conexao.connect();

            return conexao;

        }catch (IOException e){
            throw new ConexaoException(e);
        }


    }

    public static boolean virificaConexao(Context ctx){
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String converterInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while((linha = br.readLine())!= null){
                buffer.append(linha);
            }

            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();
    }





}
