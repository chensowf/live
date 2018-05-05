package cn.mz.live.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;

import java.util.List;

import cn.mz.live.R;
import cn.mz.live.bean.ImagesBean;
import cn.mz.live.bean.VideoBean;
import cn.mz.live.utils.StringUtils;

/**
 * Created by molu_ on 2018/4/22.
 */

public class LengMiImageAdapter extends CommonAdapter<ImagesBean> {
    private String TAG = this.getClass().getSimpleName();

    private final int mPhotoWidth;

    public LengMiImageAdapter(Context context, List<ImagesBean> date) {
        super(context, date, R.layout.item_leng_mi_video);
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int marginPixels = mContext.getResources().getDimensionPixelOffset(R.dimen.photo_margin_width);
        mPhotoWidth = widthPixels / 2 - marginPixels;
    }

    @Override
    public void convert(ViewHolder viewHolder, ImagesBean imagesBean) {
        int photoHeight = StringUtils.calcPhotoHeight("200*180", mPhotoWidth);
        ImageView imageView = viewHolder.getView(R.id.iv_photo);
        // 接口返回的数据有像素分辨率，根据这个来缩放图片大小
        final ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = mPhotoWidth;
        params.height = photoHeight;
        imageView.setLayoutParams(params);

        GlideUrl cookie = new GlideUrl(imagesBean.getmImagesUrl(),
                new LazyHeaders.Builder().addHeader("Referer",
                        imagesBean.getReferer()).build());
        Glide.with(mContext.getApplicationContext())
                .load(cookie)
                .placeholder(R.drawable.ic_loading)
                .override(mPhotoWidth, photoHeight)
                .into(imageView);

        viewHolder.setText(R.id.tv_title, imagesBean.getTitle());
    }
}
