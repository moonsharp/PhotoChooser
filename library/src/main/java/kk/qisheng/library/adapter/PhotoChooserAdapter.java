package kk.qisheng.library.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import kk.qisheng.library.PhotoChooser;
import kk.qisheng.library.R;
import kk.qisheng.library.bean.PhotoDir;
import kk.qisheng.library.bean.PhotoResult;
import kk.qisheng.library.callback.OnCameraClickListener;
import kk.qisheng.library.callback.OnPhotoClickListener;
import kk.qisheng.library.callback.OnPhotoSelecteListener;
import kk.qisheng.library.utils.PhotoUtil;

/**
 * Created by KkQiSheng on 2016/12/28.
 */
public class PhotoChooserAdapter extends RecyclerView.Adapter<PhotoChooserAdapter.PhotoViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<PhotoDir> photoDirs = new ArrayList();
    public List<PhotoResult> selectedPhotos = new ArrayList<>();
    private int photoSize;
    private OnPhotoClickListener mPhotoListener;
    private OnCameraClickListener mCameraListener;
    private OnPhotoSelecteListener mSelecteListener;

    public PhotoChooserAdapter(Context context, List<PhotoDir> photoDirectories) {
        this.mContext = context;
        this.photoDirs = photoDirectories;
        this.inflater = LayoutInflater.from(context);
        setPhotoSize(context);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (photoDirs != null && photoDirs.size() > 0 && photoDirs.get(0).getPhotos() != null) {
            count = photoDirs.get(0).getPhotos().size();
        }
        return count + 1;
    }

    @Override
    public PhotoChooserAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = this.inflater.inflate(R.layout.item_photo_chooser, parent, false);
        PhotoChooserAdapter.PhotoViewHolder holder = new PhotoChooserAdapter.PhotoViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PhotoChooserAdapter.PhotoViewHolder holder, final int position) {
        PhotoResult photo = new PhotoResult();
        if (position > 0) {
            photo = photoDirs.get(0).getPhotos().get(position - 1);
        }

        final PhotoResult photoResult = photo;

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (position == 0) {
                    if (mCameraListener != null) mCameraListener.onCameraClick(photoResult);
                } else {
                    if (mPhotoListener != null) mPhotoListener.onPhotoClick(photoResult);
                }
            }
        });

        holder.vSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (photoResult.isSelected()) {
                    selectedPhotos.remove(photoResult);
                } else {
                    selectedPhotos.add(photoResult);
                }
                photoResult.setSelected(!photoResult.isSelected());
                int pos = holder.getAdapterPosition();
                notifyItemChanged(pos);
                if (mSelecteListener != null)
                    mSelecteListener.onPhotoSelected(photoResult, selectedPhotos);
            }
        });

        //相机
        if (position == 0) {
            holder.ivPhoto.setImageResource(R.drawable.photo_camera);
            holder.vSelected.setVisibility(View.GONE);
            return;
        }

        holder.vSelected.setVisibility(View.VISIBLE);

        loadPhoto(photoResult, holder.ivPhoto);

        holder.vSelected.setSelected(photoResult.isSelected());

        if (photoResult.isSelected()) {
            holder.ivPhoto.setColorFilter(Color.parseColor("#77000000"));
        } else {
            holder.ivPhoto.setColorFilter(null);
        }

    }

    private void loadPhoto(final PhotoResult photoResult, final ImageView iv) {
        Glide.with(mContext).load(photoResult.getPhotoPath()).asBitmap()
                .centerCrop().dontAnimate().thumbnail(0.5F).override(photoSize, photoSize)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(R.drawable.photo_loading)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        iv.setImageBitmap(resource);
                        PhotoUtil.bitmap2File(mContext, resource, photoResult, null);
                    }
                });
    }

    private void setPhotoSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        this.photoSize = widthPixels / PhotoChooser.COLUMN;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            this.ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            this.vSelected = itemView.findViewById(R.id.v_selected);
        }
    }

    public void setPhotoListener(OnPhotoClickListener listener) {
        this.mPhotoListener = listener;
    }

    public void setCameraListener(OnCameraClickListener listener) {
        this.mCameraListener = listener;
    }

    public void setSelecteListener(OnPhotoSelecteListener listener) {
        this.mSelecteListener = listener;
    }


}
