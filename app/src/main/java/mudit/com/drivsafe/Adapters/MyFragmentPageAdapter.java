package mudit.com.drivsafe.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mudit.com.drivsafe.Fragments.AnalysisFragment;
import mudit.com.drivsafe.Fragments.MapsFragment;
import mudit.com.drivsafe.Fragments.Tyresinfo;


/**
 * Created by admin on 7/7/2017.
 */

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    Fragment[] fragments;

    public MyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        fragments=new Fragment[]{new Tyresinfo(),new AnalysisFragment(),new MapsFragment()};

//        fragments=new Fragment[]{new CameraFragment(),new RecentPostFragment(),new MessageFragment(),new UserProfileFragment()};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
