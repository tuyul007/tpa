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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

    static List<Map.Entry<String,ChatAdapterItem>> entriesSortedByValues(Map<String,ChatAdapterItem> map) {

        List<Map.Entry<String,ChatAdapterItem>> sortedEntries = new ArrayList<Map.Entry<String,ChatAdapterItem>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<String,ChatAdapterItem>>() {
                    @Override
                    public int compare(Map.Entry<String,ChatAdapterItem> e1, Map.Entry<String,ChatAdapterItem> e2) {
                        int compare =
                                (e1.getValue().getLastChat().getTimeStamp()<e2.getValue().getLastChat().getTimeStamp())?1:-1;
                        return compare;
                    }
                }
        );

        return sortedEntries;
    }
    public static void updateView(){
        if(listChatView!=null) {
            listChatView.clear();

            List<Map.Entry<String,ChatAdapterItem>> sortedEntries = entriesSortedByValues(HomeActivity.chatList);

            for (Map.Entry<String, ChatAdapterItem> data : sortedEntries) {
                listChatView.add(data.getValue());
            }
            if(adapter!=null){
                adapter.notifyDataSetChanged();
            }
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
                    PrivateChatActivity.listChat = new ChatListItem();
                    PrivateChatActivity.listChat.setUser(target);

                    Intent i = new Intent(getContext(), PrivateChatActivity.class);
                    startActivity(i);
                } else {
                    //group chat
                }
            }
        });

        return view;
    }


}