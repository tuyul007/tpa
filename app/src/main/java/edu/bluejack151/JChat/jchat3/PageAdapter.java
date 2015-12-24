package edu.bluejack151.JChat.jchat3;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.support.v7.widget.Toolbar;

import com.shiperus.ark.jchat3.R;


/**
 * Created by komputer on 12/22/2015.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    public PageAdapter(FragmentManager fm, int NumOfTabs,Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context=context;

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
                FragmentProfile tab3 = new FragmentProfile();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    private int[] imagesHeaderTab={R.drawable.ic_friend_icon,R.drawable.ic_chat_icon,R.drawable.ic_friend_icon};
    private String tabTitles[] = new String[] { "Friend", "Chat","Profile" };
    @Override
    public CharSequence getPageTitle(int position) {



        Drawable image = context.getResources().getDrawable(imagesHeaderTab[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString(" \n" + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
//
//        switch (position) {
//            case 0:
//                return  "Friend";
//            case 1:
//                return  "Chat";
//            case 2:
//                return  "Profile";
//            default:
//                return null;
//        }
    }
}
