package br.com.vambersson.portalparatodos.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.com.vambersson.portalparatodos.R;
import br.com.vambersson.portalparatodos.fragment.adapter.FragPagerAdapter;

import static java.lang.Thread.sleep;

/**
 * Created by Vambersson on 21/05/2017.
 */

public class ActivityPage extends AppCompatActivity{

    private TabLayout tabs_layout;
    public static ViewPager container_ViewPager;

    private FragPagerAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_pages);

        tabs_layout = (TabLayout) findViewById(R.id.tabs_layout);
        container_ViewPager = (ViewPager) findViewById(R.id.container_ViewPager);

        adapter = new FragPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tabs_cad_professor));

        container_ViewPager.setAdapter(adapter);
        tabs_layout.setupWithViewPager(container_ViewPager);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
