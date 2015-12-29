package edu.bluejack151.JChat.jchat3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

/**
 * Created by shiperus on 12/27/2015.
 */
public class DialogGroupInviteAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> inviteDialogFriendName=new ArrayList<String>();
    private  ArrayList<Integer> imgInviteDialogFriend=new ArrayList<>();
    public DialogGroupInviteAdapter(Activity context,ArrayList<String> inviteDialogFriendName,ArrayList<Integer> imgInviteDialogFriend) {
        super(context, R.layout.group_friend_invite_modal_list_friend, inviteDialogFriendName);

        this.context=context;

        this.inviteDialogFriendName=inviteDialogFriendName;
        this.imgInviteDialogFriend=imgInviteDialogFriend;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.group_friend_invite_modal_list_friend, null, true);

//        final CheckBox chk=(CheckBox)rowView.findViewById(R.id.checkBoxInviteFriendDialog);
//        chk.setTag(position);
//        chk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, chk.getTag().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        TextView txtNameInviteGroupDialog = (TextView) rowView.findViewById(R.id.groupInviteDialogName);

        ImageView profileInviteGroupDialogImage = (ImageView) rowView.findViewById(R.id.profileGroupInvitePictureDialog);


        txtNameInviteGroupDialog.setText(inviteDialogFriendName.get(position));

        profileInviteGroupDialogImage.setImageResource(imgInviteDialogFriend.get(position));



        return rowView;

    };
}
