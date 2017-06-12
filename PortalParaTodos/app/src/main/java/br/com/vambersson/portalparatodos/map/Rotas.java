package br.com.vambersson.portalparatodos.map;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;



public class Rotas extends AsyncTask<Void, Void, List<LatLng>> {

    private LatLng origem;
    private LatLng destino;

    private OnRotaCriadaListner onRotaCriadaListner;

    public Rotas(OnRotaCriadaListner rotaCriadas, LatLng origem, LatLng destino){
        this.origem = origem;
        this.destino = destino;
        onRotaCriadaListner = rotaCriadas;
    }

    @Override
    protected List<LatLng> doInBackground(Void... params) {

        List<LatLng> rota = gerarRota();
        return rota;

    }

    @Override
    protected void onPostExecute(List<LatLng> rota){
        onRotaCriadaListner.rotaCriada(rota);
    }

    private List<LatLng> gerarRota(){

        List<LatLng> posicoes = new ArrayList<>();

        try{
            String urlStr = String.format(Locale.US, "http://maps.google.com/maps/api/directions/json?"+
                            "origin=%f,%f&destination=%f,%f&"+
                            "mode=driving",
                    origem.latitude, origem.longitude,
                    destino.latitude, destino.longitude);

            URL url = new URL(urlStr);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setDoOutput(false);

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuffer jsonBuffer = new StringBuffer();
            while (scanner.hasNext())
                jsonBuffer.append(scanner.next());

            JSONObject objeto = new JSONObject(jsonBuffer.toString());
            JSONObject rotas = objeto.getJSONArray("routes").getJSONObject(0);
            JSONObject parte = rotas.getJSONArray("legs").getJSONObject(0);
            JSONArray passos = parte.getJSONArray("steps");

            int qtd = passos.length();

            JSONObject passo;

            for(int i = 0; i < qtd; i++){
                passo = passos.getJSONObject(i);
                String pontos = passo.getJSONObject("polyline").getString("points");
                posicoes.addAll(PolyUtil.decode(pontos));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return posicoes;
    }

    public interface OnRotaCriadaListner{

        void rotaCriada(List<LatLng> rotas);
    }


}
