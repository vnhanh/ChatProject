package vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.ItemMessageType.IMAGE_RECEIVED;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.ItemMessageType.IMAGE_SEND;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.ItemMessageType.SOUND_RECEIVED;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.ItemMessageType.SOUND_SEND;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.ItemMessageType.TEXT_RECEIVED;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.viewholder.ItemMessageType.TEXT_SEND;

/**
 * Created by Vo Ngoc Hanh on 6/7/2018.
 */

@IntDef({TEXT_SEND, TEXT_RECEIVED, IMAGE_SEND, IMAGE_RECEIVED, SOUND_SEND, SOUND_RECEIVED})
@Retention(RetentionPolicy.SOURCE)
public @interface ItemMessageType {
    int TEXT_SEND = 0, TEXT_RECEIVED = 1,
            IMAGE_SEND = 2, IMAGE_RECEIVED = 3,
            SOUND_SEND = 4, SOUND_RECEIVED = 5;
}
