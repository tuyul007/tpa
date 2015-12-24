package edu.bluejack151.JChat.jchat3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shiperus.ark.jchat3.R;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.support.design.widget.TabLayout;

import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences shrd;

    //BUAT USER SESSION
    SharedPreferences userSessionPreferences;
    SharedPreferences.Editor userSessionEditor;
    UserAccount userSessionAccount;

    public static Bitmap getFacebookProfilePicture(String userID){
        URL imageURL = null;
        Bitmap bitmap=null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }


        return bitmap;
    }

    void initComponent(){
        userSessionPreferences = getSharedPreferences("user_session",MODE_PRIVATE);
        userSessionAccount = new Gson().fromJson(userSessionPreferences.getString("user_session", ""), UserAccount.class);

        Toast.makeText(getApplicationContext(),"Welcome,"+userSessionAccount.getDisplayName(),Toast.LENGTH_SHORT).show();

    }

    static Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shrd=getSharedPreferences(MainActivity.preferencesName, Context.MODE_PRIVATE);

//        Toast.makeText(HomeActivity.this,shrd.getString("userID",null),Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //tes lagi , kemaren2 soalnya null skrg kgk ;__;
        getSupportActionBar().setTitle("Friend");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager=(ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),3,HomeActivity.this));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0:

                        getSupportActionBar().setTitle("Friend");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Chat");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Profile");
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account_setting) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            File f;
            f = new File("/data/data/"+getPackageName()+"/shared_prefs/user_session.xml");
            f.delete();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
