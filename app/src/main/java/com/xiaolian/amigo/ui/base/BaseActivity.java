/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.xiaolian.amigo.ui.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.aitangba.swipeback.SwipeBackActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.widget.dialog.ActionSheetDialog;
import com.xiaolian.amigo.ui.widget.dialog.LodingDialog;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.NetworkUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.inject.Inject;

import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
// TODO: 加载去掉浮层 添加菊花

public abstract class BaseActivity extends SwipeBackActivity
        implements IBaseView {

    private final String TAG = this.getClass().getSimpleName();

    private static final int REQUEST_CODE_CAMERA = 0x1103;
    private static final int REQUEST_CODE_PICK = 0x1104;
    private static final int REQUEST_CODE_ICON = 0x1105;
    private static final int REQUEST_BLE = 0x1106;

    private Uri mPhotoImageUri;
    private Uri mPickImageUri;
    private Uri mCropImageUri;

    RxPermissions rxPermissions;

    private LodingDialog mProgressDialog;


    private Unbinder mUnBinder;

    ActionSheetDialog actionSheetDialog;
    // 申请蓝牙访问权限后的回调
    private Callback blePermissonCallback;

    @Inject
    ISharedPreferencesHelp sharedPreferencesHelp;
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxPermissions = RxPermissions.getInstance(this);
    }


    private void selectPhoto() {
        mPickImageUri = getImageUri("pick");
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK);

    }

    private void takePhoto() {
        mPhotoImageUri = getImageUri("photo");
        //调用相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoImageUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }


    private Uri getImageUri(String fileName) {
        Uri imageUri;
        File outputImage = new File(getExternalCacheDir(), fileName + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        return imageUri;
    }

    private Uri getCropUri(String fileName) {
        File outputImage = new File(Environment.getExternalStorageDirectory(), fileName + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(outputImage);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                mCropImageUri = getCropUri("crop");
                UCrop.Options options = new UCrop.Options();
                int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
                options.setToolbarColor(colorPrimary);
                options.setActiveWidgetColor(colorPrimary);
                options.setStatusBarColor(colorPrimary);
                UCrop.of(mPhotoImageUri, mCropImageUri)
                        .withAspectRatio(25, 17)
                        .withMaxResultSize(250 * 2, 170 * 2)
                        .withOptions(options)
                        .start(this);

            } else if (requestCode == UCrop.REQUEST_CROP) {
//                mImage.setImageDrawable(null);
//                mImage.setImageURI(mCropImageUri);
//                uploadImage(mCropImageUri);
                if (imageCallback != null) {
                    imageCallback.callback(mCropImageUri);
                }
            } else if (requestCode == REQUEST_CODE_PICK) {
                if (data != null && data.getData() != null) {
                    mPickImageUri = data.getData();
                    mCropImageUri = getCropUri("crop");
                    UCrop.Options options = new UCrop.Options();
                    int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
                    options.setToolbarColor(colorPrimary);
                    options.setActiveWidgetColor(colorPrimary);
                    options.setStatusBarColor(colorPrimary);
                    UCrop.of(mPickImageUri, mCropImageUri)
                            .withAspectRatio(25, 17)
                            .withMaxResultSize(250 * 2, 170 * 2)
                            .withOptions(options)
                            .start(this);
                }
            } else if (requestCode == REQUEST_BLE) {
                if (null != blePermissonCallback) {
                    blePermissonCallback.execute();
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            if (requestCode == UCrop.REQUEST_CROP) {
                showMessage("剪裁失败");
            }
        }
    }

    private ImageCallback imageCallback;

    public void getImage(ImageCallback callback) {
        imageCallback = callback;

        if (actionSheetDialog == null) {
            actionSheetDialog = new ActionSheetDialog(this)
                    .builder()
                    .setTitle("选择")
                    .addSheetItem("相机", ActionSheetDialog.SheetItemColor.Orange,
                            i -> rxPermissions.request(Manifest.permission.CAMERA)
                                    .subscribe(granted -> {
                                        if (granted) {
                                            takePhoto();
                                        } else {
                                            showMessage("没有相机权限");
                                        }
                                    }))
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Orange,
                            i -> rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .subscribe(granted -> {
                                        if (granted) {
                                            selectPhoto();
                                        } else {
                                            showMessage("没有SD卡权限");
                                        }
                                    }));
        }
        actionSheetDialog.show();
    }

    public interface ImageCallback {
        void callback(Uri imageUri);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showLoading() {
        if (!isShowLoading()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new LodingDialog(this);
        }
        hideLoading();
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    /**
     * 子类重新来显示loading
     * @return 是否显示loading
     */
    protected boolean isShowLoading() {
        return false;
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    private void showErrorSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.colorFullRed);
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    private void showSuccessSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.device_dispenser);
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    private void showSuccessToast(String message) {
        if (toast == null) {
            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.view_toast, null, false);
        TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);
        tv_content.setText(message);
        toast.setView(layout);
        toast.show();
    }

    private void showSuccessToast(int message) {
        showSuccessToast(getString(message));
    }

    private void showErrorToast(String message) {
        if (toast == null) {
            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.view_toast, null, false);
        TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);
        tv_content.setBackgroundColor(ContextCompat.getColor(this, R.color.colorFullRed));
        tv_content.setText(message);
        toast.setView(layout);
        toast.show();
    }

    private void showErrorToast(int message) {
        showErrorToast(getString(message));
    }

    @Override
    public void onError(String message) {
        if (message != null) {
//            showSnackBar(message);
//            showErrorSnackBar(message);
            showErrorToast(message);
        } else {
//            showSnackBar(getString(R.string.some_error));
//            showErrorSnackBar(getString(R.string.some_error));
            showErrorToast(getString(R.string.some_error));
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void onSuccess(@StringRes int resId) {
        onSuccess(getString(resId));
    }

    @Override
    public void onSuccess(String message) {
        if (message != null) {
//            showSuccessSnackBar(message);
            showSuccessToast(message);
        } else {
//            showSuccessSnackBar(getString(R.string.some_error));
            showSuccessToast(getString(R.string.some_error));
        }
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isWifiConnected() {
        return NetworkUtil.isWifiConnected(getApplicationContext());
    }

    @Override
    public boolean isNetworkAvailable() {
        return NetworkUtil.isAvailable(getApplicationContext());
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    protected abstract void setUp();

    /**
     * 启动activity完成跳转
     *
     * @param activity 当前activity
     * @param clazz    目标activity class
     */
    public void startActivity(AppCompatActivity activity, Class<?> clazz) {
        startActivity(activity, clazz, null);
    }

    /**
     * 启动activity完成跳转
     *
     * @param activity 当前activity
     * @param clazz    目标activity class
     * @param extraMap 捆绑参数
     */
    public void startActivity(AppCompatActivity activity, Class<?> clazz, Map<String, ? extends Serializable> extraMap) {
        Intent intent = new Intent();
        intent.setClass(activity, clazz);
        if (null != extraMap) {
            for (Map.Entry<String, ? extends Serializable> entry : extraMap.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        startActivity(intent);
    }

    // 单击回退按钮返回
    @OnClick(R.id.iv_back)
    @Optional
    void back() {
        super.onBackPressed();
//        finish();
    }

    @Override
    public void getBlePermission() {
        RxPermissions rxPermissions = RxPermissions.getInstance(this);
        rxPermissions.request(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_BLE);
                        Log.i(TAG, "动态授权蓝牙操作成功！");
                    } else {
                        Log.e(TAG, "动态授权蓝牙操作失败！");
                    }
                });
    }

    @Override
    public boolean isBleOpen() {
        // 确保蓝牙适配器处于打开状态
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return null != adapter && adapter.isEnabled();
    }

    public void setBleCallback(Callback callback) {
        this.blePermissonCallback = callback;
    }


    @Override
    public void redirectToLogin() {
        startActivity(this, LoginActivity.class);
    }

    @Override
    public void post(Runnable task) {
        this.runOnUiThread(task);
    }

    public interface Callback {
        void execute();
    }
}
