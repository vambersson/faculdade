package br.com.vambersson.portalparatodos.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentCursoLista;
import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentCadastroDisciplina;
import br.com.vambersson.portalparatodos.fragment.cadastros.FragmentCadastroProfessor;

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
                return new FragmentCadastroProfessor();
            case 1:
                return new FragmentCursoLista();
            case 2:
                return new FragmentCadastroDisciplina();
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
