package vn.edu.hcmute.ms14110050.chatproject.model.node_chats;

import vn.edu.hcmute.ms14110050.chatproject.model.node_users.last_message.LastMessage;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public class Message extends MessageFirebase {
    public LastMessage getLastMessage(String uid) {
        LastMessage lastMessage = new LastMessage();
        lastMessage.setContent(content);
        lastMessage.setCreateTime(createTime);
        lastMessage.setMessageType(messageType);
        lastMessage.setSender(uid.equals(senderUid));
        return lastMessage;
    }
}
