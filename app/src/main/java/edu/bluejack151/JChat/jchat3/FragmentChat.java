package edu.bluejack151.JChat.jchat3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

/**
 * Created by komputer on 12/22/2015.
 */
public class FragmentChat extends android.support.v4.app.Fragment {




    ArrayList<String> listItems=new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragmentchat, container, false);

        //ini buat ngedit2 isi chat nya doang, klo edit nama pengirimnya blm ada
        listItems.add("wewew1");
        listItems.add("wewew2");
        listItems.add("wewew3");
        listItems.add("wewew4");
        listItems.add("wewew5");
        listItems.add("wewew6");

        ListView lv = (ListView) view.findViewById(R.id.listViewChat);
        lv.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_chat, R.id.chatItem, listItems));

        return view;
    }

}