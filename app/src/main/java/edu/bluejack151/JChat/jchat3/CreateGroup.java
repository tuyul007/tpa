package edu.bluejack151.JChat.jchat3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.AdapterHelper.DialogGroupInviteAdapter;
import edu.bluejack151.JChat.jchat3.AdapterHelper.InvitedFriendPageAdapter;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

public class CreateGroup extends AppCompatActivity {

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

    void initComponent(){
        fieldGroupName = (EditText)findViewById(R.id.inputGroupName);
        createGroup = (Button)findViewById(R.id.buttonCreateGroup);
        cancel = (Button)findViewById(R.id.buttonCancelGroup);

        friendList = new ArrayList<>();
        checked = new ArrayList<>();
        initFriendList();

        listInvitedFriend=new ArrayList<>();
        listInvitedFriendS=(ListView) findViewById(R.id.listInvitedFriend);
        invitedAdapter = new InvitedFriendPageAdapter(this,listInvitedFriend);
        listInvitedFriendS.setAdapter(invitedAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldGroupName.getText().toString() != null && fieldGroupName.getText().toString().length() < 3) {
                    Toast.makeText(CreateGroup.this, "Group's name length min 3 character", Toast.LENGTH_SHORT).show();
                } else {
                    String groupId = "G" + HomeActivity.userSessionAccount.getTotalGroup() + "_" + HomeActivity.userSessionAccount.getUserId();
                    String groupName = fieldGroupName.getText().toString();
                    String userId = HomeActivity.userSessionAccount.getUserId();

                    HomeActivity.groupRef.child(groupId+"_"+userId).setValue(new GroupIdentity(groupId, groupName, userId, 1));
                    HomeActivity.userSessionAccount.setTotalGroup((HomeActivity.userSessionAccount.getTotalGroup() + 1));
                    HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);

                    for (int i = 0; i < listInvitedFriend.size(); i++) {
                        userId = listInvitedFriend.get(i).getUserId();
                        HomeActivity.groupRef.child(groupId+"_"+userId).setValue(new GroupIdentity(groupId, groupName, userId, 0));
                    }
                    finish();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTitle("Create Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initModalDialog();
        initComponent();

    }
    public void initModalDialog()
    {
        dialog = new Dialog(CreateGroup.this);

        dialog.setTitle("Select Your Friend");
        dialog.setContentView(R.layout.group_invite_modal);
        dialog.setCanceledOnTouchOutside(true);
    }


    void setListInvited(){
        listInvitedFriend.clear();
        for(int i=0; i<friendList.size(); i++){
            if(checked.get(i)){
                listInvitedFriend.add(friendList.get(i));
            }
        }
    }
    public void confirmInvitation(View v)
    {
        setListInvited();
        invitedAdapter.notifyDataSetChanged();
        dialog.dismiss();
    }
    void refreshCheckedStats(){
        if(listInvitedFriend.size()==0){

        }else{
            for(int i=0; i<friendList.size(); i++){
                checked.set(i,false);
            }
            for(int i=0; i<listInvitedFriend.size(); i++){
                if(friendList.contains(listInvitedFriend.get(i))){
                    checked.set(friendList.indexOf(listInvitedFriend.get(i)),true);
                }
            }
        }
    }
    void initFriendList(){
        for(int i=0; i< HomeActivity.tempFriendList.get(1).getFriendList().size(); i++){
            UserAccount user = HomeActivity.tempFriendList.get(1).getFriendList().get(i).getFriendDetail();
            friendList.add(user);
            checked.add(false);
        }
    }
    public void inviteDialog(View v)
    {
        refreshCheckedStats();
        dialogInviteAdapter=new DialogGroupInviteAdapter(this,friendList,checked);
        lv= (ListView)dialog.findViewById(R.id.listDialogInviteFriend);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(dialogInviteAdapter);

//        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
//                // AdapterView is the parent class of ListView
//                CheckedTextView chkTxtView=(CheckedTextView)lv.getTag(position);
//                chkTxtView.setChecked(!chkTxtView.isChecked());
//
//                if(chkTxtView.isChecked()==true) {
//                    checked.set(position,true);
//                } else if(chkTxtView.isChecked()==false) {
//                    checked.set(position,false);
//                }
//            }
//        };
//
//        // Setting the ItemClickEvent listener for the listview
//        lv.setOnItemClickListener(itemClickListener);


        dialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();

        return true;
    }

    public void onBackPressed() {
        this.finish();
    }

    public void invitePeople(View v)
    {
        dialog.dismiss();
    }


}
