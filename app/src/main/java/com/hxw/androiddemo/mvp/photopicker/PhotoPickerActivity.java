package com.hxw.androiddemo.mvp.photopicker;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.ImageCompressor;
import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hxw on 2017/5/11.
 */

public class PhotoPickerActivity extends BaseActivity {
    private static final int COMPRESS_REQUEST_CODE = 2048;

    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.btn_picker)
    Button btnPicker;

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

    }

    /**
     * 初始化，会在onCreate中执行
     */
    @Override
    public void init() {
        // 需要实现IBoxingMediaLoader
        BoxingMediaLoader.getInstance().init(new BoxingGlideLoader());
    }

    @OnClick(R.id.btn_picker)
    public void onClick() {
        BoxingConfig singleImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG)//图片单选
                .needCamera(R.drawable.ic_photo_camera)
                .withMediaPlaceHolderRes(R.drawable.ic_crop_original);
//        Boxing.of(singleImgConfig).withIntent(this, BoxingActivity.class)
//                .start(this, COMPRESS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == COMPRESS_REQUEST_CODE) {
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
        }
    }
}
