package edu.bluejack151.JChat.jchat3.AdapterHelper;

import edu.bluejack151.JChat.jchat3.Helper.Chat;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

/**
 * Created by ASUS on 27/12/2015.
 */
public class ChatAdapterItem {
    private UserAccount user;
    private GroupIdentity group;
    private Chat lastChat;
    private int notifCount;

    public ChatAdapterItem(){
        this.user = null;
        this.group = null;
        this.lastChat = null;
        this.notifCount = 0;
    }

    public int getNotifCount() {
        return notifCount;
    }

    public void setNotifCount(int notifCount) {
        this.notifCount = notifCount;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public GroupIdentity getGroup() {
        return group;
    }

    public void setGroup(GroupIdentity group) {
        this.group = group;
    }

    public Chat getLastChat() {
        return lastChat;
    }

    public void setLastChat(Chat lastChat) {
        this.lastChat = lastChat;
    }
}
