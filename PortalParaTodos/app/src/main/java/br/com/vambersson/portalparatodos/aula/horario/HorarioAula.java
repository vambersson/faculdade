package br.com.vambersson.portalparatodos.aula.horario;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.aula.disciplina.ListaDisciplinasUsuario;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;

import static android.app.Activity.RESULT_OK;
import static java.lang.Thread.sleep;

/**
 * Created by Vambersson on 03/06/2017.
 */

public class HorarioAula extends Fragment {

    private static final int REQUEST_DICIPLINA = 10;


    private String disciplina_selecionada = "";

    private Usuario usuario;

    private Button btn_aula1;
    private TextView tv_dia;


    public static HorarioAula newInstance(String title) {

        HorarioAula fragment = new HorarioAula();

        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
        }else{
            usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("StateUsuario",usuario);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.horario_aulas,container,false);

        btn_aula1 = (Button) view.findViewById(R.id.btn_aula1);
        tv_dia = (TextView) view.findViewById(R.id.tv_dia);

        tv_dia.setText(getArguments().getString("title"));

        btn_aula1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disciplinaUsuario();
            }
        });


        return view;
    }

    private void disciplinaUsuario(){

        Intent it = new Intent(getActivity(), ListaDisciplinasUsuario.class);
        it.putExtra(ListaDisciplinasUsuario.EXTRA_USUARIO ,usuario);
        startActivityForResult(it,REQUEST_DICIPLINA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        disciplina_selecionada = data.getStringExtra(ListaDisciplinasUsuario.EXTRA_RESULTADO);
        btn_aula1.setText("okoko");

        if(requestCode == REQUEST_DICIPLINA && resultCode == RESULT_OK){


        }


    }
}
