package br.com.vambersson.portalparatodos.fragment.login;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.dao.UsuarioDao;
import br.com.vambersson.portalparatodos.fragment.gerenciador.GerenciadorFragment;
import br.com.vambersson.portalparatodos.main.MainActivity;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

/**
 * Created by Vambersson on 19/05/2017.
 */

public class FragmentLogin extends Fragment {

    private Gson gson;
    private Usuario usuario;
    private List<Curso> listaCursos;

    private ProgressBar progressBar;
    private int contador = 0;


    private Button login_btn_primeiroAcesso;
    private Button login_btn_login;

    private EditText login_Id_EdtTxt_Matricula;
    private EditText login_Id_EdtTxt_Senha;



    public FragmentLogin(){

        gson = new Gson();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment,container,false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        login_Id_EdtTxt_Matricula = (EditText) view.findViewById(R.id.login_Id_EdtTxt_Matricula);
        login_Id_EdtTxt_Senha = (EditText) view.findViewById(R.id.login_Id_EdtTxt_Senha);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        login_btn_login = (Button) view.findViewById(R.id.login_btn_login);
        login_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logar();
            }
        });

        login_btn_primeiroAcesso = (Button) view.findViewById(R.id.login_btn_primeiroAcesso);
        login_btn_primeiroAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primeiroAcesso();
            }
        });

        return view;
    }


    private void logar(){
        if(validaLogar() == true){
            dadosLogar();
            new ClasseLogar().execute();
        }
    }

    private boolean validaLogar(){
        boolean resultado = true;
        
        if(login_Id_EdtTxt_Matricula.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "Matricula não informada", Toast.LENGTH_SHORT).show();
            resultado = false;
        }else if(login_Id_EdtTxt_Senha.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "Senha não informada", Toast.LENGTH_SHORT).show();
            resultado = false;
        }

        return resultado;
    }

    private void dadosLogar(){
        usuario = new Usuario();
        usuario.setLogin(Integer.parseInt(login_Id_EdtTxt_Matricula.getText().toString().trim()));
        usuario.setSenha(login_Id_EdtTxt_Senha.getText().toString().trim());
    }

    class ClasseLogar extends AsyncTask<Usuario, Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(100);

            Thread t = new Thread(){
                public void run(){
                    while(contador < progressBar.getMax()){
                        try {
                            sleep(50);
                            contador  += 2;
                            progressBar.setProgress(contador);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            };  t.start();

        }

        @Override
        protected String doInBackground(Usuario... params) {

            HttpURLConnection conexao = null;
            String obj = "";

            try {

                conexao = NetworkUtil.conectar("GET","logar="+ usuario.getLogin() + "=" + usuario.getSenha());

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){

                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.converterInputStreamToString(is);
                    conexao.disconnect();
                }

                return obj;

            } catch (Exception e) {
                return e.getMessage();
            }finally {

            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            if(result == ""){

                Toast.makeText(getActivity(),"Dados invalídos", Toast.LENGTH_LONG).show();

            }else if("null".equals(result)){

                Toast.makeText(getActivity(),"Usuário não encontrado", Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){

                try{

                    Usuario user = new Usuario();

                    user = gson.fromJson(result,Usuario.class);

                    if(user.getStatus().equals("N")){

                        Toast.makeText(getActivity(), "Usuário inativo", Toast.LENGTH_SHORT).show();

                    }else if(user.getSenha() == null){

                        Toast.makeText(getActivity(), "Usuário não possui senha", Toast.LENGTH_SHORT).show();
                        usuario = user;
                        startFragment("FragmentCadastroUsuario");

                    }else if(!user.getSenha().equals(usuario.getSenha())){

                        Toast.makeText(getActivity(), "Senha não conferi", Toast.LENGTH_SHORT).show();

                    }else if(user.getSenha().equals(usuario.getSenha())){
                        usuario = user;
                        removerUsuarioLocal();
                        inserirUsuarioLocal();
                        startActivityMain(); // Activity = Principal

                    }

                }catch(Exception e){
                    Toast.makeText(getActivity(), "Web Service indisponível", Toast.LENGTH_LONG).show();
                }


            }

        }
    }




    private void primeiroAcesso(){

        if(validaPrimeiroAcesso() == true){
            dadosPrimeiroAcesso();
            new ClasseLogarPrimeiroAcesso().execute();
        }

    }

    private boolean validaPrimeiroAcesso(){
        boolean resultado = true;

        if(login_Id_EdtTxt_Matricula.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "Matricula não informada", Toast.LENGTH_SHORT).show();
            resultado = false;
        }

        return resultado;
    }

    private void dadosPrimeiroAcesso(){
        usuario = new Usuario();
        usuario.setLogin(Integer.parseInt(login_Id_EdtTxt_Matricula.getText().toString().trim()));
    }

    class ClasseLogarPrimeiroAcesso extends AsyncTask<Usuario, Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(100);

            Thread t = new Thread(){
                public void run(){
                    while(contador < progressBar.getMax()){
                        try {
                            sleep(50);
                            contador  += 2;
                            progressBar.setProgress(contador);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }; t.start();

        }

        @Override
        protected String doInBackground(Usuario... params) {

            HttpURLConnection conexao = null;
            String obj = "";

            try {

                conexao = NetworkUtil.conectar("GET","logar="+ usuario.getLogin() + "=" + 1);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.converterInputStreamToString(is);
                    conexao.disconnect();
                }

                return obj;

            } catch (Exception e) {
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            if(result == ""){

                Toast.makeText(getActivity(),"Dados invalídos", Toast.LENGTH_LONG).show();

            }else if("null".equals(result)){

                Toast.makeText(getActivity(),"Usuário não encontrado", Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){
                Usuario user = new Usuario();
                user = gson.fromJson(result,Usuario.class);

                if(user.getStatus().equals("N")){

                    Toast.makeText(getActivity(), "Usuário inativo", Toast.LENGTH_SHORT).show();

                }else if(user.getSenha() != null){

                    Toast.makeText(getActivity(), "Usuário já possui senha", Toast.LENGTH_SHORT).show();
                    // Recuperação de Senha

                }else  if(user.getSenha() == null){

                    if(user.getTipo().equals("A")){
                        usuario =  user;
                        startFragment("FragmentCadastroUsuario"); // Fragment  =  usuario_cadastro_aluno_fragment
                    }else if(user.getTipo().equals("P")){
                        usuario =  user;
                        startFragment("GerenciadorPages"); // Fragment  =  usuario_cadastro_professor_fragment
                    }


                }


            }









        }
    }

    private void startFragment(String CodigoFragment){
        Intent it = new Intent(getActivity(), GerenciadorFragment.class);
        it.putExtra("CodigoFragment",CodigoFragment);
        it.putExtra("usuario",usuario);
        startActivity(it);
    }

    private void startActivityMain(){
        Intent it = new Intent(getActivity(), MainActivity.class);
        it.putExtra("usuario",usuario);
        startActivity(it);
       getActivity().finish();
    }

    private void removerUsuarioLocal(){
        UsuarioDao dao = new UsuarioDao(getActivity());
        dao.deletar();
    }

    private void inserirUsuarioLocal(){
        UsuarioDao dao = new UsuarioDao(getActivity());
        dao.inserir(usuario,"S");

    }





















}
