package br.com.vambersson.portalacademico.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import br.com.vambersson.portalacademico.MainActivity;
import br.com.vambersson.portalacademico.R;
import br.com.vambersson.portalacademico.base.Aluno;
import br.com.vambersson.portalacademico.ws.IWSProjAndroid;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final  IWSProjAndroid WS = IWSProjAndroid.retrofit.create(IWSProjAndroid.class);

    Aluno aluno = new Aluno();

    private Button login_btn_login;
    private Button login_btn_pri;

    private EditText login_Id_Edttxt_Matricula;
    private EditText login_Id_EdtTxt_Pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        login_Id_Edttxt_Matricula = (EditText) findViewById(R.id.login_Id_EdtTxt_Matricula);
        login_Id_EdtTxt_Pass = (EditText) findViewById(R.id.login_Id_EdtTxt_Pass);

        login_btn_login = (Button) findViewById(R.id.login_btn_login);
        login_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        login_btn_pri = (Button) findViewById(R.id.login_btn_pri);
        login_btn_pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                primeiroAcesso();

            }
        });



    }

    private void chamaTelaCadastroLogin(){

        Intent it = new Intent(LoginActivity.this,LoginPrimeiroAcesso.class);
        it.putExtra("aluno",aluno);
        startActivity(it);

    }

    private void chamaTelaMainActivity(){
        Intent it = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(it);
        finish();

    }



    private void primeiroAcesso(){

        pesquisaAluno();

    }

    private boolean validaMatricula(){

        boolean resultado = true;
        Toast.makeText(this, "Matrícula não Informada1111", Toast.LENGTH_LONG).show();
        if(login_Id_Edttxt_Matricula.getText().toString().trim() == null){

            Toast.makeText(this, "Matrícula não Informada", Toast.LENGTH_LONG).show();
            return  resultado = false;
        }else if(login_Id_Edttxt_Matricula.getText().toString() != ""){

            try{
                Integer.getInteger(login_Id_Edttxt_Matricula.getText().toString());
            }catch (Exception e){
                Toast.makeText(this, "Matrícula Inválida", Toast.LENGTH_LONG).show();
            }
            return  resultado = false;
        }

        return  resultado;

    }


    private void pesquisaAluno(){

        Call<Aluno> call = WS.pesquisaAluno(login_Id_Edttxt_Matricula.getText().toString().trim());

        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {

                if(response.code() == 200){
                    aluno = response.body();
                    chamaTelaCadastroLogin();
                }

            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Aluno não Encontrado", Toast.LENGTH_SHORT).show();
            }
        });

    }






}
