package kk.qisheng.library;

import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import kk.qisheng.library.activity.PhotoChooserActivity;
import kk.qisheng.library.bean.PhotoResult;
import kk.qisheng.library.callback.OnGetPhotoCallback;

/**
 * Created by KkQiSheng on 2017/1/5.
 */
public class PhotoChooser {

    private PhotoChooser() {
        EventBus.getDefault().register(this);
    }

    //每一行显示的照片列数
    public static final int COLUMN = 3;
    //拍照取缩略图时压缩的目标宽度
    public static final int THUMBNAIL_WIDTH = 320;
    //拍照取缩略图时压缩的目标高度
    public static final int THUMBNAIL_HEIGHT = 480;

    private OnGetPhotoCallback mCallback;
    private static PhotoChooser mInstance;

    public static PhotoChooser getInstance() {
        if (mInstance == null) {
            synchronized (PhotoChooser.class) {
                if (mInstance == null) {
                    mInstance = new PhotoChooser();
                }
            }
        }
        return mInstance;
    }


    public void getPhotos(Context context, OnGetPhotoCallback callback) {
        Intent intent = new Intent(context, PhotoChooserActivity.class);
        context.startActivity(intent);
        mCallback = callback;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPhotoBack(Intent data) {
        List<PhotoResult> list = (List<PhotoResult>) data.getSerializableExtra("photo_selected_result");
        if (mCallback != null) mCallback.onGetPhotos(list);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
    }
}
