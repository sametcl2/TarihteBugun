package com.example.samet.tarihtebugun;

import android.app.SearchManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static Toolbar toolbar;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean mobile=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
        boolean wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

        if(!wifi && !mobile)
            Snackbar.make(findViewById(R.id.main_content),"Internet bağlantınızı kontrol ediniz",Snackbar.LENGTH_LONG).show();
        else
            setupFragment(mViewPager);
    }

    private void setupFragment(ViewPager viewPager){
        SectionsPagerAdapter pagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new Olaylar(),"Events");
        pagerAdapter.addFragment(new Dogumlar(),"Births");
        pagerAdapter.addFragment(new Olumler(),"Deaths");
        viewPager.setAdapter(pagerAdapter);
    }

}
class SectionsPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragmentList=new ArrayList<>();
    ArrayList<String> fragmentTitle=new ArrayList<>();

    public void addFragment(Fragment fragment, String string){
        fragmentList.add(fragment);
        fragmentTitle.add(string);
    }

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
