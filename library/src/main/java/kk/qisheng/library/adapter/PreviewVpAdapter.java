package kk.qisheng.library.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import kk.qisheng.library.R;
import kk.qisheng.library.bean.PhotoResult;
import kk.qisheng.library.view.ZoomImageView;

/**
 * Created by KkQiSheng on 2017/1/10.
 */
public class PreviewVpAdapter extends PagerAdapter {
    private Context mContext;
    private List<PhotoResult> mDatas;

    public PreviewVpAdapter(Context context, List<PhotoResult> list) {
        mContext = context;
        mDatas = list;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_preview, null);
        ZoomImageView iv = (ZoomImageView) view.findViewById(R.id.iv_preview);
        loadPhoto(iv, mDatas.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void loadPhoto(ZoomImageView iv, PhotoResult photoResult) {
        if (iv == null || photoResult == null || TextUtils.isEmpty(photoResult.getPhotoPath())) {
            return;
        }

        Glide.with(mContext).load(photoResult.getPhotoPath()).override(320, 480).placeholder(R.drawable.photo_loading).into(iv);
    }

}
