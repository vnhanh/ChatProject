package vn.edu.hcmute.ms14110050.chatproject.module.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.view.ImagePickerHelper;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ActivityChatBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.helper.ChatHelper;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ResponseLoadUsers;

import static vn.edu.hcmute.ms14110050.chatproject.common.constant.PERMISSION.REQUEST_IMAGE_CAMERA;
import static vn.edu.hcmute.ms14110050.chatproject.common.constant.PERMISSION.REQUEST_IMAGE_GALLERY;


/*
* Võ Ngọc Hạnh - 14110050 - Lớp Chiều thứ 2
* */


public class ChatActivity extends BaseActivity<ActivityChatBinding, ChatContract.View, ChatViewModel>
        implements ChatContract.View{

    private static Activity INSTANCE;

    MessageAdapter adapter;

    public static void startActivity(Activity activity, String chatterUid) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("chatterUid", chatterUid);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        String chatterUid = readExtras();
        if (StringUtils.isEmpty(chatterUid)) {
            onBackPressed();
            return;
        }
        viewModel = new ChatViewModel();
        setAndBindContentView(R.layout.activity_chat, this);
        viewModel.setChatterUserUid(chatterUid);
        binding.setViewmodel(viewModel);

        initRecyclerView();
        setupRecordButton();
        setupMessageEditText();
    }

    private void initRecyclerView() {
        binding.recyclerview.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(manager);
    }

    private void setupRecordButton() {
        ChatHelper.setupRecordButton(this, binding.imgRecord, viewModel);
    }

    private void setupMessageEditText() {
        ChatHelper.setupMessageInput(this, binding.edtMessage, viewModel);
    }

    private String readExtras() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("chatterUid")) {
                return intent.getStringExtra("chatterUid");
            }
        }
        return null;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    // Nhận ảnh trả về
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAMERA:
                    Uri uri = data.getData();
                    viewModel.uploadImage(uri);

                    break;

                case REQUEST_IMAGE_GALLERY:
                    uri = data.getData();
                    viewModel.uploadImage(uri);
                    break;
            }
        }
    }

    public static Activity getInstance() {
        return INSTANCE;
    }

    /*
    * IMPLEMENT CHATCONTRACT.VIEW
    * */

    @Override
    public void onExit() {
        onBackPressed();
    }

    @Override
    public void onUpdateChatterFailed() {
        Toast.makeText(this, getString(R.string.update_data_failed_and_exit), Toast.LENGTH_SHORT).show();
        onExit();
    }

    @Override
    public void setupToolbar(String chatterFullname) {
        getSupportActionBar().setTitle(chatterFullname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onUpdateUserUid(ResponseLoadUsers response) {
        Log.d("LOG", getClass().getSimpleName() + ":onUpdateUserUid(): response:" + response.getUid());
        initAdapter(response);
    }

    private void initAdapter(ResponseLoadUsers response) {
        if (adapter == null) {
            Log.d("LOG", getClass().getSimpleName() + ":initAdapter()");
            adapter = new MessageAdapter(response.getUid(), response.getChatterUserUrlImage());
            binding.recyclerview.setAdapter(adapter);
        }
    }

    @Override
    public void onSendMessageFailed() {
        Toast.makeText(this, getString(R.string.send_message_faield), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadRecordFailed() {
        Toast.makeText(this, getString(R.string.upload_record_message_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddMessage(Message message) {
        Log.d("LOG", getClass().getSimpleName() + ":onAddMessage():");
        if (adapter != null) {
            Log.d("LOG", getClass().getSimpleName() + ":onAddMessage():mesage:" + message.getContent());
            adapter.addMessage(message);
            binding.recyclerview.smoothScrollToPosition(adapter.getItemCount()-1);
        }
    }

    @Override
    public void onChangeMessage(Message message) {
        if (adapter != null) {
            adapter.changeMessage(message);
        }
    }

    @Override
    public void onRemoveMessage(Message message) {
        if (adapter != null) {
            adapter.removeMessage(message);
        }
    }

    @Override
    public void onClickImagePicker() {
        ImagePickerHelper.openImagePicker(this);
    }

    @Override
    public void uploadImageFailed() {
        Toast.makeText(this, getString(R.string.upload_image_message_failed), Toast.LENGTH_SHORT).show();
    }
}
