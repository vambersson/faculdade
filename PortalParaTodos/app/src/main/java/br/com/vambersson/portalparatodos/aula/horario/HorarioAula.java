package br.com.vambersson.portalparatodos.aula.horario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.aula.disciplina.ListaDisciplinasUsuario;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.dao.UsuarioDao;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Vambersson on 03/06/2017.
 */

public class HorarioAula extends Fragment {

    public static int disciplina_selecionada_codigo = 0;
    public static String disciplina_selecionada ="";

    public static boolean selecionou_disciplina = false;
    private boolean verifica = true;

    private Usuario usuario;
    private  Thread thread;

    private Button btn_aula1;
    private Button btn_aula2;
    private Button btn_aula3;
    private Button btn_aula4;
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
        btn_aula2 = (Button) view.findViewById(R.id.btn_aula2);
        btn_aula3 = (Button) view.findViewById(R.id.btn_aula3);
        btn_aula4 = (Button) view.findViewById(R.id.btn_aula4);

        tv_dia = (TextView) view.findViewById(R.id.tv_dia);
        tv_dia.setText(getArguments().getString("title"));

        btn_aula1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disciplinaUsuario();
            }
        });
        btn_aula2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disciplinaUsuario();
            }
        });
        btn_aula3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disciplinaUsuario();
            }
        });
        btn_aula4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disciplinaUsuario();
            }
        });

        getDisciplinaLocal();
        return view;
    }

    private void disciplinaUsuario(){

        verificaSelecao();

        Intent it = new Intent(getActivity(), ListaDisciplinasUsuario.class);
        it.putExtra(ListaDisciplinasUsuario.EXTRA_USUARIO ,usuario);
        startActivityForResult(it,200);


    }

    private void verificaSelecao(){

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(verifica){

                    if(selecionou_disciplina == true){
                        inserirDisciplinaLocal();
                        selecionou_disciplina = false;
                        verifica = false;
                        thread.interrupt();

                    }

                }

            }
        });
        thread.start();


    }

    private void inserirDisciplinaLocal(){
        UsuarioDao dao = new UsuarioDao(getActivity());

        Disciplina dis = new Disciplina();


        dis.setCodigo(disciplina_selecionada_codigo);
        dis.setNome(disciplina_selecionada);
        dao.inserirDisciplina(dis);

        getDisciplinaLocal();

    }

    private void getDisciplinaLocal(){
        UsuarioDao dao = new UsuarioDao(getActivity());

        Disciplina dis = new Disciplina();

        dis =  dao.getDisciplina();

        if(dis != null){
            btn_aula1.setText(dis.getNome());
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == 200){
            Toast.makeText(getActivity(), "200 okoko", Toast.LENGTH_SHORT).show();
        }



    }
}
