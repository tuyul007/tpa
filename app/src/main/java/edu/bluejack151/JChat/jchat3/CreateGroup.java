package edu.bluejack151.JChat.jchat3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;
import java.util.List;

public class CreateGroup extends AppCompatActivity {

    Dialog dialog;
    ArrayAdapter<String> adapter;
    ListView listView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTitle("Create Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initModalDialog();

//        String[] sports = {"asu","asu2","asu3","benTAI","RickyTai"};
//        listView2=(ListView)
//        adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_multiple_choice, sports);
//        listView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView2.setAdapter(adapter);


    }
    ArrayList<String> listSearchName=new ArrayList<String>();
    ArrayList<Integer> listSearchImageProfile=new ArrayList<>();
    public void initModalDialog()
    {
        dialog = new Dialog(CreateGroup.this);

        dialog.setTitle("Select Your Friend");
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.group_invite_modal);

        dialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                Toast.makeText(getApplicationContext(),String.valueOf(lv.getCount()),Toast.LENGTH_SHORT).show();
            }
        });


//        popUpMenu1 = (Button)dialog.findViewById(R.id.popup_1);
//        popUpMenu2 =  (Button)dialog.findViewById(R.id.popup_2);
    }

     ListView lv;
    ArrayList<String> listInvitedFriend=new ArrayList<String>();


    public void confirmInvitation(View v)
    {
//        for(int i=0;i<listInvitedFriend.size();i++)
//        {
//            int indx=Integer.valueOf(listInvitedFriend.get(i));
//            Toast.makeText(CreateGroup.this, indx+"", Toast.LENGTH_SHORT).show();
//
//            listSearchName.remove(indx);
//            listSearchImageProfile.remove(indx);
//        }
//        Toast.makeText(CreateGroup.this, ""+listSearchImageProfile.size(), Toast.LENGTH_SHORT).show();

        ListView listInvitedFriendS=(ListView) findViewById(R.id.listInvitedFriend);
        listInvitedFriendS.setAdapter(new InvitedFriendPageAdapter(this,listInvitedFriend,listSearchImageProfile));
        dialog.dismiss();



    }

    public void inviteDialog(View v)
    {

        View view = getLayoutInflater().inflate(R.layout.group_invite_modal, null);
        dialog.setContentView(view);


        DialogGroupInviteAdapter dialogInviteAdapter=new DialogGroupInviteAdapter(this,listSearchName,listSearchImageProfile);

//        for()
//        {
//            for()
//            {
//
//            }
//
//        }


         lv= (ListView)dialog.findViewById(R.id.listDialogInviteFriend);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(dialogInviteAdapter);
//        String[] sports = {"asu","asu2","asu3","benTAI","RickyTai"};
//        listView2=(ListView) findViewById(R.id.listDialogInviteFriend);
//        adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_multiple_choice, sports);
//        listView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView2.setAdapter(adapter);




        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                // AdapterView is the parent class of ListView
                ListView lv1 = (ListView) arg0;
                CheckedTextView chkTxtView=(CheckedTextView)lv1.getChildAt(position).findViewById(R.id.chkInviteFriend);
                chkTxtView.setChecked(!chkTxtView.isChecked());

                if(chkTxtView.isChecked()==true)
                {
                    listInvitedFriend.add(listSearchName.get(position));
                }
                else if(chkTxtView.isChecked()==false)
                {
                    listInvitedFriend.remove(listInvitedFriend.indexOf(position+""));


                }
//                Toast.makeText(getBaseContext(), listInvitedFriend.size()+"", Toast.LENGTH_SHORT).show();
//                chk.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getBaseContext(), "Click Ehhh", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                if(lv1.isItemChecked(position)){
//                    Toast.makeText(getBaseContext(), "You checked " + position, Toast.LENGTH_SHORT).show();
//                }else{
//                    lv1.setItemChecked(position,true);
//                    Toast.makeText(getBaseContext(), "You unchecked " + position, Toast.LENGTH_SHORT).show();
//                }
            }
        };

        // Setting the ItemClickEvent listener for the listview
        lv.setOnItemClickListener(itemClickListener);


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
