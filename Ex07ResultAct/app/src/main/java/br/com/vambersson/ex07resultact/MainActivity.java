package br.com.vambersson.ex07resultact;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_ESTADO = 1;
    private static final String STATE_ESTADO = "estado";

    private Button btnestado;
    private String estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnestado = (Button) findViewById(R.id.btnState);
        btnestado.setOnClickListener(this);


        if(savedInstanceState != null){
            estado = savedInstanceState.getString(STATE_ESTADO);
            btnestado.setText(estado);
        }



    }

    @Override
    public void onClick(View v) {

        Intent it = new Intent(this,ActEstado.class);
        it.putExtra(ActEstado.EXTRA_ESTADO,estado);
        startActivityForResult(it,REQUEST_ESTADO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_ESTADO){

            estado = data.getStringExtra(ActEstado.EXTRA_RESULTADO);

            if(estado != null){
                btnestado.setText(estado);
            }

        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString(STATE_ESTADO,estado);
    }
}
