package vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager;

/**
 * Created by Vo Ngoc Hanh on 6/8/2018.
 *
 * Kết quả trả về khi load thành công dữ liệu của user và bạn chat
 */

public class ResponseLoadUsers {
    String uid;
    String chatterUserUrlImage;
    String chatID;

    public ResponseLoadUsers(String uid, String chatterUserUrlImage, String chatID) {
        this.uid = uid;
        this.chatterUserUrlImage = chatterUserUrlImage;
        this.chatID = chatID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ResponseLoadUsers)) {
            return false;
        }
        ResponseLoadUsers response = (ResponseLoadUsers) obj;
        return ((uid == null && response.getUid() == null) || (uid != null && uid.equals(response.getUid()))
                && ((chatterUserUrlImage == null && response.getChatterUserUrlImage() == null)
                || (chatterUserUrlImage != null && chatterUserUrlImage.equals(response.getChatterUserUrlImage())))
                && ((chatID == null && response.getChatID() == null) || (chatID != null && chatID.equals(response.getChatID()))));
    }

    public String getUid() {
        return uid;
    }

    public String getChatterUserUrlImage() {
        return chatterUserUrlImage;
    }

    public String getChatID() {
        return chatID;
    }
}
