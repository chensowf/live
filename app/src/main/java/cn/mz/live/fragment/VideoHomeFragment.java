package cn.mz.live.fragment;

import android.os.Bundle;

import cn.mz.live.api.VideoApi;
import cn.mz.live.base.BaseFragment;
import java.util.ArrayList;

/**
 * Created by molu_ on 2018/4/22.
 */

public class VideoHomeFragment extends NewsBaseTabFragment {


    private ArrayList<BaseFragment> fragments=new ArrayList<>();


    public static VideoHomeFragment  newInstance(String title){

        VideoHomeFragment fragment=new VideoHomeFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);

        fragment.setArguments(args);

        return  fragment;
    }

    @Override
    protected boolean isUserEventBus() {
        return false;
    }

    @Override
    public void initView() {
        super.initView();

        String [] titles=new String[]{"冷密美女"};
        LengMiFragment lengMiFragment = LengMiFragment.newInstance();
        lengMiFragment.setVideoType(VideoApi.MeiMiVideoType);
        fragments.add(lengMiFragment);

        initTabLayout(titles,fragments);

    }

    @Override
    protected void initData() {

    }

}
