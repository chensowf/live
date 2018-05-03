package cn.mz.live.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import cn.mz.live.R;
import cn.mz.live.adapter.BaseFragmentPagerAdapter;
import cn.mz.live.base.BaseFragment;
import cn.mz.live.listener.OnOpenDrawerClickListener;


public abstract class NewsBaseTabFragment extends BaseFragment {

    private static final int TAB_NUM = 4;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    protected OnOpenDrawerClickListener mOnOpenDrawerClickListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnOpenDrawerClickListener) {

            mOnOpenDrawerClickListener = (OnOpenDrawerClickListener) context;
        }
    }

    /***
     * 父类需要调用此方法
     * @param titles
     * @param fragments
     */
    protected void initTabLayout(String[] titles, ArrayList<BaseFragment> fragments) {

        if (titles==null || fragments==null) return;

        if (titles.length < TAB_NUM) {

            /***
             * tab标签是否平分
             */
            tabLayout.setTabSpaceEqual(true);
        }

        BaseFragmentPagerAdapter adapter =
            new BaseFragmentPagerAdapter(getChildFragmentManager(),fragments, titles);
        viewpager.setAdapter(adapter);

        tabLayout.setViewPager(viewpager);
    }

    @Override
    public void initView() {

        if (getArguments() != null) {

            fragmentTitle = getArguments().getString(ARG_TITLE);
            mTvTitle.setText(fragmentTitle);
        }

        mToolbar.setNavigationIcon(R.mipmap.ic_menu);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnOpenDrawerClickListener != null) {
                    mOnOpenDrawerClickListener.openDrawer();
                }
            }
        });


    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_news_base_tab;
    }


}
