package edu.bluejack151.JChat.jchat3;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.AdapterHelper.AddFriendAdapter;
import edu.bluejack151.JChat.jchat3.AdapterHelper.FriendListItem;
import edu.bluejack151.JChat.jchat3.Helper.Friend;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

public class AddFriend extends AppCompatActivity {

    ImageButton imgBtnSearch;
    EditText fieldSearchFriend;
    public static Firebase userRef,friendRef;
    ArrayList<UserAccount> userAccounts;
    ListView listAdd;

    void initComponent(){
        userRef = new Firebase("https://jchatapps.firebaseio.com/user");
        friendRef = new Firebase("https://jchatapps.firebaseio.com/friend");
        fieldSearchFriend = (EditText)findViewById(R.id.inputFriendID);
        imgBtnSearch=(ImageButton)findViewById(R.id.searchFriendButton);
        userAccounts = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initComponent();
        setTitle("Add Friend");

        final AddFriendAdapter chatAdapt=new AddFriendAdapter(this,userAccounts);

        listAdd = (ListView)findViewById(R.id.listSearchFriend);
        listAdd.setAdapter(chatAdapt);

        imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldSearchFriend.getText().toString().length() > 0) {
                    if (fieldSearchFriend.getText().toString().equals(HomeActivity.userSessionAccount.getUserId())) {
                        Toast.makeText(getApplicationContext(), "You cannot add yourself..", Toast.LENGTH_SHORT).show();
                    } else {
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(fieldSearchFriend.getText().toString())) {
                                    final UserAccount a = dataSnapshot.child(fieldSearchFriend.getText().toString()).getValue(UserAccount.class);
                                    if(a.getIsPublic() == 0){
                                        Toast.makeText(getApplicationContext(), "User ID not found", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (a.getUserId().equals(fieldSearchFriend.getText().toString())) {
                                        for (int i = 0; i < HomeActivity.tempFriendList.get(1).getFriendList().size(); i++) {
                                            Friend f = HomeActivity.tempFriendList.get(1).getFriendList().get(i).getFriendIdentity();
                                            if (f.getFriendId().equals(a.getUserId())) {
                                                Toast.makeText(getApplicationContext(), "You already add this User ID", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                        if (userAccounts.size() == 0)
                                            userAccounts.add(new UserAccount());
                                        userAccounts.set(0, a);
                                        chatAdapt.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "User ID not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                }
            }

        });
        listAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                //if (id == R.id.btnAdd) {

                }
            //}
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
