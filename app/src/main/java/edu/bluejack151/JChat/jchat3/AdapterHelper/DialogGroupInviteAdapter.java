package edu.bluejack151.JChat.jchat3.AdapterHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

/**
 * Created by shiperus on 12/27/2015.
 */
public class DialogGroupInviteAdapter extends ArrayAdapter{

    private Activity context;
    private ArrayList<UserAccount> userAccounts;
    private ArrayList<Boolean> checked;

    public DialogGroupInviteAdapter(Activity context,ArrayList<UserAccount> userAccounts,ArrayList<Boolean>checked) {
        super(context, R.layout.group_friend_invite_modal_list_friend, userAccounts);

        this.context=context;
        this.userAccounts = userAccounts;
        this.checked = checked;
    }

    UserAccount getData(int position){
        return userAccounts.get(position);
    }

    public View getView(final int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.group_friend_invite_modal_list_friend, null, true);

        TextView txtNameInviteGroupDialog = (TextView) rowView.findViewById(R.id.groupInviteDialogName);

        ImageView profileInviteGroupDialogImage = (ImageView) rowView.findViewById(R.id.profileGroupInvitePictureDialog);
        final CheckedTextView chkTxtView=(CheckedTextView)rowView.findViewById(R.id.chkInviteFriend);


        txtNameInviteGroupDialog.setText(getData(position).getDisplayName());

        if(!getData(position).getProfilePicture().equals("")){
            byte[] imageAsBytes = Base64.decode(getData(position).getProfilePicture(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            profileInviteGroupDialogImage.setImageBitmap(bitmap);
        }

        chkTxtView.setChecked(checked.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkTxtView.setChecked(!chkTxtView.isChecked());
                if(chkTxtView.isChecked()==true) {
                    checked.set(position,true);
                } else if(chkTxtView.isChecked()==false) {
                    checked.set(position,false);
                }
            }
        });


        return rowView;

    };
}
