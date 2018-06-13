package vn.edu.hcmute.ms14110050.chatproject.module.chat;


import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.LifeCycle;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ResponseLoadUsers;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public interface ChatContract {
    interface View extends LifeCycle.View{

        void onExit();

        void onUpdateChatterFailed();

        // setup cho toolbar
        void setupToolbar(String fullname);

        // update user uid
        void onUpdateUserUid(ResponseLoadUsers uid);

        void onAddMessage(Message value);

        void onChangeMessage(Message value);

        void onRemoveMessage(Message value);

        void onSendMessageFailed();

        void onClickImagePicker();

        void uploadImageFailed();

        void uploadRecordFailed();
    }
}
