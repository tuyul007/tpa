package edu.bluejack151.JChat.jchat3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;
import java.util.List;

public class AddFriend extends AppCompatActivity {


    //buat munculin list orang yang ke search bisa lsg tambahin di array nya
    ArrayList<String> listSearchName=new ArrayList<String>();
    ArrayList<Integer> listSearchImageProfile=new ArrayList<>();


    ImageButton imgBtnSearch;

    ListView listAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setTitle("Add Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listSearchName.add("wewew1");
        listSearchName.add("wewew2");
        listSearchName.add("wewew3");
        listSearchName.add("wewew4");
        listSearchName.add("wewew5");
        listSearchName.add("wewew6");

        listSearchImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);
        listSearchImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);
        listSearchImageProfile.add(R.drawable.com_facebook_button_like_icon_selected);
        listSearchImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);
        listSearchImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);
        listSearchImageProfile.add(R.drawable.com_facebook_profile_picture_blank_portrait);




        AddFriendAdapter chatAdapt=new AddFriendAdapter(this,listSearchName,listSearchImageProfile);


       final ListView lv = (ListView)findViewById(R.id.listSearchFriend);
        lv.setAdapter(chatAdapt);


        //ini buat tambah temen rencanya bisa 2 cara, bisa di klik list view nya nanti muncul dialog, sama klik button nanti dia lsg add
        //cmn yang klik button msh in progress
        imgBtnSearch=(ImageButton)findViewById(R.id.searchFriendButton);

        imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Tambah Teman",Toast.LENGTH_SHORT).show();
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();

        return true;
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }


    public void tesSearchButton()
    {
        Toast.makeText(getApplicationContext(),"Tambah Teman",Toast.LENGTH_SHORT).show();
    }
}
