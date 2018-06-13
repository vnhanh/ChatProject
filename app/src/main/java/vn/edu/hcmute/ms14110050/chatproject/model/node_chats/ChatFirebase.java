package vn.edu.hcmute.ms14110050.chatproject.model.node_chats;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public class ChatFirebase extends ChatPOJO {

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("chat_id", chatID);
        map.put("uid1", uid1);
        map.put("uid2", uid2);

        Map<String, Object> messagesMap = new HashMap<>();
        for (Map.Entry<String, Message> message : messages.entrySet()) {
            messagesMap.put(message.getKey(), message.getValue().toMap(false));
        }
        map.put("messages", messagesMap);

        return map;
    }

}
