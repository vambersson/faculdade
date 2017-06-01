package br.com.vambersson.portalparatodos.fragment.cadastros;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.activity.ListaDisciplina;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.dao.UsuarioDao;
import br.com.vambersson.portalparatodos.main.MainActivity;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Vambersson on 19/05/2017.
 */

public class FragmentCadastroAluno extends android.support.v4.app.Fragment {

    private static final int REQUEST_FOTO = 100;
    private static final int REQUEST_DICIPLINA = 101;
    private static final String STATE_DISCIPLINA = "disciplina";

    private boolean save_alterar;

    private String disciplinas_selecionadas;
    private  String result_curso;

    private Usuario usuario;
    private  byte fotoBytes[];
    private  Gson gson;

    private ImageView ImgV_Idusuario;
    private FloatingActionButton login_cad_IdCamera;

    private TextView perfil_tv_IdNone_faculdade;
    private TextView perfil_tv_IdNome;
    private TextView perfil_tv_IdCurso;

    private EditText perfil_Edt_IdNome;

    private Spinner spinner;
    private ArrayAdapter<String> adp_spinner;
    private List<Curso> listaCursos;

    private EditText perfil_Edt_IdEmail;
    private EditText perfil_Edt_IdSenha;
    private EditText perfil_Edt_IdSenhaConfirma;

    private Button btn_IdCancel;
    private Button btn_IdSave;
    private Button perfil_btn_Idlista_disciplina;


    public FragmentCadastroAluno(){
        gson = new Gson();
        listaCursos = new ArrayList<Curso>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");

            disciplinas_selecionadas = savedInstanceState.getString(STATE_DISCIPLINA);
            perfil_btn_Idlista_disciplina.setText(disciplinas_selecionadas);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_DISCIPLINA,disciplinas_selecionadas);
        outState.putSerializable("StateUsuario",usuario);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.usuario_cadastro_aluno_fragment,container,false);

        ImgV_Idusuario = (ImageView) view.findViewById(R.id.ImgV_Idusuario);
        login_cad_IdCamera =(FloatingActionButton) view.findViewById(R.id.login_cad_IdCamera);

        perfil_tv_IdNone_faculdade = (TextView) view.findViewById(R.id.perfil_tv_IdNone_faculdade);
        perfil_tv_IdNome = (TextView) view.findViewById(R.id.perfil_tv_IdNome);
        perfil_tv_IdCurso = (TextView) view.findViewById(R.id.perfil_tv_IdCurso);


        perfil_Edt_IdNome = (EditText) view.findViewById(R.id.perfil_Edt_IdNome);


        spinner = (Spinner) view.findViewById(R.id.perfil_Sp_IdCurso);
        adp_spinner = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item);
        adp_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp_spinner);
        adp_spinner.add(getResources().getString(R.string.cad_spinner_seleciona_Cursos));
        spinner.setOnTouchListener(Spinner_OnTouch);

        perfil_btn_Idlista_disciplina = (Button) view.findViewById(R.id.perfil_btn_Idlista_disciplina);
        perfil_btn_Idlista_disciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaDisciplina();
            }
        });


        perfil_Edt_IdEmail = (EditText) view.findViewById(R.id.perfil_Edt_IdEmail);
        perfil_Edt_IdSenha = (EditText) view.findViewById(R.id.perfil_Edt_IdSenha);
        perfil_Edt_IdSenhaConfirma = (EditText) view.findViewById(R.id.perfil_Edt_IdSenhaConfirma);

        btn_IdCancel = (Button) view.findViewById(R.id.btn_IdCancel);
        btn_IdCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_IdSave = (Button) view.findViewById(R.id.btn_IdSave);
        btn_IdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(save_alterar == true ){
                    save();
                }else{
                    Toast.makeText(getActivity(), "alterar", Toast.LENGTH_SHORT).show();
                }


            }
        });

        login_cad_IdCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarCamera();
            }
        });

        perfil_Edt_IdNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

