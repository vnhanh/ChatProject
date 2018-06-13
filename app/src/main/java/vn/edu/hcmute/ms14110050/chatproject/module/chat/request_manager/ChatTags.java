package vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.MESSAGE_EVENT.TAG_ADD_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.MESSAGE_EVENT.TAG_CANCELED_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.MESSAGE_EVENT.TAG_CHANGE_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.MESSAGE_EVENT.TAG_REMOVE_MESSAGE;

/**
 * Created by Vo Ngoc Hanh on 6/7/2018.
 */

public class ChatTags {
    public static final int TAG_SEND_MESSAGE_FAILED = 0;
    public static final int TAG_SEND_MESSAGE_SUCCESS = TAG_SEND_MESSAGE_FAILED + 1;

    public static final int TAG_REQUEST_UPDATE_CHATTERS_NODE_FOR_FIRST_MESSAGE = TAG_SEND_MESSAGE_SUCCESS + 1;
    public static final int TAG_REQUEST_CREATE_CHATTERS_NODE_ON_FIRST_MESSAGE = TAG_REQUEST_UPDATE_CHATTERS_NODE_FOR_FIRST_MESSAGE + 1;
    public static final int TAG_CREATED_OR_UPDATED_CHATTERS_NODE_SUCCESS = TAG_REQUEST_CREATE_CHATTERS_NODE_ON_FIRST_MESSAGE + 1;
    public static final int TAG_LOADED_TWO_USERS_SUCCESS = TAG_CREATED_OR_UPDATED_CHATTERS_NODE_SUCCESS + 1;
    public static final int TAG_PREPARE_MEDIA_PLAYER_FAILED = TAG_LOADED_TWO_USERS_SUCCESS + 1;
    public static final int TAG_MEDIA_PLAYER_IS_RUNNING = TAG_PREPARE_MEDIA_PLAYER_FAILED + 1;

    @IntDef({TAG_CANCELED_MESSAGE, TAG_ADD_MESSAGE, TAG_CHANGE_MESSAGE, TAG_REMOVE_MESSAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MESSAGE_EVENT{
        int TAG_CANCELED_MESSAGE = 100;
        int TAG_ADD_MESSAGE = 101;
        int TAG_CHANGE_MESSAGE = 102;
        int TAG_REMOVE_MESSAGE = 103;
    }
}
