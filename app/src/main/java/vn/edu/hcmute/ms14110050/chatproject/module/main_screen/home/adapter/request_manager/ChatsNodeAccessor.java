package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.request_manager;

import vn.edu.hcmute.ms14110050.chatproject.base.MyObservable;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatsNodeUtil;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class ChatsNodeAccessor extends MyObservable{

    public void createChatRoom(String userUid, String chatterUserUid, SimpleCallback<String> callback) {
        ChatsNodeUtil.createChatRoom(userUid, chatterUserUid, callback);
    }
}
