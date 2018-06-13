package vn.edu.hcmute.ms14110050.chatproject.common.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import vn.edu.hcmute.ms14110050.chatproject.R;

import static vn.edu.hcmute.ms14110050.chatproject.common.constant.PERMISSION.REQUEST_IMAGE_CAMERA;
import static vn.edu.hcmute.ms14110050.chatproject.common.constant.PERMISSION.REQUEST_IMAGE_GALLERY;

/**
 * Created by Vo Ngoc Hanh on 6/2/2018.
 *
 * Gọi intent chọn ảnh từ điện thoại
 */

public class ImagePickerHelper {
    private static final String DIALOG_ITEM_CAMERA = "Camera";
    private static final String DIALOG_ITEM_GALLERY = "Gallery";
    private static final String DIALOG_ITEM_CANCEL = "Cancel";

    static final CharSequence[] items = {DIALOG_ITEM_CAMERA, DIALOG_ITEM_GALLERY, DIALOG_ITEM_CANCEL};
    private static final int REQUEST_ID_PICK_IMAGE_FROM_GALLERY = 300;
    private static final int REQUEST_ID_PICK_IMAGE_FROM_CAMERA = 320;

    // Mở hộp thoại chọn ảnh
    public static void openImagePicker(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.tilte_pick_image_dialog));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                switch (items[index].toString()) {
                    case DIALOG_ITEM_CAMERA:
                        if (android.os.Build.VERSION.SDK_INT >= 23) {
                            int cameraPermission =
                                    ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA);
                            int writePermission =
                                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            // không có quyền truy cập camera
                            if (cameraPermission != PackageManager.PERMISSION_GRANTED
                                    || writePermission != PackageManager.PERMISSION_GRANTED) {
                                activity.requestPermissions(
                                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_ID_PICK_IMAGE_FROM_CAMERA);
                                return;
                            }
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(intent, REQUEST_IMAGE_CAMERA);
                        break;

                    case DIALOG_ITEM_GALLERY:
                        if (android.os.Build.VERSION.SDK_INT >= 23) {
                            int readPermission =
                                    ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
                            // không có quyền truy cập camera
                            if (readPermission != PackageManager.PERMISSION_GRANTED) {
                                activity.requestPermissions(
                                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_ID_PICK_IMAGE_FROM_GALLERY);
                                return;
                            }
                        }
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        activity.startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
                        break;


                    case DIALOG_ITEM_CANCEL:
                        dialogInterface.dismiss();
                        break;

                }
            }
        });
        builder.show();
    }

    // Mở hộp thoại chọn ảnh
    public static void openImagePicker(final Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setTitle(fragment.getString(R.string.tilte_pick_image_dialog));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                switch (items[index].toString()) {
                    case DIALOG_ITEM_CAMERA:
                        if (android.os.Build.VERSION.SDK_INT >= 23) {
                            int cameraPermission =
                                    ActivityCompat.checkSelfPermission(fragment.getContext(), android.Manifest.permission.CAMERA);
                            int writePermission =
                                    ActivityCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            // không có quyền truy cập camera
                            if (cameraPermission != PackageManager.PERMISSION_GRANTED
                                    || writePermission != PackageManager.PERMISSION_GRANTED) {
                                fragment.requestPermissions(
                                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_ID_PICK_IMAGE_FROM_CAMERA);
                                return;
                            }
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fragment.startActivityForResult(intent, REQUEST_IMAGE_CAMERA);
                        break;

                    case DIALOG_ITEM_GALLERY:
                        if (android.os.Build.VERSION.SDK_INT >= 23) {
                            int readPermission =
                                    ActivityCompat.checkSelfPermission(fragment.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
                            // không có quyền truy cập camera
                            if (readPermission != PackageManager.PERMISSION_GRANTED) {
                                fragment.requestPermissions(
                                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_ID_PICK_IMAGE_FROM_GALLERY);
                                return;
                            }
                        }
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        fragment.startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
                        break;


                    case DIALOG_ITEM_CANCEL:
                        dialogInterface.dismiss();
                        break;

                }
            }
        });
        builder.show();
    }
}
