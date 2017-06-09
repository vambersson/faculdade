package br.com.vambersson.portalparatodos.aula.dias;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.aula.disciplina.ListaDisciplinasUsuario;
import br.com.vambersson.portalparatodos.base.Disciplina;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static android.app.Activity.RESULT_OK;
import static java.lang.Thread.sleep;

/**
 * Created by Vambersson on 05/06/2017.
 */

public class DiaFragmentTerca extends Fragment {

    private static final int REQUEST_DISCIPLINA = 103;
    private final int NUMERO_DIA = 3;

    private Button btn_aula1;
    private Button btn_aula2;
    private Button btn_aula3;
    private Button btn_aula4;

    private TextView tv_dia;

    private Usuario usuario;
    private Disciplina disciplina;
    private List<Disciplina> listaDisciplina;

    private Thread thread;
    private String tiposAlteracaoAula = "";

    private  int ordem_selecao = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaDisciplina = new ArrayList<Disciplina>();
        usuario = new Usuario();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("StateUsuario",usuario);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dia_terca_fragment,container,false);

        btn_aula1 = (Button) view.findViewById(R.id.btn_aula1);
        btn_aula2 = (Button) view.findViewById(R.id.btn_aula2);
        btn_aula3 = (Button) view.findViewById(R.id.btn_aula3);
        btn_aula4 = (Button) view.findViewById(R.id.btn_aula4);

        tv_dia = (TextView) view.findViewById(R.id.tv_dia);

        btn_aula1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usuario.getTipo().equals("P")){
                    ordem_selecao = 1;
                    disciplinaUsuario();
                }
            }
        });
        btn_aula2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usuario.getTipo().equals("P")){
                    ordem_selecao = 2;
                    disciplinaUsuario();
                }
            }
        });
        btn_aula3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usuario.getTipo().equals("P")){
                    ordem_selecao = 3;
                    disciplinaUsuario();
                }

            }
        });
        btn_aula4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usuario.getTipo().equals("P")){
                    ordem_selecao = 4;
                    disciplinaUsuario();
                }

            }
        });

        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
        }else{
            usuario = (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
            carregarDisciplinas();
        }

        return view;
    }

    private void btn_Texto_padeao(){

        btn_aula1.setText(R.string.horario_selecione_disciplina);
        btn_aula2.setText(R.string.horario_selecione_disciplina);
        btn_aula3.setText(R.string.horario_selecione_disciplina);
        btn_aula4.setText(R.string.horario_selecione_disciplina);
    }

    private void resultadoOrdemSelecao(String nome,int numero){

        switch (numero){
            case 1:
                btn_aula1.setText(nome);
                break;
            case 2:
                btn_aula2.setText(nome);
                break;
            case 3:
                btn_aula3.setText(nome);
                break;
            case 4:
                btn_aula4.setText(nome);
                break;
            default:
                break;
        }



    }

    private void salvardisciplina(Disciplina dis){

        dis.setSelecionou("S");
        dis.setOrdem(ordem_selecao);
        dis.setNumerodia(NUMERO_DIA);
        usuario.getCurso().setCodigo(dis.getCurso().getCodigo());
        usuario.setDisciplina(dis);
        usuario.setFoto(null);

        new ClasseSavarDisciplinaSelecionada().execute(usuario);
        Snackbar.make(getView(), "Salvando...", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

    }

    private void carregarDisciplinas(){

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){

                    new ClasseListaDisciplinasDasAulas().execute(usuario);

                    try {
                        sleep(40000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        thread.start();

    }

    class ClasseSavarDisciplinaSelecionada extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {
            String obj ="";
            Gson gson = new Gson();
            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("usuarioSelecionouD","POST",true);

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

            if(result.equals("1")){

                if(tiposAlteracaoAula.equals("update")){
                    Snackbar.make(getView(), "Disciplina Atualizada com sucesso!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }else  if(tiposAlteracaoAula.equals("insert")){
                    Snackbar.make(getView(), "Disciplina salva com sucesso!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                }
                carregarDisciplinas();
            }

        }

    }

    private void disciplinaUsuario(){

        Intent it = new Intent(getActivity(), ListaDisciplinasUsuario.class);

        for(int i=0;i< listaDisciplina.size();i++){

            if(ordem_selecao == listaDisciplina.get(i).getOrdem()){
                it.putExtra(ListaDisciplinasUsuario.EXTRA_DISCIPLINA_TROCA,listaDisciplina.get(i).getCodigo().toString());
            }

        }


        it.putExtra(ListaDisciplinasUsuario.EXTRA_USUARIO ,usuario);
        startActivityForResult(it,REQUEST_DISCIPLINA);

    }

    class ClasseListaDisciplinasDasAulas extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {

            String obj = "";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("disciplinasDasAulas="+params[0].getFaculdade().getCodigo() +"=" +params[0].getCodigo()+"="+NUMERO_DIA+"="+params[0].getTipo(),"GET",false);

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


            if("[]".equals(result)){
                btn_Texto_padeao();
            }else if(!"".equals(result)){
                Gson gson = new Gson();
                try{

                    Disciplina[] lista =  gson.fromJson(result, Disciplina[].class);

                    List<Disciplina> temp = new ArrayList<Disciplina>(Arrays.asList(lista) );

                    if(usuario.getTipo().equals("A") && !listaDisciplina.equals(null)){

                        if(temp.size() != listaDisciplina.size()){
                            notificacaoAgendaAula();
                        }else if(temp.size() == listaDisciplina.size()){
                            for(int i=0;i < temp.size();i++ ){
                                if(temp.get(i).getOrdem() == listaDisciplina.get(i).getOrdem() && temp.get(i).getCodigo() != listaDisciplina.get(i).getOrdem() ){
                                    notificacaoAgendaAula();
                                }else
                                if(temp.get(i).getOrdem() != listaDisciplina.get(i).getOrdem() && temp.get(i).getCodigo() != listaDisciplina.get(i).getOrdem() ){
                                    notificacaoAgendaAula();
                                }

                            }
                        }
                    }

                    listaDisciplina = temp;
                    btn_Texto_padeao();
                    for(int i = 0; i < temp.size(); i++){

                        resultadoOrdemSelecao(temp.get(i).getNome(),temp.get(i).getOrdem());
                    }

//                    INTERROPE A THREAD O USUÁRIO FOR PROFESSOR
                    if(usuario.getTipo().equals("P")){
                        thread.isInterrupted();
                    }

                }catch(Exception e){
                    //Toast.makeText(getActivity(), R.string.message_alerta_webservice, Toast.LENGTH_SHORT).show();
                }

            }









        }

    }

    private void removerAula(){

        for(int i=0;i< listaDisciplina.size();i++){
            if(ordem_selecao == listaDisciplina.get(i).getOrdem()){
                usuario.setDisciplina(listaDisciplina.get(i));
            }
        }

        new ClasseRemoverdaAula().execute(usuario);
    }

    class ClasseRemoverdaAula extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {
            String obj ="";
            Gson gson = new Gson();
            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("removerDisciplinaSelecionadaAula","POST",true);

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

            if(result.equals("1")){

                if(tiposAlteracaoAula.equals("update")){

                    salvardisciplina(disciplina);

                }else  if(tiposAlteracaoAula.equals("delete")){

                    carregarDisciplinas();
                    resultadoOrdemSelecao(getResources().getString(R.string.horario_selecione_disciplina),ordem_selecao);
                    Snackbar.make(getView(), "Disciplina removida com sucesso!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                    tiposAlteracaoAula = "";
                }

            }

        }

    }

    private void tiposAlteracao(String tipo,Disciplina dis){
        switch (tipo){

            case "insert":
                salvardisciplina(dis);
                break;
            case "delete":
                removerAula();
                break;
            case "update":
                removerAula();
                break;
            default:
                break;

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_DISCIPLINA && resultCode == RESULT_OK){

            disciplina = new Disciplina();
            tiposAlteracaoAula = data.getStringExtra("tipo");
            disciplina = (Disciplina) data.getSerializableExtra(ListaDisciplinasUsuario.EXTRA_RESULTADO);

            tiposAlteracao(tiposAlteracaoAula,disciplina);

        }





    }

    private void notificacaoAgendaAula(){

//        só notifica com o app aberto

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Portal Acadêmico")
                .setContentText("Teve auteração na sua agenda de aula de ");

        Intent resultIntent = new Intent();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(300, mBuilder.build());

    }
}
