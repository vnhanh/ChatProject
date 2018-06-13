package vn.edu.hcmute.ms14110050.chatproject.model.node_chats;


import com.google.firebase.database.PropertyName;

import vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE;

/**
 * Created by Vo Ngoc Hanh on 5/30/2018.
 *
 * Đối tượng được sử dụng trong đối tượng Chat
 * Mô tả tin nhắn
 */

public class MessagePOJO {
    protected String id;

    // chat_id của đối tượng chat chứa dữ liệu chat của 2 người
    protected String chatID;

    protected String senderUid;

    protected long createTime;

    protected String content;

    @MESSAGE_TYPE
    protected int messageType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("chat_id")
    public String getChatID() {
        return chatID;
    }

    @PropertyName("chat_id")
    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    @PropertyName("uid_sender")
    public String getSenderUid() {
        return senderUid;
    }

    @PropertyName("uid_sender")
    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    @PropertyName("create_time")
    public long getCreateTime() {
        return createTime;
    }

    @PropertyName("create_time")
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @PropertyName("type_message")
    public int getMessageType() {
        return messageType;
    }

    @PropertyName("type_message")
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
