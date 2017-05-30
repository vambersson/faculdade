package br.com.vambersson.portalparatodos.fragment.gerenciador;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentCadastroAluno;
import br.com.vambersson.portalparatodos.fragment.login.FragmentLogin;
import br.com.vambersson.portalparatodos.fragment.splash.SplashScreen;

/**
 * Created by Vambersson on 19/05/2017.
 */

public class GerenciadorFragment extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        String CodigoFragment = getIntent().getStringExtra("CodigoFragment");
        if(CodigoFragment == null){
            managerLayout(null);
        }else if(CodigoFragment != null){
            managerLayout(CodigoFragment);
        }

        setContentView(R.layout.activity_content_fragment);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void managerLayout(String CodigoFragment){

          FragmentManager fragmentManager = getSupportFragmentManager();

           FragmentTransaction ft = fragmentManager.beginTransaction();


           if(CodigoFragment == null){

               SplashScreen fragment = new SplashScreen();
               ft.replace(R.id.activity_content_fragment, fragment);

           }else if(CodigoFragment.equals("FragmentCadastroAluno") ){

               FragmentCadastroAluno fragment = new FragmentCadastroAluno();
               ft.replace(R.id.activity_content_fragment, fragment);

           }else if(CodigoFragment.equals("GerenciadorPages") ){

               GerenciadorPages fragment = new GerenciadorPages();
               ft.replace(R.id.activity_content_fragment, fragment);

           }else  if(CodigoFragment.equals("FragmentLogin") ){

               FragmentLogin fragment = new FragmentLogin();

               ft.replace(R.id.activity_content_fragment, fragment);

           }




           ft.commit();

       }




















}
