package br.com.vambersson.portalparatodos.fragment.cadastros;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Usuario;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class FragmentCadastroProfessor extends Fragment {

    // CodigoFragment = 002

    private static final int TIRAR_FOTO = 100;

    private Usuario usuario;

    private ImageView ImgV_Idusuario;
    private FloatingActionButton login_cad_IdCamera;

    private TextView perfil_tv_IdNone_faculdade;
    private TextView perfil_tv_IdNome;
    private TextView perfil_tv_IdCurso;

    private EditText perfil_Edt_IdNome;
    private EditText perfil_Edt_IdEmail;
    private EditText perfil_Edt_IdSenha;
    private EditText perfil_Edt_IdSenhaConfirma;

    public FragmentCadastroProfessor(){
        usuario = new Usuario();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
