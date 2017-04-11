package br.com.vambersson.ex08listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import br.com.vambersson.ex08listview.pessoa.Pessoa;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Pessoa> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



}
