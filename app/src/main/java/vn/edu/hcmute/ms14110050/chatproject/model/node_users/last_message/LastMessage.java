package vn.edu.hcmute.ms14110050.chatproject.model.node_users.last_message;


import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vo Ngoc Hanh on 6/2/2018.
 */

public class LastMessage extends LastMessagePOJO{

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("is_sender", isSender);
        map.put("create_time", createTime);
        map.put("content", content);
        map.put("message_type", messageType);

        return map;
    }

    @PropertyName("is_sender")
    public boolean isSender() {
        return isSender;
    }

    @PropertyName("is_sender")
    public void setSender(boolean sender) {
        isSender = sender;
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

    @PropertyName("message_type")
    public int getMessageType() {
        return messageType;
    }

    @PropertyName("message_type")
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