//                if (event.getAction() == KeyEvent. ){
//                    Toast.makeText(getActivity(), "okok", Toast.LENGTH_SHORT).show();
//
//                }

                return false;

            }
        });

        perfil_Edt_IdNome.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId ==  EditorInfo.IME_ACTION_NEXT){
                    perfil_tv_IdNome.setText(perfil_Edt_IdNome.getText().toString().trim());
                    perfil_tv_IdNome.setVisibility(View.VISIBLE);

                }
                return false;
            }
        });


        if(getActivity().getIntent().getSerializableExtra("usuario") != null){
            usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
            perfil_tv_IdNone_faculdade.setText(usuario.getFaculdade().getNome());
            save_alterar = true;

        }else if(getActivity().getIntent().getSerializableExtra("usuarioAlterar") != null ){
            usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuarioAlterar");
            save_alterar = false;
            Alterar();
        }

        return view;
    }



    private void Alterar(){

        if(usuario != null){

            ImgV_Idusuario.setImageBitmap(byteToBitmap(usuario.getFoto()));
            perfil_tv_IdNone_faculdade.setText(usuario.getFaculdade().getNome());
            perfil_tv_IdNome.setText(usuario.getNome());
            perfil_Edt_IdNome.setText(usuario.getNome());

        }

    }

    private Bitmap byteToBitmap(byte[] outImage){
        try{
            ByteArrayInputStream imageStream= new ByteArrayInputStream(outImage);
            Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
            return imageBitmap;
        }catch (Exception e){
            return null;
        }

    }


    private View.OnTouchListener Spinner_OnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                new ClasseListaCursos().execute(usuario);
                perfil_btn_Idlista_disciplina.setText(R.string.cad_btn_aluno_Discipinas);
            }
            return false;
        }
    };


    private void save(){

        if(validarSave() == true){
            dadosSave();
            new ClasseSave().execute(usuario);
        }

    }

    private boolean validarSave(){
        boolean resultado = true;

        if(perfil_Edt_IdNome.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_nome), Toast.LENGTH_SHORT).show();
            return resultado = false;
        }else if(perfil_Edt_IdEmail.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), "E-Mail inválido", Toast.LENGTH_SHORT).show();
            return resultado = false;
        }else if(!perfil_Edt_IdSenha.getText().toString().trim().equals(perfil_Edt_IdSenhaConfirma.getText().toString().trim())  ){
            Toast.makeText(getActivity(), "Senha inválido", Toast.LENGTH_SHORT).show();
            return resultado = false;
        }else if(spinner.getSelectedItem().toString().equals(getResources().getString(R.string.cad_spinner_seleciona_Cursos))){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_curso), Toast.LENGTH_SHORT).show();
            return resultado = false;
        }else if(perfil_btn_Idlista_disciplina.getText().toString().equals(R.string.cad_btn_aluno_Discipinas)){
            Toast.makeText(getActivity(), "Disciplina Inválido", Toast.LENGTH_SHORT).show();
            return resultado = false;
        }

            return  resultado;
    }

    private void dadosSave(){

        usuario.setFoto(fotoBytes);
        usuario.setNome(perfil_Edt_IdNome.getText().toString().trim());
        usuario.setEmail(perfil_Edt_IdEmail.getText().toString().trim());
        usuario.setSenha(perfil_Edt_IdSenha.getText().toString().trim());
        usuario.getCurso().setCodigo(listaCursos.get(spinner.getSelectedItemPosition()).getCodigo());
        usuario.setDisciplinaSelecionadas(disciplinas_selecionadas);
    }


    class ClasseSave extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {
            String obj ="";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("atualizaUsuario","POST",true);

                OutputStream out = conexao.getOutputStream();

                out.write(gson.toJson(params[0]).getBytes());
                out.flush();
                out.close();

                if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conexao.getInputStream();
                    obj = NetworkUtil.streamToString(is);
                    conexao.disconnect();
                }

                return obj;

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            removerUsuarioLocal();
            inserirUsuarioLocal();
            startActivityMain(); // Activity = Principal


        }
    }

    private void bitmapToByte(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        fotoBytes = stream.toByteArray();

    }

    private void startActivityMain(){
        Intent it = new Intent(getActivity(), MainActivity.class);
        it.putExtra("usuario",usuario);
        startActivity(it);
        getActivity().finishAffinity();
    }

    private void removerUsuarioLocal(){
        UsuarioDao dao = new UsuarioDao(getActivity());
        dao.deletar();
    }

    private void inserirUsuarioLocal(){
        UsuarioDao dao = new UsuarioDao(getActivity());
        dao.inserir(usuario,"S");

    }

    private void listaDisciplina(){

        if(spinner.getSelectedItem().toString().equals(getResources().getString(R.string.cad_spinner_seleciona_Cursos))){
            Toast.makeText(getActivity(),getResources().getString(R.string.message_seleciona_disciplina) , Toast.LENGTH_SHORT).show();

        }else{

            if(!listaCursos.get(spinner.getSelectedItemPosition()).getCodigo().toString().equals(result_curso )){
                disciplinas_selecionadas = "";
            }

            Intent it = new Intent(getActivity(), ListaDisciplina.class);
            it.putExtra(ListaDisciplina.EXTRA_DISCIPLINA,disciplinas_selecionadas);
            it.putExtra(ListaDisciplina.EXTRA_ID_CURSO,listaCursos.get(spinner.getSelectedItemPosition()).getCodigo().toString());
            it.putExtra(ListaDisciplina.EXTRA_ID_FACULDADE,usuario.getFaculdade().getCodigo().toString());
            startActivityForResult(it,REQUEST_DICIPLINA);

        }


    }




    class ClasseListaCursos extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("listaCursos="+params[0].getFaculdade().getCodigo(),"GET",false);

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

            }else if("null".equals(result)){

                //Toast.makeText(getActivity(),"Usuário não encontrado", Toast.LENGTH_LONG).show();

            }else if(!"".equals(result)){

                try{

                    Curso[] lista =  gson.fromJson(result, Curso[].class);

                    List<Curso>  temp = new ArrayList<Curso>(Arrays.asList(lista) );
                    listaCursos.clear();
                    listaCursos = temp;
                    adp_spinner.clear();
                    for (int i = 0;i < temp.size();i++){
                        adp_spinner.add(temp.get(i).getNome());
                    }
                    adp_spinner.setNotifyOnChange(true);

                }catch(Exception e){
                    Toast.makeText(getActivity(), "Erro de comunicação", Toast.LENGTH_SHORT).show();
                }

            }









        }

    }




    private void chamarCamera(){

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (it.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(it, REQUEST_FOTO);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_DICIPLINA && resultCode == RESULT_OK){

            result_curso = data.getStringExtra("codigoCursoSelecionado");
            disciplinas_selecionadas = data.getStringExtra(ListaDisciplina.EXTRA_RESULTADO);

            if(disciplinas_selecionadas != null){
                perfil_btn_Idlista_disciplina.setText(disciplinas_selecionadas);
            }

        }



        if (requestCode == REQUEST_FOTO  && resultCode == RESULT_OK) {
                if(data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ImgV_Idusuario.setImageBitmap(bitmap);

                    bitmapToByte(bitmap);

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getActivity().getBaseContext(), "A captura foi cancelada",
                            Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "A câmera foi fechada",
                            Toast.LENGTH_SHORT);
                }
        }












    }
}
