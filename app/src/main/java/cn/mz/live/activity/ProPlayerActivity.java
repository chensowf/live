package cn.mz.live.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.jap.playerview.PlayerView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.mz.live.R;
import cn.mz.live.base.BaseActivity;

/**
 * Created by Administrator on 2018/2/3.
 */

public class ProPlayerActivity extends BaseActivity {

    @BindView(R.id.Player)
    PlayerView player;

    private String TAG=this.getClass().getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    public void initViews() {
        initPlayer();
        if (getIntent().getBooleanExtra("showAD", false)) {
            toNextActivity(PlayerAdDialog.class, false);
        }

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initToolbar() {

    }

    private void initPlayer(){
        try {
            String title = getIntent().getStringExtra("title");
            String url = getIntent().getStringExtra("purl");
            boolean isLive = getIntent().getBooleanExtra("isLive",false);

            if (isLive) {
                String icoUrl = getIntent().getStringExtra("ico");
                Log.e(TAG,"url:"+url);
                player.setLivePurl(title,url,icoUrl);
            }else {
                player.setPurl(title,url,"");
            }


        }catch (Exception e){
            showToast("视频播放出错");
            finish();
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        player.play();


    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.desdroyPlayer(true);
        }
    }

    @OnClick({R.id.btn_juBao, R.id.btn_close})
    void onClick(View v){
        if (v.getId() == R.id.btn_juBao){
            showToast("已举报");
        }
        if (v.getId() == R.id.btn_close){
            finish();
        }
    }


    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showToast("再按一次退出播放");
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return false;
    }



}
