package kk.qisheng.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kk.qisheng.library.PhotoChooser;
import kk.qisheng.library.R;
import kk.qisheng.library.adapter.PhotoChooserAdapter;
import kk.qisheng.library.bean.PhotoDir;
import kk.qisheng.library.bean.PhotoResult;
import kk.qisheng.library.callback.OnCameraClickListener;
import kk.qisheng.library.callback.OnCompressImgCallback;
import kk.qisheng.library.callback.OnPhotoClickListener;
import kk.qisheng.library.callback.OnPhotoSelecteListener;
import kk.qisheng.library.callback.PhotoSearchCallback;
import kk.qisheng.library.utils.PhotoUtil;
import kk.qisheng.library.utils.ProgressDialogUtil;

public class PhotoChooserActivity extends Activity implements OnPhotoSelecteListener, OnPhotoClickListener, OnCameraClickListener {
    private RecyclerView recyclerView;
    private TextView tvSelectedCount, tvFinish;

    PhotoChooserAdapter mAdapter;
    private List<PhotoDir> mPhotoDirs = new ArrayList<>();
    private File mCameraFile;
    private PhotoResult mPhotoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_chooser);

        initData();
        initView();
        setSelectedCount(0);
    }

    private void initData() {
        mAdapter = new PhotoChooserAdapter(this, mPhotoDirs);
        mAdapter.setPhotoListener(this);
        mAdapter.setCameraListener(this);
        mAdapter.setSelecteListener(this);
        PhotoUtil.getPhotoDirs(this, new Bundle(), new PhotoSearchCallback() {
            @Override
            public void onSearchCallback(List<PhotoDir> photoDirs) {
                mPhotoDirs.clear();
                mPhotoDirs.addAll(photoDirs);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_photos);
        tvSelectedCount = (TextView) findViewById(R.id.tv_selected_count);
        tvFinish = (TextView) findViewById(R.id.tv_finish);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(PhotoChooser.COLUMN, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFinish(mAdapter.selectedPhotos);
            }
        });
    }

    private void handleFinish(List<PhotoResult> photos) {
        Intent intent = new Intent();
        intent.putExtra("photo_selected_result", (Serializable) photos);
        setResult(Activity.RESULT_OK, intent);
        EventBus.getDefault().post(intent);
        finish();
    }


    @Override
    public void onPhotoSelected(PhotoResult photoResult, List<PhotoResult> selectedList) {
        setSelectedCount(selectedList.size());
    }


    @Override
    public void onPhotoClick(PhotoResult photoResult) {
        mPhotoResult = photoResult;
        Intent intent = new Intent(this, kk.qisheng.library.activity.PhotoPreviewActivity.class);
        intent.putExtra("photo_path", photoResult.getPhotoPath());
        startActivityForResult(intent, 0x101);
    }

    @Override
    public void onCameraClick(PhotoResult photoResult) {
        mCameraFile = new File(PhotoUtil.getCameraCache(System.currentTimeMillis() + ".jpg"));
        startActivityForResult(PhotoUtil.getCameraIntent(mCameraFile), 0x102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x101:
                if (resultCode == Activity.RESULT_OK) {
                    mPhotoResult.setSelected(true);
                    mAdapter.selectedPhotos.add(mPhotoResult);
                    mAdapter.notifyDataSetChanged();
                    setSelectedCount(mAdapter.selectedPhotos.size());
                }
                break;

            case 0x102:
                List<PhotoResult> list = new ArrayList<>();
                //如果取消拍照返回，直接回调空list
                if (resultCode == 0) {
                    handleFinish(list);
                    return;
                }
                if (mCameraFile != null && mCameraFile.exists()) {
                    compressCameraPhoto(list);
                }
                break;
        }
    }

    private void compressCameraPhoto(final List<PhotoResult> list) {
        final PhotoResult photoResult = new PhotoResult(22, mCameraFile.getAbsolutePath());
        list.add(photoResult);

        ProgressDialogUtil.showDialog(this);

        PhotoUtil.compressImg(this, mCameraFile, photoResult, new OnCompressImgCallback() {
            @Override
            public void onCompressSuccess(final File file) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialogUtil.dismissDialog();

                        if (file != null && file.exists()) {
                            photoResult.setThumbnailPath(file.getAbsolutePath());
                        }
                        handleFinish(list);
                    }
                });
            }
        });
    }

    private void setSelectedCount(int count) {
        tvFinish.setEnabled(count > 0 ? true : false);
        String des = "已选 " + count + " 张";
        SpannableStringBuilder style = new SpannableStringBuilder(des);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#46C20C")), 3, des.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSelectedCount.setText(style);
    }


}
