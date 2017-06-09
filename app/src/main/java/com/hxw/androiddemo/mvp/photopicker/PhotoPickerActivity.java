package com.hxw.androiddemo.mvp.photopicker;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.ImageCompressor;
import com.bumptech.glide.Glide;
import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于相册和头像获取，还可以看第一行代码第二版的第八章
 * Created by hxw on 2017/5/11.
 */

public class PhotoPickerActivity extends BaseActivity {
    private static final int COMPRESS_REQUEST_CODE = 2048;
    private static final int READ_REQUEST_CODE = 42;
    private static final int MYDIY = 8;
    private static final int TAKE_PICTURE = 4;
    private static final int CROP = 16;
    private int reCode = 2;
    @Inject
    File cacheFile;
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.btn_picker)
    Button btnPicker;
    @BindView(R.id.btn_camera)
    Button btnCamera;

    private Uri tempUri;

    /**
     * @return 返回布局资源ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_image_picker;
    }

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent
     */
    @Override
    public void componentInject(AppComponent appComponent) {
        DaggerPhotoPickerComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    /**
     * 初始化，会在onCreate中执行
     */
    @Override
    public void init(Bundle savedInstanceState) {
        // 需要实现IBoxingMediaLoader
        BoxingMediaLoader.getInstance().init(new BoxingGlideLoader());
    }

    /*  权限校验  */
    private boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //校验是否已具有拍照权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        reCode);
                return false;
            } else {
                //具有权限
                return true;
            }
        } else {
            //系统不高于6.0直接执行
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == reCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意权限

            } else {
                // 权限拒绝，提示用户开启权限

            }
        }
    }

    @OnClick({R.id.btn_picker, R.id.btn_camera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_picker:
//        BoxingConfig singleImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG)//图片单选
//                .needCamera(R.drawable.ic_photo_camera)
//                .withMediaPlaceHolderRes(R.drawable.ic_crop_original);
//        Boxing.of(singleImgConfig).withIntent(this, BoxingActivity.class)
//                .start(this, COMPRESS_REQUEST_CODE);

//        startActivityForResult(new Intent(this, PhotoPickActivity.class),MYDIY);

                //调用系统取选取,只能单张,属于是文件选取
                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                // browser.
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
//                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
                break;
            case R.id.btn_camera:
                if (!checkCameraPermission()) {
                    return;
                }

                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                tempUri = Uri.fromFile(new File(Environment
//                        .getExternalStorageDirectory(), "image.jpg"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0系统适配
                    //转变成Content uri
                    tempUri = FileProvider.getUriForFile(PhotoPickerActivity.this,
                            getPackageName() + ".provider", new File(cacheFile, "head.jpg"));
                } else {
                    //file uri
                    tempUri = Uri.fromFile(new File(cacheFile, "head.jpg"));
                }

                // 指定照片保存路径（SD卡），head.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == COMPRESS_REQUEST_CODE) {//bilibili的实现返回
            ArrayList<BaseMedia> medias = Boxing.getResult(data);
            BaseMedia baseMedia = medias.get(0);
            if (!(baseMedia instanceof ImageMedia)) {
                return;
            }
            ImageMedia imageMedia = (ImageMedia) baseMedia;
            // the compress task may need time
            if (imageMedia.compress(new ImageCompressor(this))) {
                imageMedia.removeExif();
            }
            BoxingMediaLoader.getInstance().displayThumbnail(imgHead, imageMedia.getThumbnailPath(), 150, 150);
        } else if (resultCode == RESULT_OK && requestCode == MYDIY) {//自己实现的返回
            List<com.hxw.frame.loader.media.ImageMedia> list = data.getParcelableArrayListExtra("result");
            Glide.with(this)
                    .load("file://" + list.get(0).getPath())
                    .into(imgHead);
        } else if (resultCode == RESULT_OK && requestCode == READ_REQUEST_CODE) {//系统选择的返回
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    crop(uri);
                } else {
                    //这里getUriFile适用于Intent.ACTION_PICK,为简略版
                    //用Intent.ACTION_OPEN_DOCUMENT或Intent.ACTION_GET_CONTENT时用完整版
                    crop(Uri.fromFile(getUriFile(uri)));
                }

//                imgHead.setImageURI(uri);
            }
        } else if (resultCode == RESULT_OK && requestCode == TAKE_PICTURE) {//系统拍照的返回

            crop(tempUri);

//            imgHead.setImageURI(tempUri);
        } else if (resultCode == RESULT_OK && requestCode == CROP) {//图片剪裁后
            //大图用uri,图小可用bitmap
            Bitmap bitmap = data.getParcelableExtra("data");
            if (bitmap != null) {
                imgHead.setImageBitmap(bitmap);
                return;
            }
            //前后设置的uri相同的话，虽然内容改变了，但imageView不会放上新图
            imgHead.setImageURI(tempUri);
        }
    }


    /**
     * 系统的图片剪裁
     * Android7.0 前适用File Uri,之后适用Content Uri,从相册选取的在4.4以上就变成了Content Uri
     *
     * @param uri
     */
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//为7.0适配

        // 设置裁剪
        //下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //设置输出图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        //是否将数据保留在Bitmap中返回,也可以说是是否返回数据
        //为false时需和intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)一同使用
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        //是否缩放
        intent.putExtra("scale", true);
//        //输入图片的Uri，指定以后，可以在这个uri获得图片
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //是否是圆形裁剪区域，设置了也不一定有效
//        intent.putExtra("circleCrop", true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0系统适配
//            tempUri = FileProvider.getUriForFile(PhotoPickerActivity.this,
//                    getPackageName() + ".provider", new File(cacheFile, "head.jpg"));
//        } else {
        tempUri = Uri.fromFile(new File(cacheFile, "head.jpg"));//file uri
//        }
        //return-data为false时使用
        // 指定照片保存路径（SD卡），head.jpg为一个临时文件，每次拍照后这个图片都会被替换
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(intent, CROP);
    }


    /**
     * 这是简略的，根据uri获取图片路径,要4.4以上
     *
     * @param uri
     * @return
     */
    private File getUriFile(Uri uri) {
        Cursor cursor = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        try {
            cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(column_index);
                return new File(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 这是完整的根据uri获取路径,兼容任何情况
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;

        final String[] projection = {MediaStore.Images.Media.DATA};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    //第一行代码第二版上的代码
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, projection, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imgHead.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}
