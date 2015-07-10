package amhamogus.com.latsoclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import amhamogus.com.latsoclient.fragments.ProductListFragment;
import amhamogus.com.latsoclient.fragments.ProfileFragment;
import amhamogus.com.latsoclient.fragments.ScanProductFragment;

/**
 * Created by amogus on 7/8/15.
 */
public class LatsoPageAdapter extends FragmentPagerAdapter {

    private static int NUMBER_OF_ITEMS = 3;

    public LatsoPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ProductListFragment.newInstance("sample", "param");

            case 1:
                return ProfileFragment.newInstance("sample","param" );
            case 2:
                //return ProductDetailFragment.newInstance("0002");
                return ScanProductFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Products";
            case 1:
                return "Profile";
            case 2:
                return "Scan";
            default:
                return "default";
        }
    }
}
