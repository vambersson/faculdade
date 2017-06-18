package br.com.vambersson.portalparatodos.main;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.vambersson.portalparatodos.aula.adapter.DiasHorarioAdapter;
import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.aula.notificacao.ServicoNotificacaoWebApi;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.dao.UsuarioDao;
import br.com.vambersson.portalparatodos.fragment.gerenciador.GerenciadorFragment;
import br.com.vambersson.portalparatodos.map.MapsActivity;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_USER_ALTERAR = "usuarioAlterar";

    private static final int REQUEST_FOTO = 100;
    private  byte fotoBytes[];


    private Usuario usuario;
    private UsuarioDao usuarioDao;
    private Gson gson;


    private TextView header_tv_nome;
    private TextView header_tv_curso_fuculdade;
    private ImageView header_img_foto;

    private ViewPager container_ViewPager;
    private DiasHorarioAdapter adapter;

    private BroadcastReceiver broadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("StateUsuario");
        }else{
            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        }

        usuarioDao = new UsuarioDao(this);
        gson = new Gson();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                broadcastReceiver = new BroadcastReceiver() {@Override
                public void onReceive(Context context, Intent intent) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    boolean enviado = sharedPreferences.getBoolean("enviado", false);
                    if (enviado) {
                        Toast.makeText(MainActivity.this, "O token foi gerado e enviado ao servidor.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Um erro aconteceu ao gerar o token.", Toast.LENGTH_SHORT).show();
                    }
                }};

                if (verificarPlayServices()) {
                    Intent intent = new Intent(MainActivity.this, ServicoNotificacaoWebApi.class);
                    startService(intent);
                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        header_img_foto = (ImageView) headerView.findViewById(R.id.header_img_foto);
        header_tv_nome =(TextView) headerView.findViewById(R.id.header_tv_nome);
        header_tv_curso_fuculdade = (TextView) headerView.findViewById(R.id.header_tv_curso_fuculdade);

        if(usuario != null){

            header_img_foto.setImageBitmap(byteToBitmap(usuario.getFoto()));
            header_tv_nome.setText(usuario.getNome());

            if(usuario.getTipo().equals("A")){
                header_tv_curso_fuculdade.setText(usuario.getCurso().getNome());
            }else if(usuario.getTipo().equals("P")){
                header_tv_curso_fuculdade.setText(usuario.getFaculdade().getNome());
            }


        }


        dias_semana();

    }

    private boolean verificarPlayServices() {

        int codigo = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

            if (codigo!= ConnectionResult.SUCCESS) {

                if (GooglePlayServicesUtil.isUserRecoverableError(codigo)) {
                    GooglePlayServicesUtil.getErrorDialog(codigo, this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(this, "Este dispositivo não permite usar o recurso.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("StateUsuario",usuario);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_sign_out){
            removerUsuarioLocal();
            removeCursoLocal();
            removeDisciplinasLocal();
            startFragment(null);
            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            chamarCamera();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_map) {
            Intent map = new Intent(this, MapsActivity.class);
            startActivity(map);

        } else if (id == R.id.nav_perfil) {

            if(usuario.getTipo().equals("A")){
                startFragment("FragmentCadastroUsuarioAlterar");
            }else if(usuario.getTipo().equals("P")){
                startFragment("ActivityPageAlterar");
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dias_semana(){

        container_ViewPager = (ViewPager) findViewById(R.id.container_ViewPager_main);
        adapter = new DiasHorarioAdapter(getSupportFragmentManager());
        container_ViewPager.setAdapter(adapter);

        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayName = sdf_.format(date);

        container_ViewPager.setCurrentItem(2);



        //tabs_layout.getTabAt(1).select();

        switch (dayName){
            case "domingo":
                container_ViewPager.setCurrentItem(0);
                break;
            case "segunda-feira":
                container_ViewPager.setCurrentItem(1);
                break;
            case "terça-feira":
                container_ViewPager.setCurrentItem(2);
                break;
            case "quarta-feira":
                container_ViewPager.setCurrentItem(3);
                break;
            case "quinta-feira":
                container_ViewPager.setCurrentItem(4);
                break;
            case "sexta-feira":
                container_ViewPager.setCurrentItem(5);
                break;
            case "sábado":
                container_ViewPager.setCurrentItem(6);
                break;
            default:
                break;
        }

        switch (dayName){
            case "Sunday":
                container_ViewPager.setCurrentItem(0);
                break;
            case "Monday":
                container_ViewPager.setCurrentItem(1);
                break;
            case "Tuesday":
                container_ViewPager.setCurrentItem(2);
                break;
            case "Wednesday":
                container_ViewPager.setCurrentItem(3);
                break;
            case "Thursday":
                container_ViewPager.setCurrentItem(4);
                break;
            case "Friday":
                container_ViewPager.setCurrentItem(5);
                break;
            case "Saturday":
                container_ViewPager.setCurrentItem(6);
                break;
            default:
                break;
        }

    }

    class ClasseAtualizarFotoUsuario extends AsyncTask<Usuario, Void,String> {

        @Override
        protected String doInBackground(Usuario... params) {
            String obj ="";

            try {
                HttpURLConnection conexao = NetworkUtil.abrirConexao("atualizarFotoUsuario","POST",true);

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

            try{

                if(result.equals("1")){
                    atualizarFotoLocal();
                }else if(result.equals("")){
                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){

                Toast.makeText(MainActivity.this,R.string.message_alerta_webservice, Toast.LENGTH_SHORT).show();

            }






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

    private void bitmapToByte(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        fotoBytes = stream.toByteArray();

    }

    private void removerUsuarioLocal(){
        usuarioDao.deletar();
    }

    private void removeDisciplinasLocal(){

        UsuarioDao dao = new UsuarioDao(this);
        dao.deletarDisciplinas();

    }

    private void removeCursoLocal(){

        UsuarioDao dao = new UsuarioDao(this);
        dao.deletarCurso();
    }

    private void atualizarFoto(){
        dadosUsuario();
        new ClasseAtualizarFotoUsuario().execute(usuario);
    }

    private void atualizarFotoLocal(){

        usuarioDao.atualizarFoto(usuario);
        Toast.makeText(this, "Foto Atualizada", Toast.LENGTH_SHORT).show();
    }

    private void dadosUsuario(){
        usuario.setFoto(fotoBytes);
    }

    private void startFragment(String CodigoFragment){
        Intent it = new Intent(this, GerenciadorFragment.class);
        it.putExtra("CodigoFragment",CodigoFragment);
        it.putExtra("usuarioAlterar",usuario);
        startActivity(it);

    }

    private void chamarCamera(){

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (it.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(it, REQUEST_FOTO);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_FOTO  && resultCode == RESULT_OK) {
            if(data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                header_img_foto.setImageBitmap(bitmap);
                bitmapToByte(bitmap);
                atualizarFoto();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this.getBaseContext(), "A captura foi cancelada",
                        Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this.getBaseContext(), "A câmera foi fechada",
                        Toast.LENGTH_SHORT);
            }
        }





        super.onActivityResult(requestCode, resultCode, data);

    }
























}
