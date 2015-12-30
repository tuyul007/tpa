package edu.bluejack151.JChat.jchat3;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.AdapterHelper.FriendListItem;
import edu.bluejack151.JChat.jchat3.AdapterHelper.ParentFriendListItem;
import edu.bluejack151.JChat.jchat3.Helper.Friend;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

/**
 * Created by komputer on 12/22/2015.
 */
public class FragmentFriend  extends android.support.v4.app.Fragment{

    final static Integer FRIEND = 1;
    final static Integer GROUP = 0;
    public static ExpandableListView elv;


    ImageButton searchContact;
    Firebase friendRef,groupRef,userRef;

    EditText fieldSearchFriend;
    ArrayList<ParentFriendListItem>listGroupAndFriend;
    public static SavedTabsListAdapter adapter;

    GroupIdentity groupTarget;
    Friend friendIdentityTarget;
    UserAccount friendDetailTarget;
    FriendListItem fl;


    Dialog dialog;
    Button popUpMenu1,popUpMenu2;

    String search = "";
    public String toLower(String word){
        String newWord="";
        for(int i=0; i<word.length(); i++){
            newWord += Character.toLowerCase(word.charAt(i));
        }

        return newWord;
    }

    void toastMsg(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    void initModalDialog(){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.friend_modal_dialog);
        popUpMenu1 = (Button)dialog.findViewById(R.id.popup_1);
        popUpMenu2 =  (Button)dialog.findViewById(R.id.popup_2);
    }

    void initFirebase(){
        userRef = new Firebase("https://jchatapps.firebaseio.com/user");
        friendRef = new Firebase("https://jchatapps.firebaseio.com/friend");
        groupRef = new Firebase("https://jchatapps.firebaseio.com/group");

    }

    ArrayList<ParentFriendListItem> searchFriendAndGroup(String search){

        ArrayList<ParentFriendListItem>friendList = new ArrayList<>();

        friendList.add(new ParentFriendListItem());
        friendList.add(new ParentFriendListItem());

        friendList.get(0).setGroupViewName("Groups");
        friendList.get(1).setGroupViewName("Friends");

        for(int j=0; j<HomeActivity.tempFriendList.get(GROUP).getFriendList().size(); j++){
                if(toLower(HomeActivity.tempFriendList.get(GROUP).getFriendList().get(j)
                        .getGroupIdentity().getGroupName()).contains(search)) {
                    friendList.get(GROUP).getFriendList().add(HomeActivity.tempFriendList.get(GROUP).getFriendList().get(j));
                }
            }
        for(int j=0; j<HomeActivity.tempFriendList.get(FRIEND).getFriendList().size(); j++){
                if(toLower(HomeActivity.tempFriendList.get(FRIEND).getFriendList().get(j)
                        .getFriendDetail().getDisplayName()).contains(search)){
                    friendList.get(FRIEND).getFriendList().add(HomeActivity.tempFriendList.get(FRIEND).getFriendList().get(j));
                }
            }

        return friendList;
    }

