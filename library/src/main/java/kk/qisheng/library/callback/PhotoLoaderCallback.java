package kk.qisheng.library.callback;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kk.qisheng.library.bean.PhotoDir;

/**
 * Created by KkQiSheng on 2017/1/5.
 */
public class PhotoLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
    private WeakReference<Context> mContext;
    private PhotoSearchCallback mSearchCallback;

    public PhotoLoaderCallback(Context context, PhotoSearchCallback searchCallback) {
        this.mContext = new WeakReference(context);
        this.mSearchCallback = searchCallback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyPhotoLoader(mContext.get());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return;

        List<PhotoDir> dirResult = new ArrayList();
        PhotoDir dirAll = new PhotoDir();
        dirAll.setName("all dir");
        dirAll.setId("ALL");

        while(data.moveToNext()) {
            int imageId = data.getInt(data.getColumnIndexOrThrow("_id"));
            String bucketId = data.getString(data.getColumnIndexOrThrow("bucket_id"));
            String name = data.getString(data.getColumnIndexOrThrow("bucket_display_name"));
            String path = data.getString(data.getColumnIndexOrThrow("_data"));
            long size = (long)data.getInt(data.getColumnIndexOrThrow("_size"));
            if(size >= 1L) {
                PhotoDir dir = new PhotoDir();
                dir.setId(bucketId);
                dir.setName(name);
                if(!dirResult.contains(dir)) {
                    dir.setCoverPath(path);
                    dir.addPhoto(imageId, path);
                    dir.setDateAdded(data.getLong(data.getColumnIndexOrThrow("date_added")));
                    dirResult.add(dir);
                } else {
                    ((PhotoDir)dirResult.get(dirResult.indexOf(dir))).addPhoto(imageId, path);
                }

                dirAll.addPhoto(imageId, path);
            }
        }

        if(dirAll.getPhotoPaths().size() > 0) {
            dirAll.setCoverPath((String)dirAll.getPhotoPaths().get(0));
        }

        dirResult.add(0, dirAll);

        if (mSearchCallback != null) {
            mSearchCallback.onSearchCallback(dirResult);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
