package br.com.vambersson.portalparatodos.aula.dias;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
import br.com.vambersson.portalparatodos.dao.UsuarioDao;
import br.com.vambersson.portalparatodos.fragment.gerenciador.GerenciadorFragment;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static android.app.Activity.RESULT_OK;
import static java.lang.Thread.sleep;

/**
 * Created by Vambersson on 05/06/2017.
 */

public class DiaFragmentQuinta extends Fragment {

    private static final int REQUEST_DISCIPLINA = 105;
    private final int NUMERO_DIA = 5;

    private Button btn_aula1;
    private Button btn_aula2;
    private Button btn_aula3;
    private Button btn_aula4;

    private TextView tv_dia;

    private Usuario usuario;
    private Disciplina disciplina;
    private List<Disciplina> listaDisciplina;

    private Thread thread;
    private boolean threadAtiva = false;
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
        View view = inflater.inflate(R.layout.dia_quinta_fragment,container,false);

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
            carregarDisciplinasLocal();
        }

        return view;
    }


    private void btn_Texto_padrao(){

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

    }

    private void salvarDisciplinasLocal(List<Disciplina> lista){

        UsuarioDao dao = new UsuarioDao(getActivity());

        btn_Texto_padrao();

        for(int i=0;i < lista.size();i++){

            dao.inserirDisciplinas(lista.get(i));

            resultadoOrdemSelecao(lista.get(i).getNome(),lista.get(i).getOrdem());

        }

        listaDisciplina = lista;

    }

    private void salvarDisciplinaLocal(Disciplina dis){

        UsuarioDao dao = new UsuarioDao(getActivity());

        btn_Texto_padrao();

        dao.inserirDisciplinas(dis);

        carregarDisciplinasLocal();

    }

    private void removeDisciplinasLocal(){
        UsuarioDao dao = new UsuarioDao(getActivity());
        dao.deletarDisciplinas(NUMERO_DIA);

    }

    private void removeDisciplinaLocal(Disciplina dis){
        UsuarioDao dao = new UsuarioDao(getActivity());
        dao.deletarDisciplinas(dis);
    }

    private void carregarDisciplinasLocal(){
        UsuarioDao dao = new UsuarioDao(getActivity());

        List<Disciplina> lista =  dao.getDisciplinas(NUMERO_DIA);

        if(lista != null){

            for(int i=0;i < lista.size();i++){

                resultadoOrdemSelecao(lista.get(i).getNome(), lista.get(i).getOrdem() );

            }

            listaDisciplina = lista;

        }

        if(threadAtiva == false){
            carregarDisciplinas();
            thread.start();
        }

    }

    private void carregarDisciplinas(){

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){

                    new ClasseListaDisciplinasDasAulas().execute(usuario);
                    threadAtiva = true;
                    try {
                        sleep(40000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

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
                    Snackbar.make(getView(), getResources().getString(R.string.message_alerta_disciplina_Updating_Successfully), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }else  if(tiposAlteracaoAula.equals("insert")){
                    Snackbar.make(getView(),getResources().getString(R.string.message_alerta_disciplina_Saving_successfully), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
                salvarDisciplinaLocal(usuario.getDisciplina());

            }

        }

    }

    private void disciplinaUsuario(){

        Intent it = new Intent(getActivity(), ListaDisciplinasUsuario.class);


        if( (!btn_aula1.getText().toString().equals(getResources().getString(R.string.horario_selecione_disciplina)))  ){

            for(int i=0;i< listaDisciplina.size();i++){

                if(ordem_selecao == listaDisciplina.get(i).getOrdem()){
                    it.putExtra(ListaDisciplinasUsuario.EXTRA_DISCIPLINA_TROCA,listaDisciplina.get(i).getCodigo().toString());

                }

            }

        }
        if( (!btn_aula2.getText().toString().equals(getResources().getString(R.string.horario_selecione_disciplina)))  ){

            for(int i=0;i< listaDisciplina.size();i++){

                if(ordem_selecao == listaDisciplina.get(i).getOrdem()){
                    it.putExtra(ListaDisciplinasUsuario.EXTRA_DISCIPLINA_TROCA,listaDisciplina.get(i).getCodigo().toString());

                }

            }

        }
        if( (!btn_aula3.getText().toString().equals(getResources().getString(R.string.horario_selecione_disciplina)))  ){

            for(int i=0;i< listaDisciplina.size();i++){

                if(ordem_selecao == listaDisciplina.get(i).getOrdem()){
                    it.putExtra(ListaDisciplinasUsuario.EXTRA_DISCIPLINA_TROCA,listaDisciplina.get(i).getCodigo().toString());

                }

            }

        }
        if( (!btn_aula4.getText().toString().equals(getResources().getString(R.string.horario_selecione_disciplina)))  ){

            for(int i=0;i< listaDisciplina.size();i++){

                if(ordem_selecao == listaDisciplina.get(i).getOrdem()){
                    it.putExtra(ListaDisciplinasUsuario.EXTRA_DISCIPLINA_TROCA,listaDisciplina.get(i).getCodigo().toString());

                }

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

                if(listaDisciplina.size() != 0){

                    UsuarioDao dao = new UsuarioDao(getActivity());
                    if(!dao.getDisciplinas(NUMERO_DIA).equals(null)){
                        removeDisciplinasLocal();

                        btn_Texto_padrao();

                        //notifica o aluno da auteração
                        if(usuario.getTipo().equals("A")){
                            notificacaoAgendaAula();
                        }
                    }

                }

            }else if(!"".equals(result)){
                Gson gson = new Gson();
                try{

                    Disciplina[] lista =  gson.fromJson(result, Disciplina[].class);

                    List<Disciplina> temp = new ArrayList<Disciplina>(Arrays.asList(lista) );

                    if(listaDisciplina.size() != 0){

                        if(temp.size() != listaDisciplina.size()){

                            removeDisciplinasLocal();

                            salvarDisciplinasLocal(temp);

                            //notifica o aluno da auteração
                            if(usuario.getTipo().equals("A")){
                                notificacaoAgendaAula();
                            }
                        }else if(temp.size() == listaDisciplina.size()){

                            for(int i=0;i < temp.size();i++ ){

                                if(temp.get(i).getOrdem() == listaDisciplina.get(i).getOrdem() && temp.get(i).getCodigo() != listaDisciplina.get(i).getCodigo() ){

                                    removeDisciplinasLocal();

                                    salvarDisciplinasLocal(temp);

                                    //notifica o aluno da auteração
                                    if(usuario.getTipo().equals("A")){
                                        notificacaoAgendaAula();
                                    }

                                }

                            }

                        }

                    }else if(listaDisciplina.size() == 0){

                        salvarDisciplinasLocal(temp);

                        //notifica o aluno da auteração
                        if(usuario.getTipo().equals("A")){
                            notificacaoAgendaAula();
                        }

                    }

//                    INTERROPE A THREAD O USUÁRIO FOR PROFESSOR
                    if(usuario.getTipo().equals("P")){
                        thread.isInterrupted();
                    }

                }catch(Exception e){
                    //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

                    removeDisciplinaLocal(usuario.getDisciplina());

                    salvardisciplina(disciplina);

                }else  if(tiposAlteracaoAula.equals("delete")){

                    removeDisciplinaLocal(usuario.getDisciplina());

                    carregarDisciplinasLocal();

                    resultadoOrdemSelecao(getResources().getString(R.string.horario_selecione_disciplina),ordem_selecao);
                    Snackbar.make(getView(),getResources().getString(R.string.message_alerta_disciplina_Removing_Successfully), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    tiposAlteracaoAula = "";
                }

            }

        }

    }

    private void tiposAlteracao(String tipo,Disciplina dis){
        switch (tipo){

            case "insert":
                salvardisciplina(dis);
                Snackbar.make(getView(), getResources().getString(R.string.message_alerta_disciplina_Saving), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                break;
            case "delete":
                removerAula();
                Snackbar.make(getView(), getResources().getString(R.string.message_alerta_disciplina_Removing), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                break;
            case "update":
                removerAula();
                Snackbar.make(getView(), getResources().getString(R.string.message_alerta_disciplina_Updating), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icportal) )
                .setContentTitle("Portal Acadêmico")
                .setContentText("Teve auteração na sua agenda de aula de "+ getResources().getString(R.string.dia_tv_quinta));

        Intent resultIntent = new Intent(getActivity(), GerenciadorFragment.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(305, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        Notification notification = mBuilder.build();
        notification.vibrate = new long[]{150,300,150,300};

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(305, notification);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(getActivity(),som);
            toque.play();

        }catch (Exception e){

        }


    }

}
