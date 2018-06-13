package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/6/2018.
 */

public class SearchChatterHelper {
    static SearchChatterHelper INSTANCE;

    ArrayList<Chatter> chatters1 = new ArrayList<>();
    HashMap<String, Chatter> chattersMap1 = new HashMap<>();
    ArrayList<Chatter> chatters2 = new ArrayList<>();

    // hàng đợi các keySearch và callback
    ArrayList<SimpleCallback<ArrayList<Chatter>>> callbacks = new ArrayList<>();
    ArrayList<String> keySearchs = new ArrayList<>();

    // các instance lưu giữ tham chiếu tới từ class yêu cầu search
    DatabaseReference reference;
    String uid;
    SimpleCallback<ArrayList<Chatter>> _callback;

    /*
    * Các cờ nhớ
    * */
    // cờ xác định đang search
    private boolean isSearching = false;
    // cờ yêu cầu ngắt
    private boolean interupt = false;

    // cờ xác định đã load xong các chatters
    private boolean isLoadChatters1 = false;
    private boolean isLoadChatters2 = false;

    // cờ xác định đang lắng nghe dữ liệu từ firebase
    private boolean isWaitFirebaseValueListener = false;

    /**
     * cờ xác định đã interupt hoặc hoàn thành việc lắng nghe dữ liệu từ firebase
     * chỉ có ý nghĩa khi cờ {@link #isWaitFirebaseValueListener} có giá trị TRUE
     */
    private boolean isInteruptChatters1 = false;
    private boolean isInteruptChatters2 = false;

    /*
    * END
    * */

    private SearchChatterHelper() {
    }

