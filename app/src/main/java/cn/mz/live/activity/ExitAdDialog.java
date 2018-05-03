package cn.mz.live.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

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

public class ExitAdDialog extends FragmentActivity {

    @BindView(R.id.ad_pic_e)
    BGAImageView pic;
    @BindView(R.id.ad_title)
    TextView title_tv;
    private int code;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_ad_dialog);
        ButterKnife.bind(this);
        code = getIntent().getIntExtra("code", 0);

        if (code == 1){
            title_tv.setText("发现新版本是否更新");
        }
        String[] adPicUrl = SP.getString("xgm", "miniAD").split(",");
        Random random = new Random();
        int r = random.nextInt(adPicUrl.length);
        Glide.with(this)
                .load(adPicUrl[r].trim())
                .asBitmap()
                .placeholder(R.drawable.ic_start)
                .into(pic);
    }

    @OnClick({R.id.ad_no, R.id.ad_yes})
    void onClick(View v){
        if (v.getId() == R.id.ad_yes){
            Intent mIntent = new Intent();
            mIntent.putExtra("do", true);
            if (code == 1) {
                this.setResult(1, mIntent);
            }else if (code == 2){
                this.setResult(2, mIntent);
            }
            finish();
        }else {
            Intent mIntent = new Intent();
            mIntent.putExtra("do", false);
            if (code == 1) {
                this.setResult(1, mIntent);
            }else if (code == 2){
                this.setResult(2, mIntent);
            }
            finish();
        }
    }

    @OnClick(R.id.ad_pic_e)
    void adOnClick(){
        //TODO
        //openWebView("http://www.baidu.com");
        addQQ();
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

    private void addQQ(){
        //985005958
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=907516651";
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }
}
