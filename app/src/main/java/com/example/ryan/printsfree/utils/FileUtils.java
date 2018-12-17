package com.example.ryan.printsfree.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * Created by ryan on 2018/9/13.
 */

public class FileUtils {

    public static final String ROOT_DIR = "Android/data/" + UIUtils.getPackageName();;
    public static final String DOWNLOAD_DIR = "download";
    public static final String CACHE_DIR = "cache";
    public static final String ICON_DIR = "icon";

    public static final String APP_STORAGE_ROOT = "printsfree";

    public static boolean isSDCardAvailable() {
        return  Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String getDonwloadDir() {
        return  getDir(DOWNLOAD_DIR);
    }

    public static String getCacheDir() {
        return getDir(CACHE_DIR);
    }

    public static String getIconDir() {
        return getDir(ICON_DIR);
    }

    public static String getDir(String dirName) {
        StringBuffer sb = new StringBuffer();
        if (isSDCardAvailable()) {
            sb.append(getAppExternalStoragePath());
        } else {
            sb.append(getCacheDir());
        }
        sb.append(dirName);
        sb.append(File.separator);
        String path = sb.toString();
        return creatDirs(path) ? path : null;
    }

    public static String getExternalStoragePath() {
        StringBuffer sb = new StringBuffer();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    public static String getAppExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(APP_STORAGE_ROOT);
        sb.append(File.separator);
        return sb.toString();
    }

    public static String getCachePath() {
        File f = UIUtils.getContext().getCacheDir();
        return f != null ? f.getAbsolutePath() + "/" : null;
    }

    public static boolean creatDirs(String path) {
        File f = new File(path);
        if (!f.exists() || !f.isDirectory()) {
            return f.mkdir();
        }
        return true;
    }

    public static String generateImagePathStoragePath() {
        return getDir(ICON_DIR) + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }

    public static void  startPhotoZoom(Activity activity, File scrFile, File outputFile, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(activity, scrFile), "image/*");
        intent.putExtra("crop","true");

        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);

        intent.putExtra("outputX",800);
        intent.putExtra("outputY",400);

        intent.putExtra("return-data",false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        activity.startActivityForResult(intent, requestCode);

    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath},null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static String saveBitmap(Bitmap bitmap) {
        return saveBitmapByQuality(bitmap, 100);
    }

    public static String saveBitmapByQuality(Bitmap bitmap, int quality) {
        String croppath = "";

        String cropath = "";

        try {
            File file = new File(FileUtils.generateImagePathStoragePath());

            cropath = file.getPath();
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cropath;
    }

    public static void copy(File src, File dst) throws IOException {
        FileInputStream inputStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inputStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0L,inChannel.size(), outChannel);
        inputStream.close();
        outChannel.close();
    }

    public static String getImageFileExt(String filePath) {
        HashMap<String, String> mFileTypes = new HashMap<>();
        mFileTypes.put("FFD8FF", ".jpg");
        mFileTypes.put("89504E47", ".png");
        mFileTypes.put("474946", ".gif");
        mFileTypes.put("49492A00", ".tif");
        mFileTypes.put("424D", ".bmp");
        String valaue = mFileTypes.get(getFileHeader(filePath));
        String ext = TextUtils.isEmpty(valaue) ? ".jpg" : valaue;
        return ext;
    }

    public static String getFileHeader(String filePath) {
        FileInputStream inputStream = null;
        String value = null;
        try {
            inputStream = new FileInputStream(filePath);
            byte[] b = new byte[3];
            inputStream.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {

        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        String header = builder.toString();
        return header;
    }
}
