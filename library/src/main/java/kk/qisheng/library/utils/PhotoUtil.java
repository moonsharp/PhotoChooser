package kk.qisheng.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kk.qisheng.library.PhotoChooser;
import kk.qisheng.library.bean.PhotoResult;
import kk.qisheng.library.callback.OnCompressImgCallback;
import kk.qisheng.library.callback.PhotoLoaderCallback;
import kk.qisheng.library.callback.PhotoSearchCallback;

/**
 * Created by KkQiSheng on 2017/1/5.
 */
public class PhotoUtil {

    public static void getPhotoDirs(Activity activity, Bundle args, PhotoSearchCallback searchCallback) {
        activity.getLoaderManager().initLoader(0, args, new PhotoLoaderCallback(activity, searchCallback));
    }


    public static void bitmap2File(final Context context, final Bitmap bitmap, final PhotoResult photoResult, final OnCompressImgCallback callback) {
        new Thread() {

            @Override
            public void run() {

                File thumbnialFile = new File(getCachePath(context, photoResult.getPhotoPath()));

                if (setThumbnailPath(photoResult, thumbnialFile, callback)) {
                    return;
                }

                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(thumbnialFile));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();

                    setThumbnailPath(photoResult, thumbnialFile, callback);

                } catch (IOException e) {
                    e.printStackTrace();
                    setThumbnailPath(photoResult, new File(photoResult.getPhotoPath()), callback);
                } finally {
//                    bitmap.recycle();
                }
            }

        }.start();

    }

    private static boolean setThumbnailPath(PhotoResult photoResult, File file, OnCompressImgCallback callback) {
        if (file.exists()) {
            photoResult.setThumbnailPath(file.getAbsolutePath());

            if (callback != null) callback.onCompressSuccess(file);

            return true;
        }

        return false;
    }

    public static String getCachePath(Context context, String oldPath) {
        String newPath = oldPath;
        try {
            int start = oldPath.lastIndexOf("/");
            String photoName = start > 0 ? oldPath.substring(start + 1, oldPath.length()) : oldPath;
            File showDir = new File(context.getCacheDir().getAbsolutePath() + "/photo_chooser_img");
            if (!showDir.exists()) {
                showDir.mkdirs();
            }

            newPath = showDir.getAbsolutePath() + "/" + photoName;

        } catch (Exception e) {
        }
        return newPath;
    }

    public static String getCameraCache(String imgName) {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        String imgCacheDir = rootPath + "/photo_chooser_img";
        File dirFile = new File(imgCacheDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile.getAbsolutePath() + "/" + imgName;
    }

    /**
     * 打开相机获取照片
     *
     * @return
     */
    public static Intent getCameraIntent(File saveFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(saveFile);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static void compressImg(final Context context, final File file, final PhotoResult photoResult, final OnCompressImgCallback callback) {
        new Thread() {

            @Override
            public void run() {
                doCompressImg(context, file, photoResult, callback);
            }
        }.start();
    }

    public static void doCompressImg(Context context, File file, PhotoResult photoResult, OnCompressImgCallback callback) {
        BitmapFactory.Options opts = new BitmapFactory.Options();// 解析图片的选项参数
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        // 2.得到图片的宽高属性。
        opts.inJustDecodeBounds = true;// 不真正的解析这个bitmap ，只是获取bitmap的宽高信息
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

        int imageHeight = opts.outHeight;
        int imageWidth = opts.outWidth;
        // 3.计算缩放比例。
        int dx = imageWidth / PhotoChooser.THUMBNAIL_WIDTH;
        int dy = imageHeight / PhotoChooser.THUMBNAIL_HEIGHT;

        int scale = 1;
        if (dx >= dy && dx >= 1) {
            scale = dy;
        }
        if (dy >= dx && dx >= 1) {
            scale = dx;
        }

        opts.inJustDecodeBounds = false;// 真正的解析bitmap
        opts.inSampleSize = scale; // 指定图片缩放比例

        Bitmap resultBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

        //图片是否放生了旋转
        int imgDegree = getImgDegree(file.getAbsolutePath());
        if (imgDegree >= 0) {
            resultBitmap = rotateBitmapByDegree(resultBitmap, imgDegree);
        }

        bitmap2File(context, resultBitmap, photoResult, callback);
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getImgDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

}
