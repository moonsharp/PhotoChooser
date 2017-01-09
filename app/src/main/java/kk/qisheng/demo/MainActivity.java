package kk.qisheng.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;

import kk.qisheng.library.PhotoChooser;
import kk.qisheng.library.bean.PhotoResult;
import kk.qisheng.library.callback.OnGetPhotoCallback;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChooser();
            }
        });

    }

    private void openChooser() {
        PhotoChooser.getInstance().getPhotos(this, new OnGetPhotoCallback() {
            @Override
            public void onGetPhotos(List<PhotoResult> photoResults) {
                showResults(photoResults);
            }
        });
    }


    private void showResults(List<PhotoResult> photoResults) {
        Log.d("kkqisheng", "selected count: " + photoResults.size());
        for (PhotoResult photoResult : photoResults) {
            File imgFile = new File(photoResult.getPhotoPath());
            File thumbnailFile = new File(photoResult.getThumbnailPath());
            if (imgFile.exists() && thumbnailFile.exists()) {
                Log.d("kkqisheng", "photoPath: " + photoResult.getPhotoPath() + " -- thumbnailPath: " + photoResult.getThumbnailPath());
            }
        }
    }

}
