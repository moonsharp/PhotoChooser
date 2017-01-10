package kk.qisheng.library.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by KkQiSheng on 2017/1/5.
 */
public class PhotoResult implements Serializable, Parcelable {

    private static final long serialVersionUID = 3059465810640256314L;

    private int id;

    private String photoPath;

    private String thumbnailPath;

    private boolean isSelected;

    public PhotoResult() {
    }

    public PhotoResult(int id, String photoPath) {
        this.id = id;
        this.photoPath = photoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // *********************** 序列化 *********************** //

    public static final Creator<PhotoResult> CREATOR = new Creator<PhotoResult>() {

        @Override
        public PhotoResult createFromParcel(Parcel source) {
            PhotoResult photoResult = new PhotoResult();

            photoResult.id = source.readInt();
            photoResult.photoPath = source.readString();
            photoResult.thumbnailPath = source.readString();
            photoResult.isSelected = source.readByte() != 0;

            return photoResult;
        }

        @Override
        public PhotoResult[] newArray(int size) {
            return new PhotoResult[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(photoPath);
        dest.writeString(thumbnailPath);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }


    @Override
    public int describeContents() {
        return 0;
    }
}
