package kk.qisheng.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kk.qisheng.library.R;
import kk.qisheng.library.adapter.PreviewVpAdapter;
import kk.qisheng.library.bean.PhotoResult;

public class PhotoPreviewActivity extends Activity {
    private TextView tvSelectedCount;
    private ImageView ivSelecte;
    private ViewPager viewPager;
    private ArrayList<PhotoResult> mDatas;
    private int index;
    private PhotoResult mPhotoResult;
    private PreviewVpAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        initData();
        initView();
        loadPhoto();
    }


    private void initData() {
        mDatas = getIntent().getParcelableArrayListExtra("photo_preview");
        index = getIntent().getIntExtra("photo_index", 0);

        if (mDatas == null) mDatas = new ArrayList<>();
        if (mDatas.size() > index) mPhotoResult = mDatas.get(index);

        mAdapter = new PreviewVpAdapter(this, mDatas);
    }

    private void initView() {
        tvSelectedCount = (TextView) findViewById(R.id.tv_selected_count);
        ivSelecte = (ImageView) findViewById(R.id.iv_select);
        viewPager = (ViewPager) findViewById(R.id.vp_preview);

        setSelectedCount();
        ivSelecte.setSelected(mPhotoResult.isSelected());

        ivSelecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoResult.setSelected(!mPhotoResult.isSelected());
                ivSelecte.setSelected(mPhotoResult.isSelected());
                setSelectedCount();
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultBack();
                finish();
            }
        });


        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPhotoResult = mDatas.get(position);
                ivSelecte.setSelected(mPhotoResult.isSelected());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void loadPhoto() {
        viewPager.setAdapter(mAdapter);
        if (index > 0) {
            viewPager.setCurrentItem(index);
        }
    }

    private void setSelectedCount() {
        int count = 0;
        for (PhotoResult photoResult : mDatas) {
            if (photoResult.isSelected()) {
                count++;
            }
        }

        String des = "已选 " + count + " 张";
        SpannableStringBuilder style = new SpannableStringBuilder(des);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#46C20C")), 3, des.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSelectedCount.setText(style);
    }


    @Override
    public void onBackPressed() {
        setResultBack();
        super.onBackPressed();
    }

    private void setResultBack() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("photo_preview_back", mDatas);
        setResult(Activity.RESULT_OK, intent);
    }

}
