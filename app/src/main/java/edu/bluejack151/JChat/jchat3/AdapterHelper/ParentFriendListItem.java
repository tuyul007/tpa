package edu.bluejack151.JChat.jchat3.AdapterHelper;

import java.util.ArrayList;

//class buat handle ParentViewListnya
public class ParentFriendListItem{
    private String groupViewName;
    private ArrayList<FriendListItem> friendList;

    public ParentFriendListItem(){
        friendList = new ArrayList<>();
    }

    public String getGroupViewName() {
        return groupViewName;
    }

    public void setGroupViewName(String groupViewName) {
        this.groupViewName = groupViewName;
    }

    public ArrayList<FriendListItem> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<FriendListItem> friendList) {
        this.friendList = friendList;
    }
}