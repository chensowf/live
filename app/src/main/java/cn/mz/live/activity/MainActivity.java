package cn.mz.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import cn.mz.live.R;
import cn.mz.live.activity.video.VideoActivity;
import cn.mz.live.base.BaseActivity;
import cn.mz.live.fragment.LiveHomeFragment;
import cn.mz.live.fragment.MovieHomeFragment;
import cn.mz.live.fragment.PicHomeFragment;
import cn.mz.live.fragment.VideoHomeFragment;
import cn.mz.live.listener.OnOpenDrawerClickListener;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationBar.OnTabSelectedListener,OnOpenDrawerClickListener {


    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    private LiveHomeFragment mLiveHomeFragment;

    private VideoHomeFragment mVideoHomeFragment;
    private MovieHomeFragment mMovieHomeFragment;
    private PicHomeFragment mPicHomeFragment;

    private FragmentTransaction fragmentTransaction;

    private int mImageViewArray[] = {
            R.drawable.btn_tab_live ,
            R.drawable.btn_tab_video,
            R.drawable.btn_tab_movie,
            R.drawable.btn_tab_picture,
            };

    //Tab选项卡的文字
    //private String mTextviewArray[] = {"奇点说", "更多"};
    private String mTextviewArray[] = {
            "直播",
            "视频",
            "电影",
            "美图",
            };

    private List<Fragment> mFragments = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {


    }

    @Override
    public void initViews() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.
                BACKGROUND_STYLE_STATIC);

        // BottomNavigationViewHelper.disableShiftMode(mBottomNavigationBar);
        mBottomNavigationBar.setBarBackgroundColor(R.color.white);
        for (int i = 0; i < mImageViewArray.length; i++) {

            mBottomNavigationBar.addItem(new BottomNavigationItem(mImageViewArray[i], mTextviewArray[i]));
        }
        mBottomNavigationBar.setFirstSelectedPosition(0);
        mBottomNavigationBar.initialise();

        onTabSelected(0);
        mBottomNavigationBar.setTabSelectedListener(this);

        mNavigationView.setNavigationItemSelectedListener(this);

    }



    @Override
    public void initToolbar() {

    }

    @Override
    public void onTabSelected(int position) {

        FragmentManager manager = getSupportFragmentManager();

        fragmentTransaction = manager.beginTransaction();

        hideFragment(fragmentTransaction);



        switch (position) {

            case 0:



                if (mLiveHomeFragment == null) {

                    mLiveHomeFragment = LiveHomeFragment.newInstance();

                    fragmentTransaction.add(R.id.content, mLiveHomeFragment);

                    mFragments.add(mLiveHomeFragment);
                }else {

                    fragmentTransaction.show(mLiveHomeFragment);
                }

                break;

            case 1:

                if (mVideoHomeFragment == null) {

                    mVideoHomeFragment = VideoHomeFragment.newInstance(mTextviewArray[position]);

                    fragmentTransaction.add(R.id.content, mVideoHomeFragment);

                    mFragments.add(mVideoHomeFragment);
                } else {

                    fragmentTransaction.show(mVideoHomeFragment);
                }

                break;

            case 2:

                if (mMovieHomeFragment == null) {

                    mMovieHomeFragment = MovieHomeFragment.newInstance(mTextviewArray[position]);

                    fragmentTransaction.add(R.id.content, mMovieHomeFragment);

                    mFragments.add(mMovieHomeFragment);
                } else {

                    fragmentTransaction.show(mMovieHomeFragment);
                }

                break;

            case 3:

                if (mPicHomeFragment == null) {

                    mPicHomeFragment = PicHomeFragment.newInstance(mTextviewArray[position]);

                    fragmentTransaction.add(R.id.content, mPicHomeFragment);

                    mFragments.add(mPicHomeFragment);
                } else {

                    fragmentTransaction.show(mPicHomeFragment);
                }


                break;

        }

        fragmentTransaction.commit();

    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * Hide fragment.
     *
     * @param transaction the transaction
     */
    public void hideFragment(FragmentTransaction transaction) {
        for (Fragment fragment : mFragments) {

            transaction.hide(fragment);
        }
    }


    public void onOpen() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (JZVideoPlayer.backPress()) {
            return false;
        }
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //showToast("再按一次退出");
                mExitTime = System.currentTimeMillis();
                //toNextActivity(ExitAdDialog.class, 1);
                Bundle bundle = new Bundle();
                bundle.putInt("code", 2);
                toNextActivityForResult(ExitAdDialog.class, 2, bundle);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getBooleanExtra("do", false)){
            finish();
        }
    }

//http://imtt.dd.qq.com/16891/70F0FF78CBA2354E6FC37B576DFD83ED.apk?fsname=com.qi.dian.shuo_3.0.3_303.apk&csr=1bbd

    @Override
    public void onBackPressed() {

        if (JZVideoPlayer.backPress()) {
            return;
        }

        super.onBackPressed();

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){


            case R.id.action_nav_leng_mi:


                toNextActivity(VideoActivity.class,false);

                break;
        }



        return true;
    }


    @Override
    public void openDrawer() {
        onOpen();
    }
}
