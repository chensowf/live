package cn.mz.live.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;

import java.net.URLDecoder;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.imageview.BGAImageView;
import cn.mz.live.R;
import cn.mz.live.utils.SP;

/**
 * Created by Administrator on 2018/2/4.
 */

public class PlayerAdDialog extends FragmentActivity {

    @BindView(R.id.ad_pic)
    BGAImageView pic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_ad_dialog);
        ButterKnife.bind(this);

        String[] adPicUrl = SP.getString("xgm", "bigAD").split(",");
        Random random = new Random();
        int r = random.nextInt(adPicUrl.length);
        Glide.with(this)
                .load(adPicUrl[r].trim())
                .asBitmap()
                .placeholder(R.drawable.ic_start)
                .into(pic);
    }

    @OnClick(R.id.ad_close)
    void onClick(){
        finish();
    }

    @OnClick(R.id.ad_pic)
    void adOnClick(){
        openWebView("http://www.baidu.com");
    }

    private void openWebView(String url){
        try{
            url = url.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            url =  URLDecoder.decode(url, "UTF-8");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }
}
