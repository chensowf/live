package cn.mz.live.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.imageview.BGAImageView;
import cn.mz.live.APP;
import cn.mz.live.R;
import cn.mz.live.activity.LiverListActivity;
import cn.mz.live.base.BaseFragment;
import cn.mz.live.bean.DataBean;
import cn.mz.live.bean.LiveDataBean;
import cn.mz.live.utils.NetWorkUtils;
import cn.mz.live.utils.RC4;

/**
 * Created by Administrator on 2018/2/3.
 * 直播平台
 */

public class LiveFragment extends BaseFragment implements OnRefreshLoadmoreListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.comm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.errorView)
    LinearLayout errorView;

    private List<DataBean> data = new ArrayList<>();
    private CommonAdapter<DataBean> adapter;

    @Override
    protected boolean isUserEventBus() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_recycler;
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
        refreshLayout.autoRefresh();
    }


    private void initAdapter(){
        GridLayoutManager manager = new GridLayoutManager(getActivity(),4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter = new CommonAdapter<DataBean>(getActivity(),data,R.layout.item_live) {
            @Override
            public void convert(ViewHolder viewHolder, final DataBean dataBean) {
                viewHolder.setText(R.id.item_title_live,dataBean.getTitle() +" ("+ dataBean.getTag() + ")");
                Glide.with(getActivity())
                        .load(dataBean.getImage())
                        .placeholder(R.drawable.ic_loading)
                        .into((BGAImageView) viewHolder.getView(R.id.item_img_live));
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", dataBean.getTitle());
                        bundle.putString("name", dataBean.getHref());
                        bundle.putString("ico", dataBean.getImage());
                        toNextActivity(LiverListActivity.class, bundle);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void getData(){
        if (NetWorkUtils.isAvailable(APP.get())) {
            OkGo.<String>get("http://139.199.26.137/yuncaidan.txt")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String s = response.body();
                            s = s.substring(1,s.length()-1);
                            try {
                                s = new String(s.getBytes(),"gbk");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String[] lives = s.split("\n");
                            LiveDataBean mLiveDataBean = new LiveDataBean();
                            mLiveDataBean.data = new ArrayList<>();
                            for(int i = 0; i < lives.length-1; i++)
                            {
                                String live = lives[i];
                                String[] platLive = live.split("\\|");
                                LiveDataBean.Bean bean = new LiveDataBean.Bean();
                                bean.platform = platLive[0].substring(3,platLive[0].length());
                                bean.image = platLive[1].substring(3,platLive[1].length());
                                bean.name = platLive[2].substring(3,platLive[2].length());
                            //    Log.e("anchor",platLive[3]+"num");
                                bean.anchor = "50";
                                mLiveDataBean.data.add(bean);
                            }
                            for(LiveDataBean.Bean bean : mLiveDataBean.data)
                            {
                                Log.e("platform",bean.platform);
                                Log.e("image",bean.image);
                                Log.e("name",bean.name);
                                Log.e("anchor",bean.anchor+"");

                                data.add(new DataBean(bean.platform, "http://139.199.26.137/tp/"+bean.image, "http://139.199.26.137/"+bean.name, bean.anchor));
                            }
                            adapter.notifyDataSetChanged();
                            errorView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            recyclerView.setVisibility(View.INVISIBLE);
                            errorView.setVisibility(View.VISIBLE);
                            showToast("获取数据失败 err(" + response.code() + ")");
                        }
                    });
        }else {
            recyclerView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
            showToast("获取数据失败,网络不可用");
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        data.clear();
        getData();
        refreshlayout.finishLoadmore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        data.clear();
        getData();
        refreshlayout.finishRefresh();
    }
}
