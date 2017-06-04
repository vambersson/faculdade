package br.com.vambersson.portalparatodos.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentCadastroAluno;
import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentCursoLista;
import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentDisciplinaLista;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class FragPagerAdapter extends FragmentPagerAdapter {

    private String[] tabsNome;

    public FragPagerAdapter(FragmentManager fm, String[] tabsNome) {
        super(fm);
        this.tabsNome = tabsNome;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return FragmentCadastroAluno.getInstancia();
            case 1:
                return FragmentCursoLista.getInstancia();
            case 2:
                return FragmentDisciplinaLista.getInstancia();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return    this.tabsNome[position];
    }


}
