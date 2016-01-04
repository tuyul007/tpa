package edu.bluejack151.JChat.jchat3;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Map;

import edu.bluejack151.JChat.jchat3.AdapterHelper.DialogGroupInviteAdapter;
import edu.bluejack151.JChat.jchat3.AdapterHelper.InvitedFriendPageAdapter;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

public class ViewGroupChat extends AppCompatActivity {
    Dialog dialog;
    ArrayList<UserAccount> friendList;
    ArrayList<Boolean> checked;
    DialogGroupInviteAdapter dialogInviteAdapter;
    InvitedFriendPageAdapter invitedAdapter;
    ListView lv;
    ListView listInvitedFriendS;
    ArrayList<UserAccount> listInvitedFriend;


    EditText fieldGroupName;
    Button createGroup;
    Button cancel;

    void initComponent() {
        fieldGroupName = (EditText) findViewById(R.id.inputGroupName);
        fieldGroupName.setText(GroupChatActivity.listChat.getGroup().getGroupName());
        fieldGroupName.setEnabled(false);
        createGroup = (Button) findViewById(R.id.buttonCreateGroup);
        cancel = (Button) findViewById(R.id.buttonCancelGroup);

        createGroup.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

        friendList = new ArrayList<>();
        checked = new ArrayList<>();
        initFriendList();

        listInvitedFriend = new ArrayList<>();
        initGroupList();
        listInvitedFriendS = (ListView) findViewById(R.id.listInvitedFriend);
        invitedAdapter = new InvitedFriendPageAdapter(this, listInvitedFriend);
        listInvitedFriendS.setAdapter(invitedAdapter);
    }

    void initGroupList(){
        for(Map.Entry<String,UserAccount> data :  GroupChatActivity.groupMember.entrySet()){
            listInvitedFriend.add(data.getValue());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTitle("View Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initModalDialog();
        initComponent();

    }

    public void initModalDialog() {
        dialog = new Dialog(ViewGroupChat.this);

        dialog.setTitle("Select Your Friend");
        dialog.setContentView(R.layout.group_invite_modal);
        dialog.setCanceledOnTouchOutside(true);
    }


    void sendInvited() {
        for (int i = 0; i < friendList.size(); i++) {
            if (checked.get(i)) {
                HomeActivity.groupRef.child(GroupChatActivity.listChat.getGroup().getGroupId()+"_"+friendList.get(i).getUserId()).setValue(
                        new GroupIdentity(
                                GroupChatActivity.listChat.getGroup().getGroupId(),
                                GroupChatActivity.listChat.getGroup().getGroupName(),
                                friendList.get(i).getUserId(),
                                0
                        )
                );
            }
        }
    }

    public void confirmInvitation(View v) {
        sendInvited();
        dialog.dismiss();
    }

    void initFriendList() {
        for (int i = 0; i < HomeActivity.tempFriendList.get(1).getFriendList().size(); i++) {
            UserAccount user = HomeActivity.tempFriendList.get(1).getFriendList().get(i).getFriendDetail();
            if(GroupChatActivity.groupMember.get(user.getUserId())==null)friendList.add(user);
            checked.add(false);
        }
    }

    public void inviteDialog(View v) {
        dialogInviteAdapter = new DialogGroupInviteAdapter(this, friendList, checked);
        lv = (ListView) dialog.findViewById(R.id.listDialogInviteFriend);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(dialogInviteAdapter);


        dialog.show();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
