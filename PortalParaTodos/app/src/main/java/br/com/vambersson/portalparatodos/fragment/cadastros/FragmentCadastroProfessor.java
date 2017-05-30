package br.com.vambersson.portalparatodos.fragment.cadastros;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.dao.UsuarioDao;
import br.com.vambersson.portalparatodos.main.MainActivity;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class FragmentCadastroProfessor extends Fragment {

    // CodigoFragment = 002

    private static final int TIRAR_FOTO = 100;

    private Usuario usuario;
    private Gson gson;
    private  byte fotoBytes[];

    private ImageView ImgV_Idusuario;
    private FloatingActionButton login_cad_IdCamera;

    private TextView perfil_tv_IdNone_faculdade;
    private TextView perfil_tv_IdNome;
    private TextView perfil_tv_IdCurso;

    private EditText perfil_Edt_IdNome;
    private EditText perfil_Edt_IdEmail;
    private EditText perfil_Edt_IdSenha;
    private EditText perfil_Edt_IdSenhaConfirma;

    private Button btn_IdSave;
    private Button btn_Idcancel;

    public FragmentCadastroProfessor(){
        gson = new Gson();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
            //byteToBitmap(usuario.getFoto());
        }else{
            usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("StateUsuario",usuario);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.usuario_cadastro_professor_fragment,container,false);

        ImgV_Idusuario = (ImageView) view.findViewById(R.id.ImgV_Idusuario);
        login_cad_IdCamera = (FloatingActionButton) view.findViewById(R.id.login_cad_IdCamera);

        perfil_tv_IdNone_faculdade = (TextView) view.findViewById(R.id.perfil_tv_IdNone_faculdade);
        perfil_tv_IdNome = (TextView) view.findViewById(R.id.perfil_tv_IdNome);
        perfil_tv_IdCurso = (TextView) view.findViewById(R.id.perfil_tv_IdCurso);


        perfil_Edt_IdNome = (EditText) view.findViewById(R.id.perfil_Edt_IdNome);

        perfil_Edt_IdEmail = (EditText) view.findViewById(R.id.perfil_Edt_IdEmail);
        perfil_Edt_IdSenha = (EditText) view.findViewById(R.id.perfil_Edt_IdSenha);
        perfil_Edt_IdSenhaConfirma = (EditText) view.findViewById(R.id.perfil_Edt_IdSenhaConfirma);

        btn_IdSave = (Button) view.findViewById(R.id.btn_IdSave);
        btn_Idcancel = (Button) view.findViewById(R.id.btn_Idcancel);

        login_cad_IdCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarCamera();
            }
        });



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

        btn_IdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTeacher();
            }
        });


        return view;
    }


    private void SaveTeacher(){

        if (validTeacher()==true){
            dataTeacher();
            new ClasseSave().execute(usuario);
        }

    }

    private boolean validTeacher(){

        boolean resultado = true;

        if (perfil_Edt_IdNome.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_nome), Toast.LENGTH_SHORT).show();
            resultado = false;
        }else if (perfil_Edt_IdEmail.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_email), Toast.LENGTH_SHORT).show();
            resultado = false;
        }else if (!perfil_Edt_IdSenha.getText().toString().trim().
                equals(perfil_Edt_IdSenhaConfirma.getText().toString().trim())){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_valida_password), Toast.LENGTH_SHORT).show();
            resultado = false;
        }

        return resultado;
    }

    private void dataTeacher(){

        usuario.setNome(perfil_Edt_IdNome.getText().toString().trim());
        usuario.setEmail(perfil_Edt_IdEmail.getText().toString().trim());
        usuario.setSenha(perfil_Edt_IdSenha.getText().toString().trim());
        usuario.setFoto(fotoBytes);
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            removerUsuarioLocal();
            inserirUsuarioLocal();
            startActivityMain(); // Activity = Principal


        }
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

    private void chamarCamera(){

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (it.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(it, TIRAR_FOTO);
        }

    }

    private void bitmapToByte(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        fotoBytes = stream.toByteArray();

    }

    private void byteToBitmap( byte[] outImage){

        ByteArrayInputStream imageStream= new ByteArrayInputStream(outImage);
        Bitmap imageBitmap= BitmapFactory.decodeStream(imageStream);
        ImgV_Idusuario.setImageBitmap(imageBitmap);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TIRAR_FOTO) {
            if (resultCode == RESULT_OK) {
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









}
