package edu.bluejack151.JChat.jchat3.AdapterHelper;

import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.Helper.Chat;
import edu.bluejack151.JChat.jchat3.Helper.GroupIdentity;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

/**
 * Created by ASUS on 27/12/2015.
 */
public class ChatListItem {
    private UserAccount user;
    private GroupIdentity group;
    private ArrayList<Chat> listChat;
    private int notifCount;

    public ChatListItem(){
        this.user = null;
        this.group = null;
        this.listChat = new ArrayList<>();
        this.notifCount = 0;
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

    public ArrayList<Chat> getListChat() {
        return listChat;
    }

    public void setListChat(ArrayList<Chat> listChat) {
        this.listChat = listChat;
    }

    public void setLastChat(Chat c){
        listChat.add(c);
    }

    public Chat getLastChat(){
        return listChat.get(listChat.size()-1);
    }

    public int getNotifCount() {
        return notifCount;
    }

    public void setNotifCount(int notifCount) {
        this.notifCount = notifCount;
    }
}
