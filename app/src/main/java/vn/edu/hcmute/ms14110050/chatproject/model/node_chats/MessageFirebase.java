package vn.edu.hcmute.ms14110050.chatproject.model.node_chats;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public class MessageFirebase extends MessagePOJO {

    public Map<String, Object> toMap(boolean isCreated) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("chat_id", chatID);
        map.put("uid_sender", senderUid);
        map.put("create_time", isCreated ? ServerValue.TIMESTAMP : createTime);
        map.put("content", content);
        map.put("type_message", messageType);

        return map;
    }

}
