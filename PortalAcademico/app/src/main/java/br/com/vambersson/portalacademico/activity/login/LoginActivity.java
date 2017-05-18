package br.com.vambersson.portalacademico.activity.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;

import br.com.vambersson.portalacademico.MainActivity;
import br.com.vambersson.portalacademico.R;
import br.com.vambersson.portalacademico.base.Usuario;

import br.com.vambersson.portalacademico.util.NetworkUtil;


public class LoginActivity extends AppCompatActivity {

   private String enderecoBase = "http://192.168.0.115:8080/PortalAcademico/servicos/";

    private Usuario usuario;
    private Gson gson;

    private Button login_btn_login;
    private Button login_btn_pri;

    private EditText login_Id_Edttxt_Matricula;
    private EditText login_Id_EdtTxt_Pass;


    public LoginActivity(){
        usuario = new Usuario();
        gson = new Gson();
    }

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


    private void primeiroAcesso(){

        if (validarPrimeiroAcesso()==true){
            dadosUsuarioPrimeiroAcesso();
            new ClassePrimeiroAcesso().execute();
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

    private void dadosUsuarioPrimeiroAcesso(){

        usuario.setMatricula(Integer.parseInt(login_Id_Edttxt_Matricula.getText().toString()));
        usuario.setSenha(null);
    }

    class ClassePrimeiroAcesso extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {

            String objJson = gson.toJson(usuario);

            HttpURLConnection conexao = null;
            String obj = "";

            try {

                conexao = NetworkUtil.conectar(enderecoBase+"verificarPrimeiroAcesso="+objJson,"GET");

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.converterInputStreamToString(is);
                }

                return obj;

            } catch (Exception e) {
                return e.getMessage();
            }finally {
                conexao.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if("null".equals(result)){
                Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){
                usuario = gson.fromJson(result,Usuario.class);
                chamaTelaPrimeiroAcesso();
            }

        }
    }

    private void chamaTelaPrimeiroAcesso(){

        Intent it = new Intent(LoginActivity.this,LoginPrimeiroAcesso.class);
        it.putExtra("usuario",usuario);
        startActivity(it);

    }



     private void logar(){

        if (validaLogin() == true){
            dadosLogarUsuario();
            new ClasseLogar().execute();
        }


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

    private void dadosLogarUsuario(){
        usuario.setMatricula(Integer.parseInt(login_Id_Edttxt_Matricula.getText().toString()));
        usuario.setLogin(Integer.parseInt(login_Id_Edttxt_Matricula.getText().toString()));
        usuario.setSenha(login_Id_EdtTxt_Pass.getText().toString().trim());

    }

    class ClasseLogar extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {

            String objJson = gson.toJson(usuario);

            HttpURLConnection conexao = null;
            String obj = "";

            try {

                conexao = NetworkUtil.conectar(enderecoBase+"logar="+objJson,"GET");

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.converterInputStreamToString(is);
                }

                return obj;

            } catch (Exception e) {
                return e.getMessage();
            }finally {
                conexao.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if("null".equals(result)){

                Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){
                usuario = gson.fromJson(result,Usuario.class);
                chamaTelaMainActivity();
            }

        }
    }

    private void chamaTelaMainActivity(){
        Intent it = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(it);
        finish();

    }




}
