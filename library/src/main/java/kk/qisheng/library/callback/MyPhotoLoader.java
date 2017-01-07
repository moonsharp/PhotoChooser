package kk.qisheng.library.callback;

import android.content.Context;
import android.content.CursorLoader;
import android.provider.MediaStore;

/**
 * Created by KkQiSheng on 2017/1/5.
 */
public class MyPhotoLoader extends CursorLoader {

    final String[] IMAGE_PROJECTION = new String[]{"_id", "_data", "bucket_id", "bucket_display_name", "date_added", "_size"};

    public MyPhotoLoader(Context context) {
        super(context);
        this.setProjection(this.IMAGE_PROJECTION);
        this.setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.setSortOrder("date_added DESC");
        this.setSelection("mime_type=? or mime_type=? or mime_type=? or mime_type=?");
        String[] selectionArgs;
        selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
        this.setSelectionArgs(selectionArgs);
    }

}
