package edu.bluejack151.JChat.jchat3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

/**
 * Created by shiperus on 12/29/2015.
 */
public class InvitedFriendPageAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> invitedFriendName=new ArrayList<String>();
    private  ArrayList<Integer> imgInvitedFriend=new ArrayList<>();
    public InvitedFriendPageAdapter(Activity context,ArrayList<String> invitedFriendName,ArrayList<Integer> imgInvitedFriend) {
        super(context, R.layout.group_friend_invite_modal_list_friend, invitedFriendName);

        this.context=context;

        this.invitedFriendName=invitedFriendName;
        this.imgInvitedFriend=imgInvitedFriend;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.invited_group_friend, null, true);

//        final CheckBox chk=(CheckBox)rowView.findViewById(R.id.checkBoxInviteFriendDialog);
//        chk.setTag(position);
//        chk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, chk.getTag().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        TextView txtNameInvited = (TextView) rowView.findViewById(R.id.nameInvitedGroup);

        ImageView profileInvitedImage = (ImageView) rowView.findViewById(R.id.imageInvitedGroup);


        txtNameInvited.setText(invitedFriendName.get(position));

        profileInvitedImage.setImageResource(imgInvitedFriend.get(position));



        return rowView;

    };

}
