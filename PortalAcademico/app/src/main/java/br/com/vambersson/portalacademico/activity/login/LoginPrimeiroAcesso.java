package br.com.vambersson.portalacademico.activity.login;

import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.IOException;

import br.com.vambersson.portalacademico.MainActivity;
import br.com.vambersson.portalacademico.R;
import br.com.vambersson.portalacademico.base.Aluno;
import br.com.vambersson.portalacademico.base.Login;
import br.com.vambersson.portalacademico.ws.IWSProjAndroid;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPrimeiroAcesso extends AppCompatActivity {
    private final IWSProjAndroid WS = IWSProjAndroid.retrofit.create(IWSProjAndroid.class);

    private static final int TIRAR_FOTO = 100;

    Aluno aluno = new Aluno();

    Login login = new Login();


    private ImageView login_cad_IdimageView;
    private FloatingActionButton login_cad_IdCamera;
    private TextView login_Id_Cad_Matricula;
    private EditText login_Id_Cad_Nome;
    private EditText login_cad_tv_senha;
    private EditText login_cad_tv_senha_confirma;


    private Button login_cad_IdSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_primeiro_acesso_activity);

        login_Id_Cad_Matricula = (TextView) findViewById(R.id.login_Id_Cad_Matricula);

        login_cad_IdimageView = (ImageView) findViewById(R.id.login_cad_IdimageView);

        login_Id_Cad_Nome = (EditText) findViewById(R.id.login_Id_Cad_Nome);
        login_cad_tv_senha = (EditText) findViewById(R.id.login_Id_Cad_Senha);
        login_cad_tv_senha_confirma = (EditText) findViewById(R.id.login_Id_Cad_Senha_Confirma);

        login_cad_IdSave = (Button) findViewById(R.id.login_cad_IdSave);
        login_cad_IdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaAluno();
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

        aluno = (Aluno) getIntent().getSerializableExtra("aluno");
        if(aluno != null){
            login_Id_Cad_Matricula.setText(Integer.toString(aluno.getMatricula()));
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
        startActivity(it);
        finish();

    }

    private void cadastrarLogin(){

        if (login_cad_tv_senha.getText().toString().equals(login_cad_tv_senha_confirma.getText().toString())){

            login.setMatricula(aluno.getMatricula());
            login.setLogin(Integer.toString(aluno.getMatricula()));
            login.setSenha(login_cad_tv_senha.getText().toString());
            login.setStatusLogado("S");

        }


        Gson gson = new Gson();

        Call<String> call = WS.cadastrarLogin( gson.toJson(login));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("1")){
                    Toast.makeText(LoginPrimeiroAcesso.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    chamaTelaMainActivity();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginPrimeiroAcesso.this, "Deu ruim!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void atualizaAluno(){

        aluno.setNome(login_Id_Cad_Nome.getText().toString());


        Gson gson = new Gson();

        Call<String> call = WS.atualizarAluno( gson.toJson(aluno));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                if(response.body().equals("1")){
                    Toast.makeText(LoginPrimeiroAcesso.this, "Total:   " + response.body(), Toast.LENGTH_SHORT).show();
                    cadastrarLogin();
                }else{
                    Toast.makeText(LoginPrimeiroAcesso.this, "não 0  " + response.body(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginPrimeiroAcesso.this, "Erro", Toast.LENGTH_SHORT).show();
            }
        });


    }
















}
