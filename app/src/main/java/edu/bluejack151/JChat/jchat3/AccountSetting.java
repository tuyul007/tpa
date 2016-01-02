package edu.bluejack151.JChat.jchat3;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

public class AccountSetting extends AppCompatActivity {

    Switch switchNotif;
    CheckBox publicUser;
    Button clearChat;
    Button viewHelp;
    Boolean switchCheck;
    Boolean publicCheck;


    void initComponent(){
        switchNotif = (Switch)findViewById(R.id.notifSwitchSetting);
        publicUser = (CheckBox)findViewById(R.id.publicSettingCbox);
        clearChat = (Button)findViewById(R.id.clearChatHistoryButton);
        viewHelp = (Button)findViewById(R.id.viewHelpButton);

        switchCheck = false;
        publicCheck = false;

        if(HomeActivity.userSessionAccount.getNotification() == 1){
            switchNotif.setChecked(true);
            switchCheck = true;
        }
        if(HomeActivity.userSessionAccount.getIsPublic() == 1){
            publicUser.setChecked(true);
            publicCheck =true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        this.setTitle("Account Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initComponent();

        switchNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCheck = !switchCheck;

                switchNotif.setChecked(switchCheck);
                if (switchCheck) {
                    HomeActivity.userSessionAccount.setNotification(1);
                } else {
                    HomeActivity.userSessionAccount.setNotification(0);
                }

                HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);
            }
        });

        publicUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicCheck = !publicCheck;

                publicUser.setChecked(publicCheck);

                if(publicCheck){
                    HomeActivity.userSessionAccount.setIsPublic(1);
                }else{
                    HomeActivity.userSessionAccount.setIsPublic(0);
                }
                HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);
            }
        });

        clearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountSetting.this, "This feature is not avalaible now", Toast.LENGTH_SHORT).show();
            }
        });

        viewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountSetting.this, "This feature is not avalaible now", Toast.LENGTH_SHORT).show();
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

}
