package edu.bluejack151.JChat.jchat3.Helper;

/**
 * Created by ASUS on 24/12/2015.
 */
public class Friend {
    private String friendId;
    private String userId;
    private int blocked;

    public Friend(){}
    public Friend(String friendId, String userId, int blocked) {
        this.friendId = friendId;
        this.userId = userId;
        this.blocked = blocked;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }
}