    @Override
       public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance)
    {
        View view=inflater.inflate(R.layout.fragmentfriend,container,false);
        elv = (ExpandableListView) view.findViewById(R.id.friendListView);
        fieldSearchFriend = (EditText)view.findViewById(R.id.inputContactName);
        listGroupAndFriend = HomeActivity.tempFriendList;
        adapter = new SavedTabsListAdapter(getActivity(), listGroupAndFriend);
        initModalDialog();
        initFirebase();
        elv.setAdapter(adapter);

        fieldSearchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search = toLower(fieldSearchFriend.getText().toString());

                for (int i = 0; i < HomeActivity.tempFriendList.size(); i++) {
                    elv.expandGroup(i);
                }
                listGroupAndFriend = searchFriendAndGroup(search);
                adapter.setFriendAndGroupList(listGroupAndFriend);
                adapter.notifyDataSetChanged();
            }
        });

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                managePopUpMenu(groupPosition,childPosition);

                dialog.show();

                return false;
            }
        });

        return view;
    }

    void managePopUpMenu(final int group, final int child){
        fl = listGroupAndFriend.get(group).getFriendList().get(child);
        if(group == GROUP){
            dialog.setTitle(fl.getGroupIdentity().getGroupName());
            popUpMenu1.setText("Chat");
            popUpMenu2.setText("Leave Group");
            if(fl.getGroupIdentity().getAccept() == 0){
                popUpMenu1.setText("Accept");
                popUpMenu2.setText("Decline");
                popUpMenu2.setVisibility(View.VISIBLE);

            }
            popUpMenu1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popUpMenu1.getText().equals("Accept")) {
                        fl.getGroupIdentity().setAccept(1);
                        groupRef.child(fl.getGroupIdentity().getGroupId() + "_" +
                                fl.getGroupIdentity().getUserId()).setValue(fl.getGroupIdentity());

                        HomeActivity.userSessionAccount.setTotalGroup((HomeActivity.userSessionAccount.getTotalGroup() + 1));
                        userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);

                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        //chat
                        toastMsg("group chat");
                    }
                }
            });
            popUpMenu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listGroupAndFriend.get(group).getFriendList().remove(child);
                    groupRef.child(fl.getGroupIdentity().getGroupId() + "_" +
                            fl.getGroupIdentity() .getUserId()).removeValue();

                    if(popUpMenu2.getText().equals("Leave Group")){
                        HomeActivity.userSessionAccount.setTotalGroup((HomeActivity.userSessionAccount.getTotalGroup() - 1));
                        userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);
                    }
                    HomeActivity.totalGroup--;
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        }else{
            popUpMenu2.setVisibility(View.VISIBLE);
            dialog.setTitle(fl.getFriendDetail().getDisplayName());
            popUpMenu1.setText("Chat");
            popUpMenu2.setText("Block");
            if(fl.getFriendIdentity().getBlocked() == 1){
                popUpMenu2.setText("Unblock");
            }
            popUpMenu1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //chat
                    toastMsg("friend chat");
                }
            });
            popUpMenu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fl.getFriendIdentity().getBlocked() == 1)
                        fl.getFriendIdentity().setBlocked(0);
                    else
                        fl.getFriendIdentity().setBlocked(1);

                    friendRef.child(HomeActivity.userSessionAccount.getUserId() + "_" +
                            fl.getFriendIdentity().getFriendId()).setValue(fl.getFriendIdentity());

                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        }
    }

    //class buat setting expandedviewlist nya
    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

        private  Context context;
        private  ArrayList<ParentFriendListItem> friendAndGroupList;

        public ArrayList<ParentFriendListItem> getFriendAndGroupList() {
            return friendAndGroupList;
        }

        public void setFriendAndGroupList(ArrayList<ParentFriendListItem> friendAndGroupList) {
            this.friendAndGroupList = friendAndGroupList;
        }

        public SavedTabsListAdapter(Activity context, ArrayList<ParentFriendListItem> list)
        {
            this.context=context;
            this.friendAndGroupList = list;
        }

        @Override
        public int getGroupCount() {
            return friendAndGroupList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return friendAndGroupList.get(i).getFriendList().size();
        }

        @Override
        public Object getGroup(int i) {
            return friendAndGroupList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return friendAndGroupList.get(i).getFriendList().get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


        //buat ngeset header expandable nya nya
        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            String headerTitle = friendAndGroupList.get(i).getGroupViewName();
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.list_friend_group, null);
            }

            TextView lblListHeader = (TextView) view.findViewById(R.id.headerFriend);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return view;
        }


        //buat set child expandable nya
        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            String headerTitle = "";
            if(friendAndGroupList.get(i).getGroupViewName().equals("Friends")){
                headerTitle = friendAndGroupList.get(i).getFriendList().get(i1).getFriendDetail()
                        .getDisplayName();
                if(friendAndGroupList.get(i).getFriendList().get(i1).getFriendIdentity().getBlocked() == 1)
                    headerTitle+= " (blocked)";
            }else{
                headerTitle = friendAndGroupList.get(i).getFriendList().get(i1).getGroupIdentity()
                        .getGroupName();

                if(friendAndGroupList.get(i).getFriendList().get(i1).getGroupIdentity().getAccept() == 0)
                    headerTitle+= " (invited)";
            }
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.list_friend_item, null);
            }

            TextView lblListHeader = (TextView) view.findViewById(R.id.itemFriend);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }
}
