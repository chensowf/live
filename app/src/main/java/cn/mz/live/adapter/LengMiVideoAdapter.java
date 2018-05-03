package cn.mz.live.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.mz.live.R;
import cn.mz.live.bean.VideoBean;
import cn.mz.live.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import java.util.List;

/**
 * Created by molu_ on 2018/4/22.
 */

public class LengMiVideoAdapter extends CommonAdapter<VideoBean> {


    private String TAG=this.getClass().getSimpleName();

    private final int mPhotoWidth;

    private Context mContext;

    public LengMiVideoAdapter(Context context, List<VideoBean> datas) {
        super(context, datas, R.layout.item_leng_mi_video);

        mContext=context;

        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int marginPixels = mContext.getResources().getDimensionPixelOffset(R.dimen.photo_margin_width);
        mPhotoWidth = widthPixels / 2 - marginPixels;
    }

    @Override
    public void convert(ViewHolder viewHolder, VideoBean videoBean) {

        //JZVideoPlayerStandard videoPlayerStandard;



       /* JZVideoPlayerStandard videoPlayerStandard = viewHolder.getView(R.id.videoplayer);

        Log.e(TAG,"url:"+videoBean.getVideoUrl());

        videoPlayerStandard.setUp(videoBean.getVideoUrl(),
                                  JZVideoPlayer.SCREEN_WINDOW_LIST,videoBean.getTitle());

        Glide.with(mContext.getApplicationContext())
             .load(videoBean.getImageUrl())
             .placeholder(R.drawable.ic_loading)

             .into(videoPlayerStandard.thumbImageView);*/

        int photoHeight = StringUtils.calcPhotoHeight("200*180", mPhotoWidth);

        ImageView imageView= viewHolder.getView(R.id.iv_photo);

        // 接口返回的数据有像素分辨率，根据这个来缩放图片大小
        final ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = mPhotoWidth;
        params.height = photoHeight;
        imageView.setLayoutParams(params);

        Log.e(TAG,"image:"+videoBean.getImageUrl());

        Glide.with(mContext.getApplicationContext())
             .load(videoBean.getImageUrl())
             .placeholder(R.drawable.ic_loading)
             .override(mPhotoWidth,photoHeight)
             .into(imageView);

        viewHolder.setText(R.id.tv_title,videoBean.getTitle());

    }
}