    public static SearchChatterHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SearchChatterHelper();
        }
        return INSTANCE;
    }

    void search(@NonNull String keySearch,
                @NonNull DatabaseReference usersReference,
                @NonNull final String uid,
                @NonNull final SimpleCallback<ArrayList<Chatter>> callback) {
        // hủy bỏ tác vụ search này
        // nếu đã bị yêu cầu ngắt
        if (interupt) {
            Log.d("CHATTER", getClass().getSimpleName() + ":search:interupt");
            processSearchWait();
            return;
        }

        // bật cờ đánh dấu đang search
        isSearching = true;
        isWaitFirebaseValueListener = false;

        if (reference == null) {
            reference = usersReference;
        }

        if (this.uid == null) {
            this.uid = uid;
        }

        keySearch = keySearch.trim().replaceAll(" +", " ");
        keySearch = StringUtils.deAccent(keySearch).toLowerCase();

        // init or reset
        isLoadChatters1 = false;
        isLoadChatters2 = false;

        final Query query1 = reference.child(uid)
                .child(FIREBASE.NODE_CHATTERS)
                .orderByChild(FIREBASE.NODE_FULLNAME_ACRONYM)
                .startAt(keySearch).endAt(keySearch + "\uf8ff");

        final Query query2 = reference.orderByChild(FIREBASE.NODE_FULLNAME_ACRONYM)
                .startAt(keySearch).endAt(keySearch + "\uf8ff");

        isWaitFirebaseValueListener = true;

        // giữ tham chiếu callback để xử lý sau khi nhận dữ liệu từ firebase server
        _callback = callback;

        query1.addListenerForSingleValueEvent(new ProcessChatters1Callback(this, TYPE_CHATTERS.ONE));
        query2.addListenerForSingleValueEvent(new ProcessChatters1Callback(this, TYPE_CHATTERS.TWO));
    }

    private static class  ProcessChatters1Callback implements ValueEventListener {
        SearchChatterHelper searchHelper;
        TYPE_CHATTERS typeChatters;

        public ProcessChatters1Callback(@NonNull SearchChatterHelper searchHelper, TYPE_CHATTERS typeChatters) {
            this.searchHelper = searchHelper;
            this.typeChatters = typeChatters;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (typeChatters == TYPE_CHATTERS.ONE) {
                Log.d("CHATTER", getClass().getSimpleName() + ":chatters1");
                searchHelper.onReceiveChatters1(dataSnapshot);
            } else if (typeChatters == TYPE_CHATTERS.TWO) {
                Log.d("CHATTER", getClass().getSimpleName() + ":chatters2");
                searchHelper.onReceiveChatters2(dataSnapshot);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            if (typeChatters == TYPE_CHATTERS.ONE) {
                Log.d("CHATTER", getClass().getSimpleName() + ":chatters1:error:" + databaseError.getMessage());
                searchHelper.onNotReceiveChatters1();
            } else if (typeChatters == TYPE_CHATTERS.TWO) {
                Log.d("CHATTER", getClass().getSimpleName() + ":chatters2:error:" + databaseError.getMessage());
                searchHelper.onNotReceiveChatters2();
            }
        }
    }

    private void onNotReceiveChatters2() {
        Log.d("CHATTER", getClass().getSimpleName() + ":onNotReceiveChatters2");
        if (interupt) {
            isInteruptChatters2 = true;
            checkInteruptListenFirebaseValue();
            return;
        }

        isLoadChatters2 = true;
        if (isWaitFirebaseValueListener) {
            isInteruptChatters2 = true;
        }

        chatters2 = new ArrayList<>();
        checkLoadChatters(_callback);
    }

    private void onReceiveChatters2(DataSnapshot dataSnapshot) {
        Log.d("CHATTER", getClass().getSimpleName() + ":onReceiveChatters2");
        if (interupt) {
            Log.d("CHATTER", getClass().getSimpleName() + ":onReceiveChatters2:interupt");
            isInteruptChatters2 = true;
            checkInteruptListenFirebaseValue();
            return;
        }

        chatters2 = new ArrayList<>();

        if (dataSnapshot.getValue() != null) {
            Log.d("CHATTER", getClass().getSimpleName() + ":loadChatters2:start read datasnapshot");

            for (DataSnapshot item : dataSnapshot.getChildren()) {
                User user = item.getValue(User.class);
                if (!uid.equals(user.getUid())) {
                    // tạo 1 object Chatter từ dữ liệu đã có của người dùng
                    Chatter chatter = user.createChatter();
                    chatters2.add(chatter);
                }
            }
            Log.d("CHATTER", getClass().getSimpleName() + ":loadChatters2:finish read datasnapshot");
        }else{
            Log.d("CHATTER", getClass().getSimpleName() + ":loadChatters2:datasnapshot is null");
        }

        isLoadChatters2 = true;
        if (isWaitFirebaseValueListener) {
            isInteruptChatters2 = true;
        }
        checkLoadChatters(_callback);
    }

    private void onNotReceiveChatters1() {
        Log.d("CHATTER", getClass().getSimpleName() + ":onNotReceiveChatters1");
        if (interupt) {
            isInteruptChatters1 = true;
            checkInteruptListenFirebaseValue();
            return;
        }

        chatters1 = new ArrayList<>();
        isLoadChatters1 = true;
        if (isWaitFirebaseValueListener) {
            isInteruptChatters1 = true;
        }
        checkLoadChatters(_callback);
    }

    private void onReceiveChatters1(DataSnapshot dataSnapshot) {
        // ngắt
        if (interupt) {
            Log.d("CHATTER", getClass().getSimpleName() + ":onReceiveChatters1:interupt");
            isInteruptChatters1 = true;
            checkInteruptListenFirebaseValue();
            return;
        }
        Log.d("CHATTER", getClass().getSimpleName() + ":onReceiveChatters1");

        chatters1 = new ArrayList<>();
        chattersMap1 = new HashMap<>();

        if (dataSnapshot.getValue() != null) {
            Log.d("CHATTER", getClass().getSimpleName() + ":loadChatters1:datasnapshot is not null");
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                Chatter chatter = child.getValue(Chatter.class);
                chatters1.add(chatter);
                chattersMap1.put(child.getKey(), chatter);
            }
            Log.d("CHATTER", getClass().getSimpleName() + ":loadChatters2:read datasnapshot finish");
        }else{
            Log.d("CHATTER", getClass().getSimpleName() + ":loadChatters1:datasnapshot is null");
        }

        isLoadChatters1 = true;
        if (isWaitFirebaseValueListener) {
            isInteruptChatters1 = true;
        }
        checkLoadChatters(_callback);
    }

    // Kiểm tra việc load và xử lý dữ liệu trả về từ các node trên firebase
    private void checkLoadChatters(SimpleCallback<ArrayList<Chatter>> callback) {
        Log.d("CHATTER", getClass().getSimpleName() + ":checkLoadChatters");
        // nếu đã buộc bị ngắt
        // sẽ không tiến hành xử lý search keySearch cũ nữa
        if (interupt) {
            Log.d("CHATTER", getClass().getSimpleName() + ":checkLoadChatters:interupt");
            checkInteruptListenFirebaseValue();
            return;
        }

        if (isLoadChatters1 && isLoadChatters2) {
            // nếu đã load xong cả 2 chatters
            // thì coi như đã xong việc lắng nghe dữ liệu từ firebase
            isWaitFirebaseValueListener = false;
            readChatters(callback);
        }
    }

    private void readChatters(SimpleCallback<ArrayList<Chatter>> callback) {
        Log.d("CHATTER", getClass().getSimpleName() + ":readChatters");
        if (interupt) {
            processSearchWait();
            return;
        }

        ArrayList<Chatter> list = new ArrayList<>();

        // nếu chatters1 có dữ liệu mới add
        if (chatters1 != null && chatters1.size() > 0) {
            list.addAll(chatters1);
        }

        for (Chatter chatter : chatters2) {
            // nếu trong node chatters của user không tồn tại fullname nào tương tự
            // hoặc node chatters của user có tồn tại fullname tương tự với keySearch
            // nhưng uid của người dùng được tìm thấy trên firebase
            // không có trong node chatters
            if ((chattersMap1 == null || chattersMap1.size() == 0)
                    || (!chattersMap1.containsKey(chatter.getUid()))) {
                list.add(chatter);
            }
        }

        isSearching = false;

        callback.onSuccess(list);
        // nếu đang bị yêu cầu ngắt
        // sẽ reset lại các cờ cần thiết
        // và search nếu tìm thấy keySearch
        processSearchWait();
    }

    /*
    * Các method kiểm tra cờ ngắt và search tiếp (nếu có)
    * */

    // Reset lại các cờ
    // và xử lý các keySearch đang chờ (nếu có)
    private void processSearchWait() {
        Log.d("CHATTER", getClass().getSimpleName() + ":processSearchWait");
        interupt = false;
        isSearching = false;
        checkSearch();
    }

    // Kiểm tra xem đã interupt việc lắng nghe 2 value listener từ firebase chưa
    private void checkInteruptListenFirebaseValue() {
        Log.d("CHATTER", getClass().getSimpleName() + ":checkInteruptListenFirebaseValue");
        if (isWaitFirebaseValueListener) {
            Log.d("CHATTER", getClass().getSimpleName()+":checkInteruptListenFirebaseValue: is waiting firebase");
            if (isInteruptChatters1 && isInteruptChatters2) {
                isWaitFirebaseValueListener = false;
                processSearchWait();
            }
        }
    }

    /**
     * Tiến hành search nếu thỏa điều kiện
     * bảo đảm chỉ được gọi sau khi kiểm tra hoặc đã reset lại các cờ nhớ
     * only call from {@link #processSearchWait()}
     */
    private void checkSearch() {
        // đã kết thúc searching và trong hàng chờ keySearch có tồn tại giá trị
        if (!interupt && !isSearching && keySearchs.size() > 0 ) {
            Log.d("CHATTER", getClass().getSimpleName() + ":checkSearch: continue searching");
            if (reference == null) {
                Log.d("CHATTER", getClass().getSimpleName() + ":checkSearch:reference is null");
                return;
            }
            if (uid == null) {
                Log.d("CHATTER", getClass().getSimpleName() + ":checkSearch:uid is null");
                return;
            }

            String keySearch = keySearchs.get(0);
            SimpleCallback<ArrayList<Chatter>> callback = callbacks.get(0);

            // remove hàng đợi
            keySearchs.remove(0);
            callbacks.remove(0);

            search(keySearch, reference, uid, callback);
        }else{
            Log.d("CHATTER", getClass().getSimpleName() + ":checkSearch: finish searching");
        }
    }

    /*
    * END
    * */

    /*
    * Các method dành cho việc kiểm tra có đang search và xử lý ngắt search
    * */

    // Xóa hàng đợi
    public void clearWait() {
        keySearchs.clear();
        callbacks.clear();
        _callback = null;
    }

    boolean isSearching() {
        return isSearching;
    }

    void interupt() {
        Log.d("CHATTER", getClass().getSimpleName() + ":interupt search");
        interupt = true;
        // nếu đang chờ dữ liệu từ firebase đổ về
        // set thêm các cờ lắng nghe việc ngắt các listener dữ liệu đổ về từ firebase
        // để search ngay khi đã ngắt chúng đi
        if (isWaitFirebaseValueListener) {
            Log.d("CHATTER", getClass().getSimpleName() + ":interupt search: waitting firebase");
            isInteruptChatters1 = false;
            isInteruptChatters2 = false;
        }
    }

    void addKeySearch(String keySearch, SimpleCallback<ArrayList<Chatter>> callback) {
        Log.d("CHATTER", getClass().getSimpleName() + ":addKeySearch:key" + keySearch);
        if (keySearchs == null) {
            keySearchs = new ArrayList<>();
        }
        keySearchs.add(keySearch);
        if (callbacks == null) {
            callbacks = new ArrayList<>();
        }
        callbacks.add(callback);
    }

    /*
    * END
    * */

    public enum TYPE_CHATTERS{
        ONE, TWO
    }
}
