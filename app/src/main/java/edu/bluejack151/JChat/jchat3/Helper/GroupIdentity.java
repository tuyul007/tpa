package edu.bluejack151.JChat.jchat3.Helper;

/**
 * Created by ASUS on 25/12/2015.
 */
public class GroupIdentity {
    private String groupId;
    private String groupName;
    private String userId;
    private int accept;

    public GroupIdentity(){}

    public GroupIdentity(String groupId, String groupName, String userId, int accept) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.userId = userId;
        this.accept = accept;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }
}
