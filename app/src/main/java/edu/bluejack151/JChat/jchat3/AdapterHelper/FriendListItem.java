package edu.bluejack151.JChat.jchat3.AdapterHelper;

import edu.bluejack151.JChat.jchat3.Helper.Friend;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

//class buat handle childVIewListnya
public class FriendListItem{
    private Friend friendIdentity;
    private GroupIdentity groupIdentity;
    private UserAccount friendDetail;

    public FriendListItem(){
        friendIdentity = null;
        groupIdentity = null;
        friendDetail = null;
    }

    public Friend getFriendIdentity() {
        return friendIdentity;
    }

    public void setFriendIdentity(Friend friendIdentity) {
        this.friendIdentity = friendIdentity;
    }

    public GroupIdentity getGroupIdentity() {
        return groupIdentity;
    }

    public void setGroupIdentity(GroupIdentity groupIdentity) {
        this.groupIdentity = groupIdentity;
    }

    public UserAccount getFriendDetail() {
        return friendDetail;
    }

    public void setFriendDetail(UserAccount friendDetail) {
        this.friendDetail = friendDetail;
    }
}