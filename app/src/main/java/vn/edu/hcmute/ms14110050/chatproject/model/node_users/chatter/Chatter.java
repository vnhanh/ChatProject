package vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter;

import android.content.res.Resources;
import android.databinding.Bindable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.last_message.LastMessage;

/**
 * Created by Vo Ngoc Hanh on 5/30/2018.
 *
 * Đối tượng được sử dụng trong đối tượng User
 * Mô tả dữ liệu bạn chat với người dùng và tin nhắn cuối cùng
 */

public class Chatter extends ChatterFirebase{

    public String getLastMessageText(Resources resources, LastMessage lastMessage) {
        if (lastMessage == null || StringUtils.isEmpty(lastMessage.getContent())) {
            return resources.getString(R.string.text_default_no_last_message);
        }
        switch (lastMessage.getMessageType()) {
            case MESSAGE_TYPE.TEXT:
                return lastMessage.getContent();

            case MESSAGE_TYPE.IMAGE:
                // người dùng gửi ảnh
                if (lastMessage.isSender()) {
                    return resources.getString(R.string.message_you_send_1_image);
                }
                // người dùng nhận ảnh
                else{
                    return resources.getString(R.string.message_someone_send_1_image);
                }

            case MESSAGE_TYPE.SOUND:
                // bạn chat gửi file âm thanh
                if (lastMessage.isSender()) {
                    return resources.getString(R.string.message_you_send_1_sound_file);
                }
                // bạn chat nhận file âm thanh
                else{
                    return resources.getString(R.string.message_someone_send_1_sound_file);
                }

            default:
                return "";
        }
    }

    @Bindable
    public String getLastSendTime() {
        if (lastMessage == null || StringUtils.isEmpty(lastMessage.getContent()) || lastMessage.getCreateTime() == 0) {
            return "";
        }
        Date now = new Date();
        long diff = now.getTime() - lastMessage.getCreateTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        SimpleDateFormat sdf = null;
        if (diffDays < 1) {
            sdf = new SimpleDateFormat("KK:mm a", Locale.getDefault());
        } else if (diffDays < 7) {
            sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        } else if (diffDays < 366) {
            sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
        } else {
            sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        }

        if (sdf == null) {
            return "";
        }

        Date date = new Date(lastMessage.getCreateTime());
        return sdf.format(date);
    }
}
