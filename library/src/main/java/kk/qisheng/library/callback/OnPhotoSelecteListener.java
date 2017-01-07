package kk.qisheng.library.callback;

import java.util.List;

import kk.qisheng.library.bean.PhotoResult;

/**
 * Created by KkQiSheng on 2017/1/5.
 */
public interface OnPhotoSelecteListener {

    void onPhotoSelected(PhotoResult photoResult, List<PhotoResult> selectedList);

}
