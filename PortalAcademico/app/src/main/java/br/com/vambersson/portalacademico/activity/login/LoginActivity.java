package br.com.vambersson.portalacademico.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import br.com.vambersson.portalacademico.MainActivity;
import br.com.vambersson.portalacademico.R;
import br.com.vambersson.portalacademico.base.Usuario;
import br.com.vambersson.portalacademico.ws.IWSProjAndroid;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final  IWSProjAndroid WS = IWSProjAndroid.retrofit.create(IWSProjAndroid.class);

    Usuario usuario = new Usuario();

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
                logar();
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
        it.putExtra("usuario",usuario);
        startActivity(it);
        usuario = null;

    }

    private void chamaTelaMainActivity(){
        Intent it = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(it);
        finish();

    }



    private void primeiroAcesso(){

        if (validarPrimeiroAcesso()==true){
            dadosUsuarioPrimeiroAcesso();
            Gson gson = new Gson();
            Call<Usuario> call = WS.verificarPrimeiroAcesso(gson.toJson(usuario));
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if(response.code()==200){
                        chamaTelaCadastroLogin();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Aluno não encontrado", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean validarPrimeiroAcesso(){

        boolean resultado = true;

        if(login_Id_Edttxt_Matricula.getText().toString().trim().equals("")){

            Toast.makeText(this, "Matrícula não Informada", Toast.LENGTH_LONG).show();
            return resultado = false;

        }

        return resultado;
    }

    private boolean validaLogin(){

        boolean resultado = true;

        if(login_Id_Edttxt_Matricula.getText().toString().trim().equals("")){

            Toast.makeText(this, "Matrícula não Informada", Toast.LENGTH_LONG).show();
            return  resultado = false;

        }else if(login_Id_EdtTxt_Pass.getText().toString().trim().equals("")){

            Toast.makeText(this, "Senha não Informada", Toast.LENGTH_LONG).show();
            return  resultado = false;
        }

        return  resultado;

    }

    private void dadosUsuario(){

        usuario.setLogin(Integer.parseInt(login_Id_Edttxt_Matricula.getText().toString()));
        usuario.setSenha(login_Id_EdtTxt_Pass.getText().toString());


    }

    private void dadosUsuarioPrimeiroAcesso(){

        usuario.setMatricula(Integer.parseInt(login_Id_Edttxt_Matricula.getText().toString()));
        usuario.setSenha(null);
    }


    private void logar(){

        if (validaLogin() == true){
            Gson gson = new Gson();
            dadosUsuario();
            Call<Usuario> call = WS.logar(gson.toJson(usuario));
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.code()==200){
                        chamaTelaMainActivity();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Deu merda", Toast.LENGTH_LONG).show();
                }
            });
        }


    }




}
