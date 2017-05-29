package br.com.vambersson.portalparatodos.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.dao.UsuarioDao;
import br.com.vambersson.portalparatodos.fragment.gerenciador.GerenciadorFragment;

public class MainActivity extends AppCompatActivity {

    private Usuario usuario;

    private TextView texto;

    private ImageView img_teste;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        img_teste = (ImageView) findViewById(R.id.img_teste);
        texto = (TextView) findViewById(R.id.act_main_nome_usuario);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if(usuario != null){
           texto.setText(usuario.getNome());
            byteToBitmap();
        }



    }

    private void byteToBitmap(){

        UsuarioDao dao = new UsuarioDao(this);

        byte[] outImage =  dao.getUsuario().getFoto();
        ByteArrayInputStream imageStream= new ByteArrayInputStream(outImage);
        Bitmap imageBitmap= BitmapFactory.decodeStream(imageStream);
        img_teste.setImageBitmap(imageBitmap);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){

        }else if(id == R.id.action_sign_out){
            removerUsuarioLocal();
            startFragment(null);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }



    private void removerUsuarioLocal(){
        UsuarioDao dao = new UsuarioDao(this);
        dao.deletar();
    }

    private void startFragment(String CodigoFragment){
        Intent it = new Intent(this, GerenciadorFragment.class);
        it.putExtra("CodigoFragment",CodigoFragment);
        startActivity(it);
        finish();
    }



}
