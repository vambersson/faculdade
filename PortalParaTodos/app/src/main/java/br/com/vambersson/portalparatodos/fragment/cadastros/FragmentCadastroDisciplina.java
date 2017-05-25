package br.com.vambersson.portalparatodos.fragment.cadastros;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.vambersson.portalparatodos.R;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class FragmentCadastroDisciplina extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disciplina_cadastro_fragment,container,false);


        return view;
    }
}
