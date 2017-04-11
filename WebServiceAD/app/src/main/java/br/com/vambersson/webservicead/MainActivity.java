package br.com.vambersson.webservicead;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
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

                try {
                    teste1();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });







    }

    public void teste1()throws Exception{
        String URlLocal = "http://localhost:8080/WSAndroid/rest/servicos";

        NetworkUtil.conectar(URlLocal,"GET");








    }



    public void teste() throws Exception{
        download = new PessoaDownload();
        lista = download.listaPessoas();

        adapter = new ArrayAdapter<Pessoa>(this,android.R.layout.simple_list_item_1,lista);
        listaV.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }








}
