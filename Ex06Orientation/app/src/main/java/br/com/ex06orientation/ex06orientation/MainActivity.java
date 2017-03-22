package br.com.ex06orientation.ex06orientation;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private ArrayList<String> listaNome;

    private EditText edtNome;
    private ArrayAdapter<String> adapter;
    private ListView listaV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            listaNome = savedInstanceState.getStringArrayList("lista");
        }else{
            listaNome = new ArrayList<String>();
        }

        edtNome = (EditText) findViewById(R.id.edtNome);
        listaV = (ListView) findViewById(R.id.listV);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listaNome);
        listaV.setAdapter(adapter);






    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("lista",listaNome);
    }

    public void meuButton(View view){

        if(edtNome.getText().toString().trim() != ""){

            listaNome.add(edtNome.getText().toString().trim());
            edtNome.setText("");
            adapter.notifyDataSetChanged();

            Toast.makeText(this,"Salvo",Toast.LENGTH_SHORT).show();

        }



    }











}
