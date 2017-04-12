package br.com.vambersson.webservicead;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.webservicead.adapter.PessoaAdapter;
import br.com.vambersson.webservicead.base.Pessoa;
import br.com.vambersson.webservicead.download.PessoaDownload;
import br.com.vambersson.webservicead.util.NetworkUtil;

public class MainActivity extends AppCompatActivity {




    private PessoaAdapter adapter;
    private Button btnlista;
    private ListView listaV;

    private List<Pessoa> lista;
    private PessoaDownload download;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaV = (ListView) findViewById(R.id.litaV);



        btnlista = (Button) findViewById(R.id.btnlista);
        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PessoaTask().execute();

            }
        });







    }


    public class PessoaTask extends AsyncTask<String , Void, List<Pessoa>>{

        private String endereco = "http://192.168.43.123:8080/WSAndroid/rest/servicos";

        @Override
        protected List<Pessoa> doInBackground(String... params) {

            List<Pessoa> lista = new ArrayList<Pessoa>();
            HttpURLConnection conexao = null;

            if(NetworkUtil.virificaConexao(MainActivity.this)){

                try {

                  conexao = NetworkUtil.conectar(endereco,"GET");

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
        protected void onPostExecute(List<Pessoa> lista) {
            try {
                super.onPostExecute(lista);

                adapter = new PessoaAdapter(lista, MainActivity.this);
                listaV.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                //Toast.makeText(MainActivity.this, "Aqui :" + lista.get(0).getNome() + " e " + lista.get(1).getNome(), Toast.LENGTH_SHORT).show();
            } catch (Exception ex){
                Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }


/*
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
*/

    }








}
