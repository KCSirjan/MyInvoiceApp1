package com.example.myinvoice;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;



public class SectionsStatepageAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragments=new ArrayList<>();
    public SectionsStatepageAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fragment){
        this.fragments.add(fragment);
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
