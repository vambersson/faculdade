package br.com.vambersson.portalparatodos.fragment.gerenciador;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.fragment.adapter.FragPagerAdapter;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class GerenciadorPages extends Fragment {


    private TabLayout tabs_layout;
    private ViewPager container_pages;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_pages,container,false);


        tabs_layout = (TabLayout) view.findViewById(R.id.tabs_layout);
        container_pages = (ViewPager) view.findViewById(R.id.container_pages);

        container_pages.setAdapter(new FragPagerAdapter(getFragmentManager(),  getResources().getStringArray(R.array.tabs_cad_professor)));
        tabs_layout.setupWithViewPager(container_pages);

        return view;
    }
}
