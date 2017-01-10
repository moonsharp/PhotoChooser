package kk.qisheng.library.callback;

import java.util.ArrayList;

import kk.qisheng.library.bean.PhotoResult;

/**
 * Created by KkQiSheng on 2017/1/6.
 */
public interface OnGetPhotoCallback {

    void onGetPhotos(ArrayList<PhotoResult> photoResults);

}
