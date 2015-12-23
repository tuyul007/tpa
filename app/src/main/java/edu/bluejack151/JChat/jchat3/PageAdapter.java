package edu.bluejack151.JChat.jchat3;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



/**
 * Created by komputer on 12/22/2015.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentFriend tab1 = new FragmentFriend();
                return  tab1;
            case 1:
                FragmentChat tab2 = new FragmentChat();
                return tab2;
            case 2:
                FragmentFriend tab3 = new FragmentFriend();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return  "Friend";
            case 1:
                return  "Chat";
            case 2:
                return  "Friend";
            default:
                return null;
        }
    }
}
