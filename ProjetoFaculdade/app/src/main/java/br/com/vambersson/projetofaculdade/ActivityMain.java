package br.com.vambersson.projetofaculdade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.vambersson.projetofaculdade.activity.ActivityPrincipal;
import br.com.vambersson.projetofaculdade.activity.logincadastro.ActivityCadLogin;

/**
 * Created by Vambersson on 11/05/2017.
 */

public class ActivityMain extends Activity {

    private Button login_btn_login;
    private Button login_btn_pri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login_btn_login = (Button) findViewById(R.id.login_btn_login);
        login_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chamarTelaPrincipal();
            }
        });

        login_btn_pri = (Button) findViewById(R.id.login_btn_pri);
        login_btn_pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chamaTelaCadastroLogin();
            }
        });









    }

    public void chamarTelaPrincipal(){
        Intent it = new Intent(this, ActivityPrincipal.class);
        startActivity(it);
    }


    private void chamaTelaCadastroLogin(){
        Intent it = new Intent(this, ActivityCadLogin.class);
        startActivity(it);
    }























}
