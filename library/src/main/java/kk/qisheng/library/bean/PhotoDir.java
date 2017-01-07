package kk.qisheng.library.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by KkQiSheng on 2017/1/5.
 */
public class PhotoDir {
    private String id;
    private String coverPath;
    private String name;
    private long dateAdded;
    private List<PhotoResult> photos = new ArrayList();

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof PhotoDir)) {
            return false;
        } else {
            PhotoDir directory = (PhotoDir)o;
            boolean hasId = !TextUtils.isEmpty(this.id);
            boolean otherHasId = !TextUtils.isEmpty(directory.id);
            return hasId && otherHasId?(!TextUtils.equals(this.id, directory.id)?false:TextUtils.equals(this.name, directory.name)):false;
        }
    }

    public int hashCode() {
        if(TextUtils.isEmpty(this.id)) {
            return TextUtils.isEmpty(this.name)?0:this.name.hashCode();
        } else {
            int result = this.id.hashCode();
            if(TextUtils.isEmpty(this.name)) {
                return result;
            } else {
                result = 31 * result + this.name.hashCode();
                return result;
            }
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return this.coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<PhotoResult> getPhotos() {
        return this.photos;
    }

    public void setPhotos(List<PhotoResult> photos) {
    }

    public List<String> getPhotoPaths() {
        ArrayList paths = new ArrayList(this.photos.size());
        Iterator iterator = this.photos.iterator();

        while(iterator.hasNext()) {
            PhotoResult photo = (PhotoResult)iterator.next();
            paths.add(photo.getPhotoPath());
        }

        return paths;
    }

    public void addPhoto(int id, String path) {
        this.photos.add(new PhotoResult(id, path));
    }

}
