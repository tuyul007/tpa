package edu.bluejack151.JChat.jchat3;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

public class AccountSetting extends AppCompatActivity {

    Switch switchNotif;
    CheckBox publicUser;


    void initComponent(){
        switchNotif = (Switch)findViewById(R.id.notifSwitchSetting);
        publicUser = (CheckBox)findViewById(R.id.publicSettingCbox);

        if(HomeActivity.userSessionAccount.getNotification() == 1)switchNotif.setChecked(true);
        if(HomeActivity.userSessionAccount.getIsPublic() == 1)publicUser.setChecked(true);
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
                switchNotif.setChecked(!switchNotif.isChecked());
                Toast.makeText(AccountSetting.this, switchNotif.isChecked()+"", Toast.LENGTH_SHORT).show();
                if(switchNotif.isChecked()){
                    HomeActivity.userSessionAccount.setNotification(1);
                }else{
                    HomeActivity.userSessionAccount.setNotification(0);
                }

                HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);
            }
        });

        publicUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicUser.setChecked(!publicUser.isChecked());
                Toast.makeText(AccountSetting.this, publicUser.isChecked()+"", Toast.LENGTH_SHORT).show();
                if(publicUser.isChecked()){
                    HomeActivity.userSessionAccount.setIsPublic(1);
                }else{
                    HomeActivity.userSessionAccount.setIsPublic(0);
                }
                HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);
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
