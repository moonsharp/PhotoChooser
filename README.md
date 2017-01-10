# PhotoChooser
Imitation of WeChat PhotoChooser,it's simple configuration and useage.It will callback the photos include source of photo and thumbnail after user choose photos. 

仿微信图片选择库，加载流畅，配置简单，调用简单，带拍照压缩功能，用户选择完图片或拍照完成后，会自动回调所选的图片，包括原图和压缩后的缩略图。

#Preview
![image](https://github.com/moonsharp/PhotoChooser/blob/master/img/read_me_01.jpg)
![image](https://github.com/moonsharp/PhotoChooser/blob/master/img/read_me_02.jpg)

#Usage

##Gradle
root gradle
```
allprojects {
    repositories { 
        jcenter()      
        maven { url "https://jitpack.io" } //在工程的root gradle下添加这一行      
    }    
}
```

and then apply it in your module:
```
compile 'com.github.moonsharp:PhotoChooser:1.0.0' //添加依赖
```
##ChoosePhoto
Just call one method,you can get the photos of user selected,like this sample:

只需调用一个方法即可跳转到图片选择库并拿到回调，回调的结果为用户所选图片的集合。如果通过拍照获取图片，则该集合只有一个元素。
PhotoResult为封装后的图片对象，从中可以拿到原图和缩略图的路径，其中缩略图经过压缩后尺寸都要小很多，可直接转换成bitmap后用于显示。
```
    PhotoChooser.getInstance().getPhotos(this, new OnGetPhotoCallback() {
        @Override
        public void onGetPhotos(List<PhotoResult> list) {

            if(list.size()==0){
                Log.d("PhotoChooser", "用户没有选择任何图片");
            }

            for (PhotoResult photoResult : list) {
                File imgFile = new File(photoResult.getPhotoPath());
                File thumbnailFile = new File(photoResult.getThumbnailPath());
                if (imgFile.exists() && thumbnailFile.exists()) {
                    Log.d("PhotoChooser", "原图路径: " + photoResult.getPhotoPath() + "缩略图路径: " + photoResult.getThumbnailPath());
                }
            }
        }

    });
```
