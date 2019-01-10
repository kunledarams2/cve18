package com.kunledarams.cve2018.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kunledarams.cve2018.Fragment.Gallery;
import com.kunledarams.cve2018.Fragment.ImageAdder;
import com.kunledarams.cve2018.Fragment.Profile;

/**
 * Created by ok on 10/24/2018.
 */

public class PageHolder  extends FragmentStatePagerAdapter {

    int Tab_num;
    public PageHolder(FragmentManager fm, int tab_num) {
        super(fm);
        Tab_num = tab_num;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Profile profile= new Profile();
                return profile;
            case 1:
                Gallery gallery= new Gallery();
                return gallery;
            case 2 :
                ImageAdder imageAdder= new ImageAdder();
                return imageAdder;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return Tab_num;
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
