package edu.bluejack151.JChat.jchat3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.shiperus.ark.jchat3.R;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatListItem;
import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatViewAdapter;
import edu.bluejack151.JChat.jchat3.AdapterHelper.GroupChatViewAdapter;
import edu.bluejack151.JChat.jchat3.Helper.Chat;
import edu.bluejack151.JChat.jchat3.Helper.Friend;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.GroupNotif;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

public class GroupChatActivity extends AppCompatActivity {

    public static ChatListItem listChat;
    public static HashMap<String,UserAccount> groupMember;
    public static GroupChatViewAdapter adapter;
    Button btnSend ;
    Button viewGroup;
    ListView listView;
    EditText fieldMsg ;
    public static Boolean set = false;

    void initComponent(){
        btnSend = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listView);
        fieldMsg = (EditText)findViewById(R.id.editText);
        viewGroup = (Button)findViewById(R.id.viewGroupButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        HomeActivity.chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                HomeActivity.userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot userSnapshot) {
                        HomeActivity.groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot groupSnapshot) {
                                initComponent();
                                setTitle("[GROUP] " + listChat.getGroup().getGroupName());
                                listChat.setListChat(new ArrayList<Chat>());
                                groupMember = new HashMap<>();
                                HomeActivity.chatRef.child("IN_" + listChat.getGroup().getGroupId() + "_" + HomeActivity.userSessionAccount.getUserId())
                                        .setValue(new Chat(HomeActivity.userSessionAccount.getUserId()
                                                , ""
                                                , listChat.getGroup().getGroupId(), 0, "", 0));

                                for(DataSnapshot gs : groupSnapshot.getChildren()){
                                    GroupIdentity gi = gs.getValue(GroupIdentity.class);
                                    if(gi.getGroupId().equals(listChat.getGroup().getGroupId()) && gi.getAccept() == 1){
                                        groupMember.put(gi.getUserId(),userSnapshot.child(gi.getUserId()).getValue(UserAccount.class));
                                    }
                                }
                                if (HomeActivity.chatList.get(listChat.getGroup().getGroupId()) != null) {
                                    HomeActivity.chatList.get(listChat.getGroup().getGroupId()).setNotifCount(0);
                                    FragmentChat.updateView();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Chat c = ds.getValue(Chat.class);
                                        if (!c.getMessage().equals(""))
                                            if(c.getGroupId().equals(listChat.getGroup().getGroupId())){
                                                HomeActivity.groupNotifRef.child(c.getTimeStamp()+"_"+HomeActivity.userSessionAccount.getUserId()).removeValue();
                                                listChat.setLastChat(c);
                                            }
                                    }
                                }
                                adapter = new GroupChatViewAdapter(getApplicationContext(), listChat.getListChat(), groupMember);
                                listView.setAdapter(adapter);
                                set = true;

                                viewGroup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getApplicationContext(), ViewGroupChat.class);
                                        startActivity(i);
                                    }
                                });

                                btnSend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (fieldMsg.getText().toString().length() > 0) {
                                            HomeActivity.chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Chat c = new Chat(HomeActivity.userSessionAccount.getUserId(),
                                                            "",
                                                            listChat.getGroup().getGroupId(),
                                                            0,
                                                            fieldMsg.getText().toString(),
                                                            new Date().getTime());

                                                    for(Map.Entry<String,UserAccount> data : groupMember.entrySet()){
                                                        UserAccount user = data.getValue();
                                                        if(!user.getUserId().equals(HomeActivity.userSessionAccount.getUserId()))
                                                            if (!dataSnapshot.hasChild("IN_" + listChat.getGroup().getGroupId() + "_" + user.getUserId())) {
                                                                HomeActivity.groupNotifRef.child(c.getTimeStamp()+"_"+user.getUserId()).setValue(
                                                                        new GroupNotif(listChat.getGroup().getGroupId(), user.getUserId())
                                                                );
                                                            }
                                                    }

                                                    listChat.setLastChat(c);
                                                    adapter.notifyDataSetChanged();

                                                    HomeActivity.chatRef.child(c.getTimeStamp() + "").setValue(c);
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
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        }

        );
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeActivity.chatRef.child("IN_" + listChat.getGroup().getGroupId() + "_" + HomeActivity.userSessionAccount.getUserId())
                .removeValue();
        set = false;
        finish();
    }
}