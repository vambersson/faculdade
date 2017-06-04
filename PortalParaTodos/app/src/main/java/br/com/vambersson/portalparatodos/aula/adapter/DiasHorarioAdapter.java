package br.com.vambersson.portalparatodos.aula.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.vambersson.portalparatodos.aula.horario.HorarioAula;

/**
 * Created by Vambersson on 04/06/2017.
 */

public class DiasHorarioAdapter extends FragmentPagerAdapter {

    private String[] tabsNome;

    public DiasHorarioAdapter(FragmentManager fm, String[] tabsNome){
        super(fm);
        this.tabsNome = tabsNome;
    }

    @Override
    public Fragment getItem(int position) {

        return HorarioAula.newInstance(tabsNome[position].toString());
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return tabsNome[position].toString();
            case 1:
                return tabsNome[position].toString();
            case 2:
                return tabsNome[position].toString();
            case 3:
                return tabsNome[position].toString();
            case 4:
                return tabsNome[position].toString();
            default:
                return null;

        }
    }


}
