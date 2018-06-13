package vn.edu.hcmute.ms14110050.chatproject.module.chat.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.ChatViewModel;

/**
 * Created by Vo Ngoc Hanh on 6/10/2018.
 */

public class ChatHelper {

    public static void setupRecordButton(@NonNull final Activity context,
                                         @NonNull final ImageView imgRecord,
                                         @NonNull final ChatRecordHelper.RecordListener listener) {
        imgRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(imgRecord.getContext(), context.getString(R.string.start_record), Toast.LENGTH_SHORT).show();
                        // TODO: thêm hiệu ứng ghi âm

                        // bắt đầu ghi âm
                        ChatRecordHelper.onStartRecord(context, listener);
                        break;

                    case MotionEvent.ACTION_UP:
                        // dừng ghi âm
                        ChatRecordHelper.onStopRecord(context, listener);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(R.string.ask_wanna_send_record_file);
                        builder.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ChatRecordHelper.onPushRecord(listener);
                            }
                        }).setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        builder.show();

                        view.performClick();
                        break;

                    default:
                        view.performClick();
                        return false;
                }
                return true;
            }
        });
    }

    public static void setupMessageInput(final Activity context, final EditText messageInput, final ChatViewModel viewModel) {
        messageInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (viewModel != null) {
                        viewModel.onEnterMessage(messageInput.getText().toString().trim());
                    }
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    messageInput.setText("");
                    return true;
                }
                return false;
            }
        });
    }
}
