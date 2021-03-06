package edu.bluejack151.JChat.jchat3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.shiperus.ark.jchat3.R;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import android.support.design.widget.TabLayout;

import org.w3c.dom.Text;

import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatAdapterItem;
import edu.bluejack151.JChat.jchat3.AdapterHelper.ChatListItem;
import edu.bluejack151.JChat.jchat3.AdapterHelper.FriendListItem;
import edu.bluejack151.JChat.jchat3.Helper.GroupNotif;
import edu.bluejack151.JChat.jchat3.AdapterHelper.PageAdapter;
import edu.bluejack151.JChat.jchat3.AdapterHelper.ParentFriendListItem;
import edu.bluejack151.JChat.jchat3.Helper.Chat;
import edu.bluejack151.JChat.jchat3.Helper.Friend;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    void toastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    SharedPreferences shrd;
    public static Firebase groupRef ;
    public static Firebase friendRef;
    public static Firebase userRef ;
    public static Firebase chatRef;
    public static Firebase groupNotifRef ;
    TextView loadingHandler;
    Boolean updateGroup = false,updateUser=false;
    Chat c;

    public static ArrayList<ParentFriendListItem> tempFriendList;
    public static HashMap<String, ChatAdapterItem> chatList;
    public static HashMap<String, UserAccount> friendAccountList;
    public static Integer friendCount = 0, groupCount = 0, userCount = 0, totalGroup = 0;
    Boolean ready;

    //BUAT USER SESSION
    SharedPreferences userSessionPreferences;
    public static UserAccount userSessionAccount;

    public static Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
    void setSession(UserAccount user){
        userSessionPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor userSessionEditor = userSessionPreferences.edit();
        String user_session = new Gson().toJson(user);
        userSessionEditor.putString("user_session", user_session);
        userSessionEditor.commit();
    }
    void initStatic() {
        HomeActivity.tempFriendList = null;
        HomeActivity.friendAccountList = null;
        HomeActivity.chatList = null;
        HomeActivity.friendCount = 0;
        HomeActivity.groupCount = 0;
        HomeActivity.userCount = 0;
        HomeActivity.totalGroup = 0;
    }
    void initDatabase() {

        updateChatHistory();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (userCount == 0) {
                    userCount = (int) dataSnapshot.getChildrenCount();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    friendAccountList.put(ds.getKey(), ds.getValue(UserAccount.class));
                }
                loadingHandler.setText("event_trigger_u");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Friend f = ds.getValue(Friend.class);

                    if (f.getUserId().equals(userSessionAccount.getUserId())) {
                        tempFriendList.get(FragmentFriend.FRIEND).getFriendList().add(new FriendListItem());
                        tempFriendList.get(FragmentFriend.FRIEND).getFriendList().get(
                                tempFriendList.get(FragmentFriend.FRIEND).getFriendList().size() - 1
                        ).setFriendIdentity(f);
                        friendCount++;
                    }
                }

                loadingHandler.setText("event_trigger_f");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    GroupIdentity g = ds.getValue(GroupIdentity.class);
                    if (g.getUserId().equals(userSessionAccount.getUserId())) {
                        tempFriendList.get(FragmentFriend.GROUP).getFriendList().add(new FriendListItem());
                        tempFriendList.get(FragmentFriend.GROUP).getFriendList().get(
                                tempFriendList.get(FragmentFriend.GROUP).getFriendList().size() - 1
                        ).setGroupIdentity(g);
                        if (g.getAccept() == 1) groupCount++;
                    }
                    totalGroup++;
                }
                loadingHandler.setText("event trigger_g");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        checkUpdates();
    }

    void updateChatHistory() {
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot chatSnapshot, final String s) {
                groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot groupSnapshot) {
                        groupNotifRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot notifSnapshot) {
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot userSnapshot) {
                                        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot friendSnapshot) {
                                                final Chat c = chatSnapshot.getValue(Chat.class);
                                                if (c.getTimeStamp() != 0) {
                                                    if (!c.getGroupId().equals("")) {
                                                        if(groupSnapshot.hasChild(c.getGroupId() + "_" + userSessionAccount.getUserId())){
                                                            if (chatList.get(c.getGroupId()) == null) {
                                                                chatList.put(c.getGroupId(), new ChatAdapterItem());
                                                                chatList.get(c.getGroupId()).setGroup(groupSnapshot.child(c.getGroupId() + "_" + userSessionAccount.getUserId()).getValue(GroupIdentity.class));
                                                            }
                                                            chatList.get(c.getGroupId()).setLastChat(c);
                                                            int notif = 0;
                                                            if(notifSnapshot.hasChild(c.getTimeStamp()+"_"+HomeActivity.userSessionAccount.getUserId())){
                                                                MainActivity.showNotif();
                                                            }
                                                            if (notifSnapshot.getChildrenCount() != 0) {
                                                                for (DataSnapshot ds : notifSnapshot.getChildren()) {
                                                                    GroupNotif gn = ds.getValue(GroupNotif.class);
                                                                    if (gn.getGroupId().equals(c.getGroupId()) && gn.getUserId().equals(userSessionAccount.getUserId()))
                                                                        notif++;
                                                                }
                                                            }
                                                            chatList.get(c.getGroupId()).setNotifCount(notif);
                                                            FragmentChat.updateView();
                                                            if (!c.getFromId().equals(HomeActivity.userSessionAccount.getUserId()) && GroupChatActivity.set) {
                                                                GroupChatActivity.listChat.setLastChat(c);
                                                                GroupChatActivity.adapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    } else {
                                                        if (c.getFromId().equals(userSessionAccount.getUserId())
                                                                || c.getToId().equals(userSessionAccount.getUserId())) {

                                                            String friendId = c.getFromId();
                                                            if (c.getFromId().equals(userSessionAccount.getUserId())) {
                                                                friendId = c.getToId();
                                                            }
                                                            if (chatList.get(friendId) == null) {
                                                                chatList.put(friendId, new ChatAdapterItem());
                                                                chatList.get(friendId).setUser(userSnapshot.child(friendId).getValue(UserAccount.class));
                                                            }
                                                            if (friendSnapshot.hasChild(userSessionAccount.getUserId() + "_" + friendId)) {
                                                                Friend f = friendSnapshot.child(userSessionAccount.getUserId() + "_" + friendId).getValue(Friend.class);
                                                                if (f.getBlocked() == 0) {
                                                                    chatList.get(friendId).setLastChat(c);
                                                                    if (!c.getFromId().equals(userSessionAccount.getUserId()) && c.getPrivateStatus() == 0) {
                                                                        //fire event
                                                                        MainActivity.showNotif();
                                                                        chatList.get(friendId).setNotifCount(chatList.get(friendId).getNotifCount() + 1);
                                                                    }
                                                                    FragmentChat.updateView();
                                                                    if(!c.getFromId().equals(HomeActivity.userSessionAccount.getUserId())&& PrivateChatActivity.set){
                                                                        PrivateChatActivity.listChat.setLastChat(c);
                                                                        PrivateChatActivity.adapter.notifyDataSetChanged();
                                                                    }
                                                                } else {
                                                                    chatList.remove(friendId);
                                                                    FragmentChat.updateView();

                                                                }
                                                                return;
                                                            }
                                                            chatList.get(friendId).setLastChat(c);
                                                            if (!c.getFromId().equals(userSessionAccount.getUserId()) && c.getPrivateStatus() == 0) {
                                                                chatList.get(friendId).setNotifCount(chatList.get(friendId).getNotifCount() + 1);
                                                            }

                                                            FragmentChat.updateView();
                                                            if(!c.getFromId().equals(HomeActivity.userSessionAccount.getUserId())&& PrivateChatActivity.set){
                                                                PrivateChatActivity.listChat.setLastChat(c);
                                                                PrivateChatActivity.adapter.notifyDataSetChanged();
                                                            }

                                                        }
                                                    }
                                                }
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
                        });

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    void initComponent() {
        Firebase.setAndroidContext(this);

        groupRef = new Firebase("https://jchatapps.firebaseio.com/group");
        friendRef = new Firebase("https://jchatapps.firebaseio.com/friend");
        userRef = new Firebase("https://jchatapps.firebaseio.com/user");
        chatRef = new Firebase("https://jchatapps.firebaseio.com/chat");
        groupNotifRef = new Firebase("https://jchatapps.firebaseio.com/groupnotif");

        userSessionPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        userSessionAccount = new Gson().fromJson(userSessionPreferences.getString("user_session", ""), UserAccount.class);
        loadingHandler = (TextView) findViewById(R.id.loadingHandler);
        loadingHandler.setText("");
        ready = false;

        tempFriendList = new ArrayList<>();
        chatList = new HashMap<>();
        friendAccountList = new HashMap<>();

        tempFriendList.add(new ParentFriendListItem());
        tempFriendList.add(new ParentFriendListItem());

        tempFriendList.get(0).setGroupViewName("Groups");
        tempFriendList.get(1).setGroupViewName("Friends");

        initDatabase();

        loadingHandler.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userCount != 0 && friendAccountList.size() == userCount
                        && userSessionAccount.getTotalFriend() == friendCount
                        && userSessionAccount.getTotalGroup() == groupCount
                        ) {
                    selectAllFriend();
                    updateGroup = updateUser = true;
                    setLayout();
                    ready = true;
                    Toast.makeText(getApplicationContext(), "Welcome," + userSessionAccount .getDisplayName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void selectAllFriend() {
        for (int i = 0; i < tempFriendList.get(FragmentFriend.FRIEND).getFriendList().size(); i++) {
            tempFriendList.get(FragmentFriend.FRIEND).getFriendList().get(i).setFriendDetail(
                    friendAccountList.get(
                            tempFriendList.get(
                                    FragmentFriend.FRIEND
                            ).getFriendList().get(i).getFriendIdentity().getFriendId())
            );
        }
    }

    void checkUpdates() {
        groupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!updateGroup) return;
                GroupIdentity g = dataSnapshot.getValue(GroupIdentity.class);
                FriendListItem fl = new FriendListItem();
                fl.setGroupIdentity(g);

                if (g.getUserId().equals(HomeActivity.userSessionAccount.getUserId())
                        && !HomeActivity.tempFriendList.get(FragmentFriend.GROUP).getFriendList().contains(fl)) {
                    HomeActivity.tempFriendList.get(FragmentFriend.GROUP).getFriendList().add(new FriendListItem());
                    HomeActivity.tempFriendList.get(FragmentFriend.GROUP).getFriendList().get(
                            HomeActivity.tempFriendList.get(FragmentFriend.GROUP).getFriendList().size() - 1
                    ).setGroupIdentity(g);

                    FragmentFriend.adapter.setFriendAndGroupList(tempFriendList);
                    FragmentFriend.adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(final DataSnapshot dataSnapshot, String s) {
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot userSnapshot) {
                        if (GroupChatActivity.set) {
                            GroupIdentity g = dataSnapshot.getValue(GroupIdentity.class);
                            if (g.getGroupId().equals(GroupChatActivity.listChat.getGroup().getGroupId())
                                    && g.getAccept() == 1) {
                                GroupChatActivity.groupMember.put(g.getUserId(), userSnapshot.child(g.getUserId()).getValue(UserAccount.class));
                                GroupChatActivity.adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (!updateUser) return;
                if (dataSnapshot.getValue(UserAccount.class).getUserId().equals(userSessionAccount.getUserId()))
                    setSession(dataSnapshot.getValue(UserAccount.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    static Toolbar toolbar;
   static RoundImage roundImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shrd = getSharedPreferences(MainActivity.preferencesName, Context.MODE_PRIVATE);

//        Toast.makeText(HomeActivity.this,shrd.getString("userID",null),Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);




        initComponent();


//        txtNavEmail.setText(userSessionAccount.getEmail());


    }
    static NavigationView navigationView;
    void setLayout() {
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //tes lagi , kemaren2 soalnya null skrg kgk ;__;

        getSupportActionBar().setTitle("Friend");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), 3, HomeActivity.this));



        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle("Friend");
                        showMenuItem();

                        break;
                    case 1:
                        getSupportActionBar().setTitle("Chat");
                        hideMenuItem();
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Profile");
                        hideMenuItem();

                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
         bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.com_facebook_profile_picture_blank_portrait);
        refresehNavigationDrawer();

    }

    public static void showNotif()
    {

    }

    static Bitmap bmp2;
    public  static void refresehNavigationDrawer()
    {
        View vHeader=navigationView.getHeaderView(0);
        TextView txtNavDisplayName=(TextView) vHeader.findViewById(R.id.displayNameNavBar);
        TextView txtNavEmail = (TextView) vHeader.findViewById(R.id.emailNavBar);
        ImageView imgProfNav = (ImageView) vHeader.findViewById(R.id.profImageSlide);
        txtNavDisplayName.setText(userSessionAccount.getDisplayName());
        txtNavEmail.setText(userSessionAccount.getEmail());


        byte[] imageAsBytes = Base64.decode(
                userSessionAccount.getProfilePicture()
                , Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        Bitmap bmp3=Bitmap.createScaledBitmap(bmp2, 50, 50, false);
        if(bmp!=null) {
            Bitmap bmp4 = Bitmap.createScaledBitmap(bmp, 50, 50, false);
            roundImage=new RoundImage(bmp4,50,50);
            imgProfNav.setImageDrawable(roundImage);
        }
        else
        {
            roundImage=new RoundImage(bmp3,50,50);
            imgProfNav.setImageDrawable(roundImage);
        }
    }
    @Override
    public void onBackPressed() {
        if (!ready) {
            this.finish();
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                initStatic();
                this.finish();
            }
        }
    }

    static Menu mn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        mn = menu;


        return true;
    }

    public static void hideMenuItem() {
        mn.findItem(R.id.action_settings).setVisible(false);
        mn.findItem(R.id.action_settings_create_group).setVisible(false);
    }

    public static void showMenuItem() {
        mn.findItem(R.id.action_settings).setVisible(true);
        mn.findItem(R.id.action_settings_create_group).setVisible(true);

    }

    public void wew() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), AddFriend.class);
            startActivity(i);

            return true;
        } else if (id == R.id.action_settings_create_group) {
            Intent i = new Intent(getApplicationContext(), CreateGroup.class);
            startActivity(i);

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
            Intent i = new Intent(getApplicationContext(), AccountSetting.class);
            startActivity(i);

        } else if (id == R.id.nav_logout) {
            File f;
            f = new File("/data/data/" + getPackageName() + "/shared_prefs/user_session.xml");
            f.delete();
            initStatic();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
