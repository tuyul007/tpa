package edu.bluejack151.JChat.jchat3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;
import java.util.Map;

import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatAdapterItem;
import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatListAdapter;
import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatListItem;
import edu.bluejack151.JChat.jchat3.Helper.Chat;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

/**
 * Created by komputer on 12/22/2015.
 */
public class FragmentChat extends android.support.v4.app.Fragment {


    public static ArrayList<ChatAdapterItem> listChatView;
    public static ChatListAdapter adapter;
    public static ListView lv;
    void toastMsg(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
    void initComponent(View view){

        listChatView = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.listViewChat);
        updateView();
        adapter=new ChatListAdapter(getActivity(),listChatView);
        lv.setAdapter(adapter);

    }
    public static void updateUserNotifCount(String targetId,int count){
        int idx = 0;
        for(int i=0; i<listChatView.size(); i++){
            Chat c = listChatView.get(i).getLastChat();
            if((c.getFromId().equals(targetId) && c.getToId().equals(HomeActivity.userSessionAccount.getUserId()))
                    || (c.getFromId().equals(HomeActivity.userSessionAccount.getUserId()) && c.getToId().equals(targetId))){
                listChatView.get(i).setNotifCount(count);
                break;
            }
        }
        adapter.notifyDataSetChanged();

    }
    public static void updateView(){
        if(listChatView!=null) {
            int idx = 0;
            for (Map.Entry<String, ChatListItem> data : HomeActivity.chatList.entrySet()) {
                if(listChatView.size()-1<idx || listChatView.size()==0){
                    listChatView.add(new ChatAdapterItem());
                    listChatView.get(listChatView.size()-1).setNotifCount(data.getValue().getNotifCount());
                    listChatView.get(listChatView.size()-1).setUser(data.getValue().getUser());
                    listChatView.get(listChatView.size()-1).setGroup(data.getValue().getGroup());
                    listChatView.get(listChatView.size()-1).setLastChat(data.getValue().getLastChat());

                }else{
                    listChatView.get(idx).setNotifCount(data.getValue().getNotifCount());
                    listChatView.get(idx).setUser(data.getValue().getUser());
                    listChatView.get(idx).setGroup(data.getValue().getGroup());
                    listChatView.get(idx).setLastChat(data.getValue().getLastChat());
                }
                idx++;
            }
            if(adapter!=null)adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragmentchat, container, false);

        initComponent(view);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listChatView.get(position).getGroup() == null) {
                    UserAccount target = listChatView.get(position).getUser();
                    if (HomeActivity.chatList.get(target.getUserId()) == null) {
                        PrivateChatActivity.listChat = new ChatListItem();
                        PrivateChatActivity.listChat.setUser(target);
                    } else
                        PrivateChatActivity.listChat = HomeActivity.chatList.get(target.getUserId());

                    Intent i = new Intent(getContext(), PrivateChatActivity.class);
                    startActivity(i);
                }
            }
        });

        return view;
    }


}