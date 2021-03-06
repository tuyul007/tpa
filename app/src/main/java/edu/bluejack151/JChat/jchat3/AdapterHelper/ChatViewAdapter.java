package edu.bluejack151.JChat.jchat3.AdapterHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shiperus.ark.jchat3.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.bluejack151.JChat.jchat3.Helper.Chat;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;
import edu.bluejack151.JChat.jchat3.HomeActivity;

public class ChatViewAdapter extends ArrayAdapter{

    private ArrayList<Chat> chatViewList;
    private UserAccount userAccount;

    public void setChatViewList(ArrayList<Chat> chatViewList) {
        this.chatViewList = chatViewList;
    }

    public ChatViewAdapter(Context context, ArrayList<Chat> chatViewList,UserAccount userAccount) {
        super(context, R.layout.activity_private_chat, chatViewList);
        this.chatViewList = chatViewList;
        this.userAccount = userAccount;
    }

    Chat getChat(int position){
        return chatViewList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int listViewItemType = 1;
        if(getChat(position).getFromId().equals(HomeActivity.userSessionAccount.getUserId()))listViewItemType = 0;

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

        if (listViewItemType == 1) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.other_chats, null);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.self_chat, null);
        }

        TextView message = (TextView) convertView.findViewById(R.id.message);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateText);
        TextView displayName = (TextView) convertView.findViewById(R.id.nameText);

        displayName.setText(userAccount.getDisplayName());
        if(listViewItemType == 0)displayName.setVisibility(View.GONE);
        message.setText(getChat(position).getMessage());

        date.setTime(getChat(position).getTimeStamp());
        dateView.setText(dateFormat.format(date));



        return convertView;
    }

}