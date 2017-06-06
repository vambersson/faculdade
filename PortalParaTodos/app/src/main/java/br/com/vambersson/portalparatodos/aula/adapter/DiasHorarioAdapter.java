package br.com.vambersson.portalparatodos.aula.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.vambersson.portalparatodos.aula.dias.DiaFragmentDomingo;
import br.com.vambersson.portalparatodos.aula.dias.DiaFragmentQuarta;
import br.com.vambersson.portalparatodos.aula.dias.DiaFragmentQuinta;
import br.com.vambersson.portalparatodos.aula.dias.DiaFragmentSabado;
import br.com.vambersson.portalparatodos.aula.dias.DiaFragmentSegunda;
import br.com.vambersson.portalparatodos.aula.dias.DiaFragmentSexta;
import br.com.vambersson.portalparatodos.aula.dias.DiaFragmentTerca;
import br.com.vambersson.portalparatodos.aula.horario.HorarioAula;

/**
 * Created by Vambersson on 04/06/2017.
 */

public class DiasHorarioAdapter extends FragmentPagerAdapter {



    public DiasHorarioAdapter(FragmentManager fm){
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new DiaFragmentDomingo();
            case 1:
                return  new DiaFragmentSegunda();
            case 2:
                return  new DiaFragmentTerca();
            case 3:
                return new DiaFragmentQuarta();
            case 4:
                return new DiaFragmentQuinta();
            case 5:
                return new DiaFragmentSexta();
            case 6:
                return new DiaFragmentSabado();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 7;
    }



}
