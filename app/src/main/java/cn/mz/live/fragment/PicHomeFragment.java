package cn.mz.live.fragment;

import android.os.Bundle;

import java.util.ArrayList;

import cn.mz.live.api.ImageApi;
import cn.mz.live.base.BaseFragment;

/**
 * Created by molu_ on 2018/4/22.
 */

public class PicHomeFragment extends NewsBaseTabFragment{

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public static PicHomeFragment newInstance(String title) {
        PicHomeFragment fragment = new PicHomeFragment();
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

        String [] titles=new String[]{"性感美女"};
        LengMiImageFragment lengMiImageFragment = LengMiImageFragment.newInstance();
        lengMiImageFragment.setImageType(ImageApi.ImageGroupsListType);
        fragments.add(lengMiImageFragment);

        initTabLayout(titles,fragments);
    }

    @Override
    protected void initData() {

        initTabLayout(null,null);
    }
}
