package kk.qisheng.library.bean;

import java.io.Serializable;

/**
 * Created by KkQiSheng on 2017/1/5.
 */
public class PhotoResult implements Serializable{

    private static final long serialVersionUID = 3059465810640256314L;

    private int id;

    private String photoPath;

    private String thumbnailPath;

    private boolean isSelected;

    public PhotoResult(){
    }

    public PhotoResult(int id,String photoPath){
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

}
