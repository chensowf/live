package cn.mz.live.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.mz.live.R;
import cn.mz.live.adapter.LengMiImageAdapter;
import cn.mz.live.adapter.LengMiVideoAdapter;
import cn.mz.live.api.ImageApi;
import cn.mz.live.api.VideoApi;
import cn.mz.live.base.BaseFragment;
import cn.mz.live.bean.ImagesBean;
import cn.mz.live.bean.VideoBean;
import cn.mz.live.event.ImageEvent;
import cn.mz.live.event.VideoEvent;
import cn.mz.live.toast.Ts;
import cn.mz.live.utils.ThreadPoolUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LengMiImageFragment extends BaseFragment implements OnRefreshLoadmoreListener,
        OnItemClickListener<ImagesBean> {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.comm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.errorView)
    LinearLayout errorView;

    private int mPage = 1;

    private String mImageType;

    private CommonAdapter<ImagesBean> adapter;


    public LengMiImageFragment() {
        // Required empty public constructor
    }

    public static LengMiImageFragment newInstance() {
        LengMiImageFragment lengMiFragment = new LengMiImageFragment();
        return lengMiFragment;
    }

    public void setImageType(String mImageType) {
        this.mImageType = mImageType;
    }

    @Override
    protected boolean isUserEventBus() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_leng_mi;
    }

    @Override
    public void initView() {

        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayout.setEnableFooterTranslationContent(true);
        refreshLayout.setEnableHeaderTranslationContent(true);
        refreshLayout.setOnRefreshLoadmoreListener(this);

        initAdapter();
    }

    @Override
    protected void initData() {


    }

    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter = new LengMiImageAdapter(getContext(), new ArrayList<ImagesBean>());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        refreshLayout.autoRefresh();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage++;
        getImageDatas(true);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 1;
        getImageDatas(false);
    }

    public void onCallBackDataEvent(ImageEvent event) {

        List<ImagesBean> dates = event.getDatas();
        if (event.isRefresh() && (dates == null || dates.size() == 0)) {

            Ts.longToast(getContext(), "数据加载为空");
        } else {
            Ts.longToast(getContext(), "收到信息数据:" + dates.size());
        }
        adapter.addDatas(dates);
        adapter.notifyDataSetChanged();

        if (refreshLayout.isLoading()) {
            refreshLayout.finishLoadmore();
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
    }

    private void getImageDatas(final boolean isRefresh) {
        ThreadPoolUtils.getThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                List<ImagesBean> imageList = ImageApi.getImagesList(mPage, mImageType);
                final ImageEvent event = new ImageEvent(imageList);
                event.setRefresh(isRefresh);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onCallBackDataEvent(event);
                    }
                });
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private ProgressDialog progressDialog;

    @Override
    public void onItemClick(ViewGroup viewGroup, View view, final ImagesBean imagesBean, int i) {


    }

    @Override
    public boolean onItemLongClick(ViewGroup viewGroup, View view, ImagesBean imagesBean, int i) {
        return false;
    }
}
