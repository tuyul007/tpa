package edu.bluejack151.JChat.jchat3.Helper;

/**
 * Created by ASUS on 27/12/2015.
 */
public class Chat {
    private String fromId;
    private String toId;
    private String groupId;
    private int privateStatus;
    private String message;
    private long timeStamp;

    public Chat(){}

    public Chat(String fromId, String toId, String groupId, int privateStatus, String message, long timeStamp) {
        this.fromId = fromId;
        this.toId = toId;
        this.groupId = groupId;
        this.privateStatus = privateStatus;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getPrivateStatus() {
        return privateStatus;
    }

    public void setPrivateStatus(int privateStatus) {
        this.privateStatus = privateStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


}

