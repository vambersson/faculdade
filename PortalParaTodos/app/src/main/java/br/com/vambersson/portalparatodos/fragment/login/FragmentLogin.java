package br.com.vambersson.portalparatodos.fragment.login;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Faculdade;
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
    private List<Faculdade> listaFaculdade;

    private ProgressBar progressBar;
    private int contador = 0;


    private Button login_btn_primeiroAcesso;
    private Button login_btn_login;
    private Button login_btn_esqueceu_senha;

    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private EditText login_Id_EdtTxt_Matricula;
    private EditText login_Id_EdtTxt_Senha;



    public FragmentLogin(){
        listaFaculdade = new ArrayList<Faculdade>();
        gson = new Gson();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment,container,false);




        spinner = (Spinner) view.findViewById(R.id.login_spinner_faculdade);
        login_Id_EdtTxt_Matricula = (EditText) view.findViewById(R.id.login_Id_EdtTxt_Matricula);
        login_Id_EdtTxt_Senha = (EditText) view.findViewById(R.id.login_Id_EdtTxt_Senha);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        login_btn_login = (Button) view.findViewById(R.id.login_btn_login);
        login_btn_primeiroAcesso = (Button) view.findViewById(R.id.login_btn_primeiroAcesso);
        login_btn_esqueceu_senha = (Button) view.findViewById(R.id.login_btn_esqueceu_senha);


        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.add(getResources().getString(R.string.login_sp_faculdade));
        spinner.setOnTouchListener(Spinner_OnTouch);

        login_btn_esqueceu_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Esqueceu Foi", Toast.LENGTH_SHORT).show();
            }
        });


        login_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logar();
            }
        });


        login_btn_primeiroAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primeiroAcesso();
            }
        });

        return view;
    }

    private View.OnTouchListener Spinner_OnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                new ClasseListaFaculdades().execute();
            }
            return false;
        }
    };
    class ClasseListaFaculdades extends AsyncTask<Faculdade, Void,String> {

        @Override
        protected String doInBackground(Faculdade... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("listaFaculdades","GET",false);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(is);
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

            if(result == ""){

                //Toast.makeText(getActivity(),"Dados invalídos", Toast.LENGTH_LONG).show();

            }else if("[]".equals(result)){

                //Toast.makeText(getActivity(),"Usuário não encontrado", Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){

                try{

                    Faculdade[] lista =  gson.fromJson(result, Faculdade[].class);

                    List<Faculdade>  temp = new ArrayList<Faculdade>(Arrays.asList(lista) );
                    listaFaculdade.clear();
                    listaFaculdade = temp;
                    adapter.clear();
                    for (int i = 0;i < temp.size();i++){
                        adapter.add(temp.get(i).getNome());
                    }
                    adapter.setNotifyOnChange(true);

                }catch(Exception e){
                    Toast.makeText(getActivity(), getResources().getString(R.string.message_alerta_webservice), Toast.LENGTH_SHORT).show();
                }

            }









        }

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
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_matricula), Toast.LENGTH_SHORT).show();
            resultado = false;
        }else if(login_Id_EdtTxt_Senha.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_senha), Toast.LENGTH_SHORT).show();
            resultado = false;
        }else if(spinner.getSelectedItem().toString().equals(getResources().getString(R.string.login_sp_faculdade))){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_faculdade), Toast.LENGTH_SHORT).show();
            resultado = false;
        }

        return resultado;
    }

    private void dadosLogar(){
        usuario = new Usuario();
        usuario.getFaculdade().setCodigo(listaFaculdade.get(spinner.getSelectedItemPosition()).getCodigo());
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

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("logar="+ usuario.getLogin() + "=" + usuario.getSenha() +"="+usuario.getFaculdade().getCodigo(),"GET",false);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){

                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(is);
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

                        if(user.getTipo().equals("A")){
                            usuario =  user;
                            startFragment("FragmentCadastroAluno"); // Fragment  =  usuario_cadastro_aluno_fragment
                        }else if(user.getTipo().equals("P")){
                            usuario =  user;
                            startFragment("ActivityPage"); // Fragment  =  usuario_cadastro_professor_fragment
                        }

                        Toast.makeText(getActivity(), "Usuário não possui senha", Toast.LENGTH_SHORT).show();

                    }else if(!user.getSenha().equals(usuario.getSenha())){

                        Toast.makeText(getActivity(), "Senha não conferi", Toast.LENGTH_SHORT).show();

                    }else if(user.getSenha().equals(usuario.getSenha())){
                        usuario = user;
                        removerUsuarioLocal();
                        inserirUsuarioLocal();
                        startActivityMain(); // Activity = Principal

                    }

                }catch(Exception e){
                    Toast.makeText(getActivity(), getResources().getString(R.string.message_alerta_webservice), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_matricula), Toast.LENGTH_SHORT).show();
            resultado = false;
        }else if(spinner.getSelectedItem().toString().equals(getResources().getString(R.string.login_sp_faculdade))){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_faculdade), Toast.LENGTH_SHORT).show();
            resultado = false;
        }

        return resultado;
    }

    private void dadosPrimeiroAcesso(){
        usuario = new Usuario();
        usuario.getFaculdade().setCodigo(listaFaculdade.get(spinner.getSelectedItemPosition()).getCodigo());
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

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("logar="+usuario.getLogin() + "=" + 1+"="+ usuario.getFaculdade().getCodigo(),"GET",false);

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(is);
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

                try{


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
                            startFragment("FragmentCadastroAluno"); // Fragment  =  usuario_cadastro_aluno_fragment
                        }else if(user.getTipo().equals("P")){
                            usuario =  user;
                            startFragment("ActivityPage"); // Fragment  =  usuario_cadastro_professor_fragment
                        }


                    }

                }catch (Exception e){
                    Toast.makeText(getActivity(), getResources().getString(R.string.message_alerta_webservice), Toast.LENGTH_SHORT).show();
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
