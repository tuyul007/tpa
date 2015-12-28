package edu.bluejack151.JChat.jchat3.AdapterHelper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.Helper.Chat;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

/**
 * Created by shiperus on 12/27/2015.
 */
public class ChatListAdapter extends ArrayAdapter{

    private Activity context;

    private ArrayList<ChatAdapterItem> chatIdentiyList;

    public ArrayList<ChatAdapterItem> getChatIdentiyList() {
        return chatIdentiyList;
    }

    public void setChatIdentiyList(ArrayList<ChatAdapterItem> chatIdentiyList) {
        this.chatIdentiyList = chatIdentiyList;
    }

    public ChatListAdapter(Activity context, ArrayList<ChatAdapterItem> list) {
        super(context, R.layout.list_chat, list);

        this.context=context;
        this.chatIdentiyList = list;

    }
    public UserAccount getUserAccount(int position){
        return chatIdentiyList.get(position).getUser();
    }
    public String getProfilePicture(int position){
        return chatIdentiyList.get(position).getUser().getProfilePicture();
    }
    public GroupIdentity getGroupIdentity(int position){
        return chatIdentiyList.get(position).getGroup();
    }

    public String getMessage(int position){
        return chatIdentiyList.get(position).getLastChat().getMessage();
    }

    public int getTotalNotif(int position){
        return chatIdentiyList.get(position).getNotifCount();
    }

    @Override
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        if(view == null)view = LayoutInflater.from(getContext()).inflate(R.layout.list_chat, null);

        TextView txtFromName = (TextView) view.findViewById(R.id.chatItemFromName);
        TextView txtChatMsg = (TextView) view.findViewById(R.id.lastChatMsg);
        TextView txtNotif = (TextView) view.findViewById(R.id.chatNotifCount);
        ImageView profileChat = (ImageView) view.findViewById(R.id.profileSenderChat); //kaming sun

        if(chatIdentiyList.get(position).getGroup() == null){
            //private chat
            txtFromName.setText(getUserAccount(position).getDisplayName());
            if(!getProfilePicture(position).equals("")){
                //ganti profile pictures
            }
        }else{
            //groupchat
            txtFromName.setText(getGroupIdentity(position).getGroupName());

        }
        txtChatMsg.setText(getMessage(position));

        if(getTotalNotif(position) == 0){
            txtNotif.setVisibility(View.INVISIBLE);
        }else{
            txtNotif.setVisibility(View.VISIBLE);
            txtNotif.setText(getTotalNotif(position)+"");
        }


        return view;

    };
}
