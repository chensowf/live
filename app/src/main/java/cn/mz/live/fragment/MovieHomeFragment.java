package cn.mz.live.fragment;

import android.os.Bundle;

import java.util.ArrayList;

import cn.mz.live.api.VideoApi;
import cn.mz.live.base.BaseFragment;

/**
 * Created by molu_ on 2018/4/22.
 */

public class MovieHomeFragment extends NewsBaseTabFragment {

    private ArrayList<BaseFragment> fragments=new ArrayList<>();

    public static MovieHomeFragment newInstance(String title) {

        MovieHomeFragment fragment = new MovieHomeFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected boolean isUserEventBus() {
        return false;
    }

    @Override
    public void initView() {

        super.initView();

        String [] titles=new String[]{"韩国伦理"};
        LengMiFragment lengMiFragment = LengMiFragment.newInstance();
        lengMiFragment.setVideoType(VideoApi.SKVideoType);
        fragments.add(lengMiFragment);

        initTabLayout(titles,fragments);
    }

    @Override
    protected void initData() {

        initTabLayout(null, null);
    }
}
