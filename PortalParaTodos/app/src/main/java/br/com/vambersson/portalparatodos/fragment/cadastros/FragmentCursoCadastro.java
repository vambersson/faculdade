package br.com.vambersson.portalparatodos.fragment.cadastros;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.com.vambersson.portalparatodos.R;

/**
 * Created by Vambersson on 23/05/2017.
 */

public class FragmentCursoCadastro extends DialogFragment {

    private static final String DIALOG_TAG = "addcurso";

    private EditText edt_IdNome_Curso;
    private Button btn_IdCancel_Curso;
    private Button btn_IdSave_Curso;


    public static FragmentCursoCadastro newInstancia(){
        FragmentCursoCadastro fragemnt = new FragmentCursoCadastro();
        return fragemnt;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.curso_cadastro_fragment,container,false);

        edt_IdNome_Curso = (EditText) view.findViewById(R.id.edt_IdNome_Curso);
        btn_IdCancel_Curso = (Button) view.findViewById(R.id.btn_IdCancel_Curso);
        btn_IdSave_Curso = (Button) view.findViewById(R.id.btn_IdSave_Curso);

        return view;
    }

    public void abrir(FragmentManager fm){
        show(fm,DIALOG_TAG);
    }


}
