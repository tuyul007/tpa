package edu.bluejack151.JChat.jchat3.AdapterHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

/**
 * Created by shiperus on 12/29/2015.
 */
public class InvitedFriendPageAdapter extends ArrayAdapter {

    private Activity context;
    private ArrayList<UserAccount> userAccounts;

    public InvitedFriendPageAdapter(Activity context,ArrayList<UserAccount> userAccounts) {
        super(context, R.layout.group_friend_invite_modal_list_friend, userAccounts);

        this.context=context;
        this.userAccounts = userAccounts;
    }
    UserAccount getData(int position){
        return userAccounts.get(position);
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.invited_group_friend, null, true);

        TextView txtNameInvited = (TextView) rowView.findViewById(R.id.nameInvitedGroup);

        ImageView profileInvitedImage = (ImageView) rowView.findViewById(R.id.imageInvitedGroup);

        txtNameInvited.setText(getData(position).getDisplayName());

        if(!getData(position).getProfilePicture().equals("")){
            byte[] imageAsBytes = Base64.decode(getData(position).getProfilePicture(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            profileInvitedImage.setImageBitmap(bitmap);
        }


        return rowView;

    };

}
