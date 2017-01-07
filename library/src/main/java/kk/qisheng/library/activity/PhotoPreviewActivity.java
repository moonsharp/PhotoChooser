package kk.qisheng.library.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import kk.qisheng.library.R;
import kk.qisheng.library.view.ZoomImageView;

public class PhotoPreviewActivity extends Activity {
    private ZoomImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        initView();
        loadPhoto();
    }

    private void initView() {
        iv = (ZoomImageView) findViewById(R.id.iv_preview);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tv_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private void loadPhoto() {
        String path = getIntent().getStringExtra("photo_path");
        if (!TextUtils.isEmpty(path)) {
            Glide.with(this).load(path).override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).into(iv);
        }
    }

}
