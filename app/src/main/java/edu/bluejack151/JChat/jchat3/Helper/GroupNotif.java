package edu.bluejack151.JChat.jchat3.Helper;

/**
 * Created by ASUS on 27/12/2015.
 */
public class GroupNotif {
    private String groupId;
    private String userId;

    public GroupNotif(){}

    public GroupNotif(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
