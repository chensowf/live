package cn.mz.live.fragment;

import android.os.Bundle;

/**
 * Created by molu_ on 2018/4/22.
 */

public class PicHomeFragment extends NewsBaseTabFragment{

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
    }

    @Override
    protected void initData() {

        initTabLayout(null,null);
    }
}
