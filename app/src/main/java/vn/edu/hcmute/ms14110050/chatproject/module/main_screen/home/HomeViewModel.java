package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.BaseViewModel;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.request_manager.ChatItemRequestManager;

/**
 * Created by Vo Ngoc Hanh on 5/23/2018.
 */

public class HomeViewModel extends BaseViewModel<HomeContract.View> implements SimpleCallback<User> {
    public final ObservableBoolean progressStatus = new ObservableBoolean(false);
    private SearchChatterHelper searchHelper;

    FirebaseUser firebaseUser;
    DatabaseReference usersReference;
    User user;

    // cờ xác định đang chế độ search
    private boolean isSearchMode = false;
    // cờ block search
    private boolean isBlockSearch = false;

    @Override
    public void onViewAttach(@NonNull HomeContract.View viewCallback) {
        super.onViewAttach(viewCallback);
        init();
    }

    private void init() {
        searchHelper = SearchChatterHelper.getInstance();

        createFirebase();
        createDatabaseReference();
    }

    private void createFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void createDatabaseReference() {
        usersReference = FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE.NODE_USERS);
    }

    /*
    * Implement OnChangeUserProfileListener
    * */

    @Override
    public void onSuccess(User user) {
        this.user = user;

        // không phải đang search
        if (!isSearchMode) {
            onUpdateUserProfile();
        }
    }

    @Override
    public void onError() {
        this.user = null;
        onUpdateUserProfile();
    }

    // Được gọi khi thông tin người dùng có thay đổi
    private void onUpdateUserProfile() {
        if (isViewAttached()) {
            Log.d("LOG", getClass().getSimpleName() + ":onUpdateUserProfile");
            showProgress();

            ChatItemRequestManager.getInstance().onUpdateUserProfile(user);

            readChattersLocal();
        }
    }

    private void readChattersLocal() {
        ArrayList<Chatter> chatters = new ArrayList<>();
        if (user != null) {
            for (Map.Entry<String, Chatter> entry : user.getChatters().entrySet()) {
                chatters.add(entry.getValue());
            }
        }
        hideProgress();
        getView().onLoadChatters(chatters);
    }

    private void showProgress() {
        progressStatus.set(true);
    }

    private void hideProgress() {
        progressStatus.set(false);
    }

    private SearchChatterHelper searchChatters;

    void search(String newText) {
        Log.d("CHATTER", getClass().getSimpleName() + ":search:key:" + newText);
        if (isBlockSearch) {
            Log.d("CHATTER", getClass().getSimpleName() + ":search:block");
            // xóa hàng đợi
            if (searchChatters != null) {
                searchChatters.clearWait();
            }
            return;
        }

        // bật cờ SearchMode
        // khi SearchMode = TRUE
        // nếu dữ liệu người dùng firebase có thay đổi
        // cũng không hiển thị lên UI
        // và phải chờ đến khi tắt SearchMode
        // mới hiển thị
        isSearchMode = true;

        if (searchChatters == null) {
            searchChatters = SearchChatterHelper.getInstance();
        }

        SimpleCallback<ArrayList<Chatter>> callback = new SimpleCallback<ArrayList<Chatter>>() {
            @Override
            public void onSuccess(ArrayList<Chatter> chatters) {
                if (!isBlockSearch) {
                    updateChatters(chatters);
                }
            }

            @Override
            public void onError() {
                if (!isBlockSearch) {
                    updateChatters(new ArrayList<Chatter>());
                }
            }
        };

        // nếu đang search
        if (searchChatters.isSearching()) {
            Log.d("CHATTER", getClass().getSimpleName() + ":search:helper is searching, it will be interupt");
            // ngắt search keySearch trước
            searchChatters.interupt();
            // add keySearch hiện tại và callback vào hàng chờ
            searchChatters.addKeySearch(newText, callback);
        }
        // nếu đang rảnh
        else{
            Log.d("CHATTER", getClass().getSimpleName() + ":search:run helper");
            // search ngay
            searchChatters.search(newText, usersReference, user.getUid(), callback);
        }
    }

    void blockSearch() {
        isBlockSearch = true;

        // tắt SearchMode, cho phép hiển thị các chatter riêng của người dùng
        turnOffSearchMode();
    }

    void unblockSearch() {
        isBlockSearch = false;
    }

    // tắt SearchMode, cho phép hiển thị các chatter riêng của người dùng
    private void turnOffSearchMode() {
        Log.d("CHATTER", getClass().getSimpleName() + ":turnOffSearchMode");
        isSearchMode = false;
        // hiển thị lại các chatter của người dùng
        onUpdateUserProfile();
    }

    private void updateChatters(ArrayList<Chatter> chatters) {
        if (isViewAttached()) {
            getView().onLoadChatters(chatters);
        }
    }
}