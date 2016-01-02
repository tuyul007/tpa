package edu.bluejack151.JChat.jchat3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatListItem;
import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatViewAdapter;
import edu.bluejack151.JChat.jchat3.Helper.Chat;
import edu.bluejack151.JChat.jchat3.Helper.Friend;

public class PrivateChatActivity extends AppCompatActivity {

    public static ChatListItem listChat;
    public static ChatViewAdapter adapter;
    Button btnSend ;
    ListView listView;
    EditText fieldMsg ;
    public static Boolean set = false;

    void initComponent(){
        btnSend = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listView);
        fieldMsg = (EditText)findViewById(R.id.editText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
        HomeActivity.chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                            initComponent();
                            setTitle(listChat.getUser().getDisplayName());
                            listChat.setListChat(new ArrayList<Chat>());
                            HomeActivity.chatRef.child("IN_" + HomeActivity.userSessionAccount.getUserId() + "_" + listChat.getUser().getUserId())
                                    .setValue(new Chat(HomeActivity.userSessionAccount.getUserId()
                                            , listChat.getUser().getUserId()
                                            , "", 0, "", 0));
                            if(HomeActivity.chatList.get(listChat.getUser().getUserId())!=null) {
                                HomeActivity.chatList.get(listChat.getUser().getUserId()).setNotifCount(0);
                                FragmentChat.updateView();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Chat c = ds.getValue(Chat.class);
                                    if(!c.getMessage().equals(""))
                                        if ((c.getFromId().equals(listChat.getUser().getUserId()) && c.getToId().equals(HomeActivity.userSessionAccount.getUserId()))
                                                || (c.getFromId().equals(HomeActivity.userSessionAccount.getUserId()) && c.getToId().equals(listChat.getUser().getUserId()))) {
                                            if(!c.getFromId().equals(HomeActivity.userSessionAccount.getUserId()))
                                                c.setPrivateStatus(1);

                                            listChat.setLastChat(c);
                                            HomeActivity.chatRef.child(ds.getKey()).child("privateStatus").setValue(c.getPrivateStatus());
                                        }
                                }
                            }
                            adapter = new ChatViewAdapter(getApplicationContext(),listChat.getListChat(),listChat.getUser());
                            listView.setAdapter(adapter);
                            set = true;

                            btnSend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(fieldMsg.getText().toString().length()>0){
                                        HomeActivity.chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Chat c = new Chat(HomeActivity.userSessionAccount.getUserId(),
                                                        listChat.getUser().getUserId(),
                                                        "",
                                                        0,
                                                        fieldMsg.getText().toString(),
                                                        new Date().getTime());
                                                if(dataSnapshot.hasChild("IN_" +listChat.getUser().getUserId() + "_" + HomeActivity.userSessionAccount.getUserId() )){
                                                    c.setPrivateStatus(1);
                                                }
                                                listChat.setLastChat(c);
                                                adapter.notifyDataSetChanged();

                                                HomeActivity.chatRef.child(c.getTimeStamp()+"").setValue(c);
                                                fieldMsg.setText("");
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                                    }
                                }
                            });
                }

                @Override
                public void onCancelled (FirebaseError firebaseError){

                }
            }

            );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeActivity.chatRef.child("IN_" + HomeActivity.userSessionAccount.getUserId() + "_" + listChat.getUser().getUserId())
                .removeValue();
        set = false;
        finish();
    }
}
