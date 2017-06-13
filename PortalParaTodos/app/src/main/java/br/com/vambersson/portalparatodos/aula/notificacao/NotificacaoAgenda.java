package br.com.vambersson.portalparatodos.aula.notificacao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import br.com.vambersson.portalparatodos.R;

/**
 * Created by Vambersson on 13/06/2017.
 */

public class NotificacaoAgenda extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_menssagem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificacao_agenda);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_menssagem = (TextView) findViewById(R.id.tv_menssagem);

    }


}
