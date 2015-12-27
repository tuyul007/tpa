package edu.bluejack151.JChat.jchat3;

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

/**
 * Created by shiperus on 12/27/2015.
 */
public class chatAdapter extends ArrayAdapter<String> {

    private Activity context;
    private  ArrayList<String> chatMsg=new ArrayList<String>();
    private  ArrayList<Integer> imgid=new ArrayList<>();
    private  ArrayList<String> chatFromName=new ArrayList<>();
    private  ArrayList<String> notifChat=new ArrayList<>();
    public chatAdapter(Activity context,ArrayList<String> chatFromName, ArrayList<String> chatMsg,ArrayList<String> notifChat, ArrayList<Integer> imgid) {
        super(context, R.layout.list_chat, chatMsg);

        this.context=context;

        this.chatFromName=chatFromName;
        this.chatMsg=chatMsg;
        this.imgid=imgid;
        this.notifChat=notifChat;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_chat, null, true);

        TextView txtFromName = (TextView) rowView.findViewById(R.id.chatItemFromName);

        TextView txtChatMsg = (TextView) rowView.findViewById(R.id.chatItem);
        TextView txtNotif = (TextView) rowView.findViewById(R.id.notifCount);
        ImageView profileChat = (ImageView) rowView.findViewById(R.id.profileSenderChat);


        txtFromName.setText(chatFromName.get(position));
        txtChatMsg.setText(chatMsg.get(position));
        txtNotif.setText(notifChat.get(position));
        profileChat.setImageResource(imgid.get(position));



        return rowView;

    };
}
