package com.jap.playerview;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aplayer.aplayerandroid.APlayerAndroid;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerView extends RelativeLayout implements View.OnClickListener,
        APlayerAndroid.OnPlayStateChangeListener,APlayerAndroid.OnBufferListener,
        APlayerAndroid.OnPlayCompleteListener,APlayerAndroid.OnOpenSuccessListener{

    private final String TAG = "PlayerView";
    private Context mcontext;
    private APlayerAndroid AP;
    private CtrlBar ctrlBar = null;
    private View view_TopBar,view_LiveTopBar,view_OnBuffer,view_SeekTo,view_volume_brightness;
    private SurfaceView playerView;
    private TextView PlayTitle,LiveTitle,buffer_num_tv,onbuffer_txt_tv,
            volume_brightness_tv,seektosecond,seekposition;
    private ImageView PlayerCloseR,LiveClose,LiveIco,volume_brightness_icon;

    private Timer barTimer;
    private GestureDetector mGestureDetector;
    private float brightness = -1;
    private int volume = -1;
    private int newPosition = -1;
    private int screenWidthPixels;
    private Activity activity;

    private AudioManager audioManager;
    private PowerManager.WakeLock mWakeLock = null;
    private int mMaxVolume;
    private String purl;

    private static final int MESSAGE_SEEK_NEW_POSITION = 3;//滑动结束改变播放进度
    private static final int MESSAGE_HIDE_CENTER_BOX = 4;

    private boolean isLive, isLock;
    private long lastTimeStamp = 0;
    private long lastTotalRxBytes = 0;

    public PlayerView(Context context) {
        super(context);
        mcontext = context;
        activity = (Activity) mcontext;
        initPlayer();
        intView();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        activity = (Activity) mcontext;
        initPlayer();
        intView();
    }

    private void initPlayer(){
        AP = new APlayerAndroid();
        AP.setConfig(APlayerAndroid.CONFIGID.AUTO_PLAY,"1");//自动播放
        AP.setOnPlayCompleteListener(this);
        AP.setOnPlayStateChangeListener(this);
        AP.setOnBufferListener(this);
        AP.setOnOpenSuccessListener(this);
    }

    private void intView(){

        PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);

        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        screenWidthPixels = getResources().getDisplayMetrics().widthPixels;
        mGestureDetector = new GestureDetector(mcontext, new MyGestureListener());

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_player, this);
        view_TopBar = layout.findViewById(R.id.PlayTopBar);
        PlayTitle = (TextView) layout.findViewById(R.id.PlayTitle);
        PlayerCloseR = (ImageView) layout.findViewById(R.id.PlayCloseR);
        view_LiveTopBar = layout.findViewById(R.id.LiveTopBar);
        LiveTitle = (TextView) layout.findViewById(R.id.LiveTitle);
        LiveIco = (ImageView) layout.findViewById(R.id.LiveIco);
        LiveClose = (ImageView) findViewById(R.id.LiveClose);
        view_OnBuffer = layout.findViewById(R.id.view_center_onbuffer);
        buffer_num_tv = (TextView) layout.findViewById(R.id.onbuffer_num_tv);
        onbuffer_txt_tv = (TextView) layout.findViewById(R.id.onbuffer_txt_tv);
        view_volume_brightness = layout.findViewById(R.id.view_center_volume_brightness);
        volume_brightness_icon = (ImageView) layout.findViewById(R.id.volume_brightness_icon);
        volume_brightness_tv = (TextView) layout.findViewById(R.id.volume_brightness_tv);
        view_SeekTo = layout.findViewById(R.id.view_center_seekto);
        seektosecond = (TextView) layout.findViewById(R.id.seektosecond_tv);
        seekposition = (TextView) layout.findViewById(R.id.seekposition_tv);
        playerView = (SurfaceView) layout.findViewById(R.id.playerView);
        ctrlBar = (CtrlBar) layout.findViewById(R.id.ctrl_bar);
        ctrlBar.setCtrlBar(AP);
        AP.setView(playerView);
        PlayerCloseR.setOnClickListener(this);
        LiveClose.setOnClickListener(this);
        aouterHide();
    }

    /**
     * 销毁播放器*/
    public void desdroyPlayer(boolean isuClose){
        if (AP != null){
            AP.close();
            AP.destroy();
            if (!isuClose) {
                Toast.makeText(mcontext, "视频播放出错", Toast.LENGTH_SHORT).show();
            }
        }
        activity.finish();
    }

    public void setPurl(String title,String url,String cookie){
        if (!cookie.isEmpty()) {
            AP.setConfig(APlayerAndroid.CONFIGID.HTTP_COOKIE, cookie);
        }
        purl = url.replace("https","http");
        AP.open(purl);
        view_OnBuffer.setVisibility(View.VISIBLE);
        onbuffer_txt_tv.setText("稍等片刻");
        buffer_num_tv.setText("精彩马上开始");

        PlayTitle.setText(title);
    }

    public void setLivePurl(String title,String url,String icoUrl){
        isLive = true;
        purl = url.replace("https","http");
        AP.open(purl);
        view_OnBuffer.setVisibility(View.VISIBLE);
        onbuffer_txt_tv.setText("稍等片刻");
        buffer_num_tv.setText("精彩马上开始");
        view_TopBar.setVisibility(View.GONE);
        view_LiveTopBar.setVisibility(View.VISIBLE);
        Glide.with(activity).load(icoUrl).asBitmap().placeholder(R.drawable.ic_download_empty).into(LiveIco);

        LiveTitle.setText(title);
        ctrlBar.hide();
    }


    public void play(){
        if (null != mWakeLock && (!mWakeLock.isHeld())) {
            mWakeLock.acquire();
        }
        if (AP != null){
            AP.play();
        }
    }

    public void pause(){
        if (AP != null){
            AP.pause();
        }
    }

    public void setIsLock(boolean isLock){
        this.isLock = isLock;
    }

    public boolean getIsLock(){
        return this.isLock;
    }

    /**
     * 关闭按钮*/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.PlayCloseR){
            desdroyPlayer(true);
        }
        if (v.getId() == R.id.LiveClose){
            desdroyPlayer(true);
        }
    }

    /**
     * 播放器回调事件*/
    @Override
    public void onBuffer(int percent) {
        if (AP != null && ctrlBar != null) {
            if (percent < 99) {
                view_OnBuffer.setVisibility(View.VISIBLE);
                onbuffer_txt_tv.setText("正在缓冲..." + getNetSpeed());
                buffer_num_tv.setText(percent + "%");
            }else {
                view_OnBuffer.setVisibility(View.GONE);
            }
            Log.i(TAG, "onBuffer: "+percent);
        }
    }

    @Override
    public void onOpenSuccess() {
        Log.i(TAG, "onOpenSuccess: ");
        ctrlBar.setPurl(purl);
        view_OnBuffer.setVisibility(View.GONE);
    }

    @Override
    public void onPlayComplete(String playRet) {
        switch (playRet){
            case APlayerAndroid.PlayCompleteRet.PLAYRE_RESULT_COMPLETE:
                AP.pause();
                break;
            case APlayerAndroid.PlayCompleteRet.PLAYRE_RESULT_CLOSE:
                break;
            default:
                //错误
                Log.i(TAG, "onPlayComplete: "+playRet);
                desdroyPlayer(false);
                break;
        }

        //Log.i(TAG, "onPlayComplete: "+playRet);
    }

    @Override
    public void onPlayStateChange(int newState, int oldState) {
        if (ctrlBar != null) {
            ctrlBar.changeState();
        }
    }

    /**
     * 手势处理*/

    private void aouterHide() {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                hideBar();
            }

        }, 3 * 1000);

    }

    private void toggle() {
        if (isLive){
            if (view_LiveTopBar.getVisibility() == VISIBLE){
                hideBar();
            }else {
                showBar();
                aouterHide();
            }
        }else {
            if (ctrlBar != null && ctrlBar.getVisibility() == View.VISIBLE) {
                hideBar();
            } else {
                showBar();
                aouterHide();
            }
        }

    }

    private void showBar(){
        ctrlBar.getThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if (isLive){
                    view_LiveTopBar.setVisibility(View.VISIBLE);
                }else {
                    view_TopBar.setVisibility(View.VISIBLE);
                    ctrlBar.show();
                }
                playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            }

        });

    }

    private void hideBar(){
        ctrlBar.getThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if (isLive){
                    view_LiveTopBar.setVisibility(View.GONE);
                }else {
                    view_TopBar.setVisibility(View.GONE);
                    ctrlBar.hide();
                }
                playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            }

        });
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (volume == -1) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume < 0)
                volume = 0;
        }

        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "0%";
        }
        // 显示centerbox
        view_volume_brightness.setVisibility(View.VISIBLE);
        volume_brightness_icon.setImageResource(i == 0 ? R.drawable.ic_volume_off_white : R.drawable.ic_volume_up_white);
        volume_brightness_tv.setText(s);
    }

    /**
     * 滑动改变亮度
     */
    private void onBrightnessSlide(float percent) {
        if (brightness < 0) {
            brightness = activity.getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f) {
                brightness = 0.50f;
            } else if (brightness < 0.01f) {
                brightness = 0.01f;
            }
        }

        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        view_volume_brightness.setVisibility(View.VISIBLE);
        volume_brightness_icon.setImageResource(R.drawable.ic_brightness_white);
        volume_brightness_tv.setText(((int) (lpa.screenBrightness * 100)) + "%");
        activity.getWindow().setAttributes(lpa);

    }

    /**
     * 滑动改变播放位置
     */
    private void onProgressSlide(float percent) {
        int position = AP.getPosition();
        int duration = AP.getDuration();
        int deltaMax = Math.min(200, duration - position);
        int delta = (int) (deltaMax * percent) * 100;

        newPosition = delta + position;
        //Log.i(TAG, "onProgressSlide: delta="+ delta + " newPosition=" + newPosition);
        if (newPosition > duration) {
            newPosition = duration;
        } else if (newPosition <= 0) {
            newPosition = 0;
            delta = -position;
        }
        int showDelta = delta/1000;
        if (showDelta != 0) {
            view_SeekTo.setVisibility(View.VISIBLE);
            String text = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
            seektosecond.setText(text + "s");
            seekposition.setText(CtrlBar.formatMilliSecond(newPosition) + "/" + CtrlBar.formatMilliSecond(duration));
        }
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }
        return true;
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        volume = -1;
        brightness = -1f;
        if (newPosition >= 0) {
            handler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            handler.sendEmptyMessage(MESSAGE_SEEK_NEW_POSITION);
        }
        handler.removeMessages(MESSAGE_HIDE_CENTER_BOX);
        handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CENTER_BOX, 500);

    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_HIDE_CENTER_BOX:
                    view_SeekTo.setVisibility(View.GONE);
                    view_volume_brightness.setVisibility(View.GONE);
                    view_OnBuffer.setVisibility(View.GONE);
                    break;
                case MESSAGE_SEEK_NEW_POSITION:
                    if (newPosition >= 0) {
                        AP.setPosition(newPosition);
                        newPosition = -1;
                    }
                    break;
            }
        }
    };

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean volumeControl;
        private boolean toSeek;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!isLive) {
                if (AP.getState() == APlayerAndroid.PlayerState.APLAYER_PLAYING) {
                    AP.pause();
                } else {
                    if (AP.getState() == APlayerAndroid.PlayerState.APLAYER_READ) {
                        AP.open(purl);
                    } else {
                        AP.play();
                    }
                }
            }
            return super.onDoubleTap(e);

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            toggle();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstTouch = true;
            return super.onDown(e);

        }

        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            if (firstTouch) {
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl = mOldX > screenWidthPixels * 0.5f;
                firstTouch = false;
            }

            if (toSeek) {
                int state = AP.getState();
                if (state == APlayerAndroid.PlayerState.APLAYER_PLAYING || state == APlayerAndroid.PlayerState.APLAYER_PAUSED) {
                    if (!isLive && !isLock) {
                        onProgressSlide(-deltaX / playerView.getWidth());
                    }
                }
            } else {
                float percent = deltaY / playerView.getHeight();
                if (!isLock) {
                    if (volumeControl) {
                        onVolumeSlide(percent);
                    } else {
                        onBrightnessSlide(percent);
                    }
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }


    /****
     * 获取当前网速
     *
     * @return String 返回当前网速字符
     **/
    private String getNetSpeed() {
        String netSpeed;
        long nowTotalRxBytes = PlayerUtil.getTotalRxBytes(activity);
        long nowTimeStamp = System.currentTimeMillis();
        long calculationTime = (nowTimeStamp - lastTimeStamp);
        if (calculationTime == 0) {
            netSpeed = String.valueOf(1) + " kb/s";
            return netSpeed;
        }
        //毫秒转换
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / calculationTime);
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        if (speed > 1024) {
            DecimalFormat df = new DecimalFormat("######0.0");
            netSpeed = String.valueOf(df.format(PlayerUtil.getM(speed))) + " MB/s";
        } else {
            netSpeed = String.valueOf(speed) + " kb/s";
        }
        return netSpeed;
    }


}
