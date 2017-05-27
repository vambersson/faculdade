package br.com.vambersson.portalparatodos.fragment.splash;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.base.Usuario;
import br.com.vambersson.portalparatodos.dao.UsuarioDao;
import br.com.vambersson.portalparatodos.erro.ConexaoException;
import br.com.vambersson.portalparatodos.fragment.gerenciador.GerenciadorFragment;
import br.com.vambersson.portalparatodos.fragment.login.FragmentLogin;
import br.com.vambersson.portalparatodos.main.MainActivity;
import br.com.vambersson.portalparatodos.util.NetworkUtil;

import static java.lang.Thread.sleep;

/**
 * Created by Vambersson on 22/05/2017.
 */

public class SplashScreen extends Fragment {

    private ProgressBar progressBar;
    private int contador = 0;
    private Usuario usuario;

    public SplashScreen(){
        usuario = new Usuario();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_screen,container,false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while(contador < progressBar.getMax()){
                    try {
                        sleep(50);
                        contador +=2;
                        progressBar.setProgress(contador);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                getUser();

            }
        });

        t.start();

        return view;
    }

    private void getUser(){

        UsuarioDao dao = new UsuarioDao(getActivity());

         usuario = dao.getUsuario();

        if(usuario == null){
            startFragment("FragmentLogin");
        }else {
            startActivityMain();
        }

    }

    private void startActivityMain(){
        Intent it = new Intent(getActivity(), MainActivity.class);
        it.putExtra("usuario",usuario);
        startActivity(it);
        getActivity().finish();
    }

    private void startFragment(String CodigoFragment){
        Intent it = new Intent(getActivity(), GerenciadorFragment.class);
        it.putExtra("CodigoFragment",CodigoFragment);
        startActivity(it);
        getActivity().finish();

    }







}
