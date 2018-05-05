package cn.mz.live.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Pair;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.imageview.BGAImageView;
import cn.mz.live.R;
import cn.mz.live.adapter.MainAdapter;
import cn.mz.live.base.BaseFragment;
import cn.mz.live.listener.OnOpenDrawerClickListener;
import cn.mz.live.widget.NoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 *
 * 直播主页面
 */
public class LiveHomeFragment extends BaseFragment {

    @BindView(R.id.home_user_ico)
    BGAImageView user_ico;
    @BindView(R.id.home_tabLayout)
    SegmentTabLayout tab;
    @BindView(R.id.home_viewPager)
    NoScrollViewPager viewPager;

    protected OnOpenDrawerClickListener mOnOpenDrawerClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnOpenDrawerClickListener) {

            mOnOpenDrawerClickListener = (OnOpenDrawerClickListener) context;
        }
    }


    public LiveHomeFragment() {
        // Required empty public constructor
    }

    public static LiveHomeFragment  newInstance(){

        LiveHomeFragment liveHomeFragment=new LiveHomeFragment();

        return  liveHomeFragment;
    }

    @Override
    protected boolean isUserEventBus() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_live_home;
    }

    @Override
    public void initView() {


        initPage();

        showDialog();

    }

    @OnClick(R.id.home_user_ico)
    void OnClick(){

        if (mOnOpenDrawerClickListener!=null){

            mOnOpenDrawerClickListener.openDrawer();
        }
    }

    @Override
    protected void initData() {

    }

    private AlertDialog dialog;
    private void showDialog(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("性感猫公告");
                builder.setMessage("《1》本软件内容采集于第三方直播平台，软件只做学习研究使用\n" +
                        "《2》 未满18周岁请自觉退出并卸载app,否则一切法律后果自行承担\n" +
                        "《3》观看过程中如有发现违规请联系相应平台删除");
                builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dialog.cancel();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initPage(){
        String[] titles = {"直播广场","直播平台"};
        tab.setTabData(titles);
        List<Pair<String, Fragment>> items = new ArrayList<>();
        items.add(new Pair<String, Fragment>("直播广场", new MainFragment()));
        items.add(new Pair<String, Fragment>("直播平台", new LiveFragment()));
        MainAdapter adapter=new MainAdapter(getChildFragmentManager(),items);
        viewPager.setAdapter(adapter);
        //setOffscreenPageLimit缓存页面内容
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.setCurrentItem(0);
        tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
