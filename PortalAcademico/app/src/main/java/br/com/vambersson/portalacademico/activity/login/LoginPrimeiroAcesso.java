package br.com.vambersson.portalacademico.activity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;

import br.com.vambersson.portalacademico.MainActivity;
import br.com.vambersson.portalacademico.R;
import br.com.vambersson.portalacademico.base.Usuario;
import br.com.vambersson.portalacademico.util.NetworkUtil;


public class LoginPrimeiroAcesso extends AppCompatActivity {

    private String enderecoBase = "http://192.168.0.115:8080/PortalAcademico/servicos/";

    private static final int TIRAR_FOTO = 100;

    private Usuario usuario;
    private Gson gson;


    private ImageView login_cad_IdimageView;
    private FloatingActionButton login_cad_IdCamera;
    private TextView login_Id_Cad_Matricula;
    private EditText login_Id_Cad_Nome;
    private EditText login_Id_Cad_Email;
    private EditText login_cad_tv_senha;
    private EditText login_cad_tv_senha_confirma;


    private Button login_cad_IdSave;


    public LoginPrimeiroAcesso(){
        usuario = new Usuario();
        gson = new Gson();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_primeiro_acesso_activity);

        login_Id_Cad_Matricula = (TextView) findViewById(R.id.login_Id_Cad_Matricula);

        login_cad_IdimageView = (ImageView) findViewById(R.id.login_cad_IdimageView);

        login_Id_Cad_Nome = (EditText) findViewById(R.id.login_Id_Cad_Nome);
        login_Id_Cad_Email = (EditText) findViewById(R.id.login_Id_Cad_Email);
        login_cad_tv_senha = (EditText) findViewById(R.id.login_Id_Cad_Senha);
        login_cad_tv_senha_confirma = (EditText) findViewById(R.id.login_Id_Cad_Senha_Confirma);

        login_cad_IdSave = (Button) findViewById(R.id.login_cad_IdSave);
        login_cad_IdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });


        login_cad_IdCamera = (FloatingActionButton) findViewById(R.id.login_cad_IdCamera);
        login_cad_IdCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarCamera();
            }
        });



//      Mostra a matrícula do Aluno
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if(usuario != null){
            login_Id_Cad_Matricula.setText(Integer.toString(usuario.getMatricula()));
        }

    }

    private void salvar(){
        if(validarSalvar() == true){
            dadosUsuario();
            new ClasseAtualizar().execute();
        }

    }

    private boolean validarSalvar(){
        boolean resultado = true;

        if(login_Id_Cad_Nome.getText().toString().trim().equals("")){
            Toast.makeText(this, "Nome do Usuário Invalído", Toast.LENGTH_SHORT).show();
            return resultado = false;
        }else  if(login_Id_Cad_Email.getText().toString().trim().equals("")){
            Toast.makeText(this, "E-Mail do Usuário Invalído", Toast.LENGTH_SHORT).show();
            return resultado = false;
        }else if(login_cad_tv_senha.getText().toString().trim().equals("") |
                login_cad_tv_senha_confirma.getText().toString().trim().equals("")){
            Toast.makeText(this, "Senha do Usuário Invalído", Toast.LENGTH_SHORT).show();
            return resultado = false;
        }else if(!login_cad_tv_senha.getText().toString().trim().equals(login_cad_tv_senha_confirma.getText().toString().trim()) ){
            Toast.makeText(this, "Senha não idênticas", Toast.LENGTH_SHORT).show();
            return resultado = false;
        }

        return resultado;

    }

    private void dadosUsuario(){

        usuario.setNome(login_Id_Cad_Nome.getText().toString().trim());
        usuario.setEmail(login_Id_Cad_Email.getText().toString().trim());
        usuario.setLogin(usuario.getMatricula());
        usuario.setSenha(login_cad_tv_senha.getText().toString().trim());

    }

    class ClasseAtualizar extends AsyncTask<Usuario,Void, String>{

        @Override
        protected String doInBackground(Usuario... params) {
            String objJson = gson.toJson(usuario);

            HttpURLConnection conexao = null;
            String obj = "";

            try {

                conexao = NetworkUtil.conectar(enderecoBase+"atualizarUsuario="+objJson,"PUT");

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

            Toast.makeText(LoginPrimeiroAcesso.this, "Total de Alteração: "+ result, Toast.LENGTH_SHORT).show();
        }
    }







    private void chamarCamera(){

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (it.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(it, TIRAR_FOTO);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TIRAR_FOTO) {
            if (resultCode == RESULT_OK) {
                if(data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    login_cad_IdimageView.setImageBitmap(bitmap);

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), "A captura foi cancelada",
                            Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getBaseContext(), "A câmera foi fechada",
                            Toast.LENGTH_SHORT);
                }
            }
        }

    }

    private void chamaTelaMainActivity(){
        Intent it = new Intent(LoginPrimeiroAcesso.this,MainActivity.class);
        it.putExtra("usuario",usuario);
        startActivity(it);
        finish();

    }
















}
