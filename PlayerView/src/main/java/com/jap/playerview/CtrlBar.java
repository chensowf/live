package com.jap.playerview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aplayer.aplayerandroid.APlayerAndroid;

import java.util.Timer;
import java.util.TimerTask;

public class CtrlBar extends FrameLayout {
    private static final String TAG = "CtrlBar";
    private APlayerAndroid AP = null;
    private String purl = null;
    private ImageView btn_play_pause;
    private TextView tv_position,tv_duration;
    private SeekBar seekBar;

    private Timer positionTimer;
    private static final int POSITION_REFRESH_TIME = 500;

    private Handler ThreadHandler = new Handler(Looper.getMainLooper());
    public Handler getThreadHandler() {
        return ThreadHandler;
    }

    private boolean mbIsDragging = false;

    public CtrlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CtrlBar(Context context) {
        super(context);
        initView();
    }

    public void setCtrlBar(APlayerAndroid ap){
        AP = ap;
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.ctrl_bar, this);
        btn_play_pause = (ImageView) layout.findViewById(R.id.btn_play_puae);
        tv_position = (TextView) layout.findViewById(R.id.tv_position);
        tv_duration = (TextView) layout.findViewById(R.id.tv_duration);
        seekBar = (SeekBar) layout.findViewById(R.id.seekBar);
        seekBar.setMax(0);
        btn_play_pause.setBackgroundResource(R.drawable.ic_btn_play);
        btn_play_pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AP != null){
                    int state = AP.getState();
                    if (state == APlayerAndroid.PlayerState.APLAYER_PLAYING){
                        AP.pause();
                        btn_play_pause.setBackgroundResource(R.drawable.ic_btn_play);
                    }else {
                        if (state == APlayerAndroid.PlayerState.APLAYER_READ){
                            if (!purl.isEmpty()){
                                AP.open(getPurl());
                                btn_play_pause.setBackgroundResource(R.drawable.ic_btn_pause);
                            }
                        }else {
                            AP.play();
                            btn_play_pause.setBackgroundResource(R.drawable.ic_btn_pause);
                        }
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePostion(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mbIsDragging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (AP.getDuration() > 0) {
                    // 仅非直播的视频支持拖动
                    currentPositionInMilliSeconds = seekBar.getProgress();
                    if (AP != null) {
                        AP.setPosition(seekBar.getProgress());
                    }
                }
                mbIsDragging = false;
            }
        });
        enableControllerBar(false);
    }


    /**
     *
     */
    public void changeState() {
        final int status = AP.getState();
        isMaxSetted = false;
        ThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                if (status == APlayerAndroid.PlayerState.APLAYER_READ ) {
                    stopPositionTimer();
                    btn_play_pause.setBackgroundResource(R.drawable.ic_btn_play);
                    //seekBar.setEnabled(false);
                    updatePostion(AP == null ? 0 : AP.getPosition());
                    //updateDuration(AP == null ? 0 : AP.getDuration());
                } else if (status == APlayerAndroid.PlayerState.APLAYER_PLAYING) {
                    startPositionTimer();
                    seekBar.setEnabled(true);
                    btn_play_pause.setEnabled(true);
                    btn_play_pause.setBackgroundResource(R.drawable.ic_btn_pause);
                } else if (status == APlayerAndroid.PlayerState.APLAYER_PAUSED) {
                    btn_play_pause.setEnabled(true);
                    seekBar.setEnabled(true);
                    btn_play_pause.setBackgroundResource(R.drawable.ic_btn_play);
                }
                //Log.i(TAG, "run: "+status);
            }

        });

    }

    private void startPositionTimer() {
        if (positionTimer != null) {
            positionTimer.cancel();
            positionTimer = null;
        }
        positionTimer = new Timer();
        positionTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                ThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onPositionUpdate();
                    }
                });
            }

        }, 0, POSITION_REFRESH_TIME);
    }

    private void stopPositionTimer() {
        if (positionTimer != null) {
            positionTimer.cancel();
            positionTimer = null;
        }
    }


    /**
     * Show the controller on screen. It will go away automatically after 3
     * seconds of inactivity.
     */
    public void show() {
        if (AP == null) {
            return;
        }

        setProgress((int) currentPositionInMilliSeconds);

        this.setVisibility(View.VISIBLE);
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        this.setVisibility(View.GONE);
    }

    /**
     * @hide
     */
    public boolean getIsDragging() {
        return mbIsDragging;
    }

    private void updateDuration(int milliSecond) {
        if (tv_duration != null) {
            tv_duration.setText(formatMilliSecond(milliSecond));
        }
    }

    private void updatePostion(int milliSecond) {
        if (tv_position != null) {
            if (tv_duration.length() > formatMilliSecond(milliSecond).length()){
                tv_position.setText("00:" + formatMilliSecond(milliSecond));
            }else {
                tv_position.setText(formatMilliSecond(milliSecond));
            }


        }
    }

    public static String formatMilliSecond(int milliSecond) {
        int second = milliSecond / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;

    }

    private boolean isMaxSetted = false;

    /**
     * Set the max process for the seekBar, usually the lasting milliseconds
     *
     * @hide
     */
    public void setMax(int maxProgress) {
        if (isMaxSetted) {
            return;
        }
        if (seekBar != null) {
            seekBar.setMax(maxProgress);
        }
        updateDuration(maxProgress);
        if (maxProgress > 0) {
            isMaxSetted = true;
        }
    }

    /**
     * in milliseconds
     * @hide
     * @param progress
     */
    public void setProgress(int progress) {
        if (seekBar != null) {
            seekBar.setProgress(progress);
        }
    }

    /**
     * in milliseconds
     * @hide
     */
    public void setCache(int cache) {
        if (seekBar != null && cache != seekBar.getSecondaryProgress()) {
            seekBar.setSecondaryProgress(cache);
        }
    }

    /**
     * @hide
     * @param isEnable
     */
    public void enableControllerBar(boolean isEnable) {
        seekBar.setEnabled(isEnable);
        btn_play_pause.setEnabled(isEnable);
    }

    private long currentPositionInMilliSeconds = 0L;

    /**
     * onPositionUpdate is invoked per 500ms
     */

    public boolean onPositionUpdate() {
        if (AP == null) {
            return false;
        }
        long newPositionInMilliSeconds = AP.getPosition();

        long previousPosition = currentPositionInMilliSeconds;
        if (newPositionInMilliSeconds > 0 && !getIsDragging()) {
            currentPositionInMilliSeconds = newPositionInMilliSeconds;
        }
        if (getVisibility() != View.VISIBLE) {
            // 如果控制条不可见，则不设置进度
            return false;
        }
        if (!getIsDragging()) {
            int durationInMilliSeconds = AP.getDuration();
            if (durationInMilliSeconds > 0) {
                this.setMax(durationInMilliSeconds);
                int cache = Integer.valueOf(AP.getConfig(APlayerAndroid.CONFIGID.READPOSITION));
                //Log.i(TAG, "onPositionUpdate: "+cache);
                this.setCache(cache);
                // 直播视频的duration为0，此时不设置进度
                if (previousPosition != newPositionInMilliSeconds) {
                    this.setProgress((int) newPositionInMilliSeconds);
                }
            }
        }
        return false;
    }

    /**
     * invoke per 500ms
     * @param milliSeconds
     */
    public void onTotalCacheUpdate(final long milliSeconds) {
        ThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                setCache((int) milliSeconds);
            }

        });
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getPurl() {
        return purl;
    }

}
