package edu.bluejack151.JChat.jchat3;

import android.content.Context;
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
import edu.bluejack151.JChat.jchat3.Helper.Chat;

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
        ListView lv = (ListView) view.findViewById(R.id.listViewChat);
        updateView();
        adapter=new ChatListAdapter(getActivity(),listChatView);
        lv.setAdapter(adapter);
    }

    public static void updateView(){
        if(listChatView!=null) {
            int idx = 0;
            for (Map.Entry<String, ChatAdapterItem> data : HomeActivity.chatList.entrySet()) {
                if(listChatView.size()-1<idx || listChatView.size()==0){
                    listChatView.add(data.getValue());
                }else{
                    listChatView.set(idx,data.getValue());
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

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "Chat With " + String.valueOf(position), Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }


}