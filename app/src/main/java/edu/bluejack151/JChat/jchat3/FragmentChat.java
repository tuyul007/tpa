package edu.bluejack151.JChat.jchat3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

/**
 * Created by komputer on 12/22/2015.
 */
public class FragmentChat extends android.support.v4.app.Fragment {



    //klo mw tambahin chat dari siapa2 aja bisa lsg tambahin di array list nya
    ArrayList<String> listChat=new ArrayList<String>();
    ArrayList<String> listFromName=new ArrayList<String>();
    ArrayList<String> listChatNotif=new ArrayList<String>();
    ArrayList<Integer> listImageProfile=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragmentchat, container, false);

        //ini buat ngedit2 isi chat nya doang, klo edit nama pengirimnya blm ada
        listChat.add("wewew1");
        listChat.add("wewew2");
        listChat.add("wewew3");
        listChat.add("wewew4");
        listChat.add("wewew5");
        listChat.add("wewew6");

        listImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);
        listImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);
        listImageProfile.add(R.drawable.com_facebook_button_like_icon_selected);
        listImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);
        listImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);
        listImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);

        listFromName.add("anjing1");
        listFromName.add("anjing2");
        listFromName.add("anjing3");
        listFromName.add("EHMmmm");
        listFromName.add("ZZZZ");
        listFromName.add("faazzzzzzz");


        listChatNotif.add("1");
        listChatNotif.add("1");
        listChatNotif.add("1");
        listChatNotif.add("1");
        listChatNotif.add("1");
        listChatNotif.add("1");

        chatAdapter chatAdapt=new chatAdapter(getActivity(),listFromName,listChat,listChatNotif,listImageProfile);

        ListView lv = (ListView) view.findViewById(R.id.listViewChat);
        lv.setAdapter(chatAdapt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Chat With " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
//        HomeActivity.hideMenuItem();
        return view;
    }

}