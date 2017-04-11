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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.vambersson.webservicead.base.Pessoa;
import br.com.vambersson.webservicead.download.PessoaDownload;
import br.com.vambersson.webservicead.util.NetworkUtil;

public class MainActivity extends AppCompatActivity {




    private ArrayAdapter<Pessoa> adapter;
    private Button btnlista;
    private ListView listaV;

    private List lista;
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


    public class PessoaTask extends AsyncTask<String , Void, String>{

        private String endereco = "http://10.0.0.57:8080/WSAndroid/rest/servicos";

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection conexao = null;

            if(NetworkUtil.virificaConexao(MainActivity.this)){

                try {

                    /*
                    URL url = new URL(endereco);
                    conexao = (HttpURLConnection) url.openConnection();
                    conexao.setRequestMethod("GET");
                    conexao.connect();
                    */

                  conexao = NetworkUtil.conectar(endereco,"GET");




                    if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream is = conexao.getInputStream();
                        JSONObject jsonlista  = new JSONObject(NetworkUtil.converterInputStreamToString(is));

                       return jsonlista.toString();


                    }

                } catch (Exception e) {

                }finally {
                    conexao.disconnect();
                }


            }else{

            }

            return null;
        }

        @Override
        protected void onPostExecute(String txt) {
            super.onPostExecute(txt);

            Toast.makeText(MainActivity.this, "Aqui :" + txt, Toast.LENGTH_SHORT).show();

            //adapter = new ArrayAdapter<Pessoa>(MainActivity.this,android.R.layout.simple_list_item_2);
           // listaV.setAdapter(adapter);
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








}
