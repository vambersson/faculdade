package br.com.vambersson.portalparatodos.fragment.cadastros;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Curso;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Vambersson on 19/05/2017.
 */

public class FragmentCadastroUsuario extends android.support.v4.app.Fragment {
    // CodigoFragment = 001


    private static final int TIRAR_FOTO = 100;

    private Usuario usuario;
    private  List<String> stlistaCursos;
    private  Gson gson;

    private ImageView ImgV_Idusuario;
    private FloatingActionButton login_cad_IdCamera;

    private TextView perfil_tv_IdNone_faculdade;
    private TextView perfil_tv_IdNome;
    private TextView perfil_tv_IdCurso;

    private EditText perfil_Edt_IdNome;

    private Spinner perfil_Sp_IdCurso;
    private ArrayAdapter<String> adp_spinner;



    private EditText perfil_Edt_IdEmail;
    private EditText perfil_Edt_IdSenha;
    private EditText perfil_Edt_IdSenhaConfirma;

    private Button btn_IdCancel;
    private Button btn_IdSave;


    public FragmentCadastroUsuario(){
        usuario = new Usuario();
        stlistaCursos = new ArrayList<String>();
        gson = new Gson();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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



        listaCursos();

        perfil_Sp_IdCurso = (Spinner) view.findViewById(R.id.perfil_Sp_IdCurso);
        adp_spinner = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,stlistaCursos);
        adp_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        perfil_Sp_IdCurso.setAdapter(adp_spinner);

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
                save();
            }
        });

        login_cad_IdCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarCamera();
            }
        });

        usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        if(usuario != null){
            perfil_tv_IdNone_faculdade.setText(usuario.getFaculdade().getNome());
        }

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


        return view;
    }

    private void listaCursos(){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                new ClasseListaCursos().execute();
            }
        });

        t.start();
    }


    private void save(){

        if(validarSave() == true){

        }

    }

    private boolean validarSave(){
        boolean resultado = true;

        if(perfil_Edt_IdNome.getText().toString().trim().equals(null)){
            Toast.makeText(getActivity(), "Nome inválido", Toast.LENGTH_SHORT).show();
            return resultado = false;
        }

            return  resultado;
    }

    class ClasseListaCursos extends AsyncTask<Usuario, Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(Usuario... params) {

            HttpURLConnection conexao = null;
            String obj = "";

            try {

                conexao = NetworkUtil.conectar("GET","listaCursos="+usuario.getFaculdade().getCodigo());

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

            List<Curso> temp = new ArrayList<Curso>();
            Curso[] lista =  gson.fromJson(result, Curso[].class);

            temp = new ArrayList<Curso>(Arrays.asList(lista) );

            for (int i = 0;i < temp.size();i++){
                stlistaCursos.add(temp.get(i).getNome());
            }
            adp_spinner.setNotifyOnChange(true);

        }

    }




    private void chamarCamera(){

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (it.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(it, TIRAR_FOTO);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TIRAR_FOTO) {
            if (resultCode == RESULT_OK) {
                if(data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ImgV_Idusuario.setImageBitmap(bitmap);

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
}
