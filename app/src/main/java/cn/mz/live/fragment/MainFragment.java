package cn.mz.live.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.bingoogolapple.imageview.BGAImageView;
import cn.mz.live.APP;
import cn.mz.live.R;
import cn.mz.live.activity.ProPlayerActivity;
import cn.mz.live.base.BaseFragment;
import cn.mz.live.bean.DataBean;
import cn.mz.live.bean.MPLiverBean;
import cn.mz.live.utils.NetWorkUtils;

/**
 * Created by Administrator on 2018/2/3.
 * 直播广场
 */

public class MainFragment extends BaseFragment implements OnRefreshLoadmoreListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.comm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.errorView)
    LinearLayout errorView;
    private List<DataBean> data = new ArrayList<>();
    private CommonAdapter<DataBean> adapter;
    private int mPage = 1;


    @Override
    protected boolean isUserEventBus() {
        return true;
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
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter = new CommonAdapter<DataBean>(getActivity(),data,R.layout.item_main_liver) {
            @Override
            public void convert(ViewHolder viewHolder, final DataBean dataBean) {
                viewHolder.setText(R.id.item_main_title_liver,dataBean.getTitle());
                Glide.with(getActivity())
                        .load(dataBean.getImage())
                        .placeholder(R.drawable.ic_loading)
                        .into((BGAImageView) viewHolder.getView(R.id.item_main_img_liver));
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("ico", "http://pp.myapp.com/ma_icon/0/icon_12063624_1517298856/96");
                        bundle.putString("title", dataBean.getTitle());
                        //bundle.putString("purl", dataBean.getHref());
                        bundle.putBoolean("isLive", true);
                        bundle.putBoolean("showAD", false);
                        getPurl(dataBean.getHref(), bundle);
                        //toNextActivity(ProPlayerActivity.class, bundle);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void getData(int page){
        if (NetWorkUtils.isAvailable(APP.get())) {
            OkGo.<String>get("http://api.meipai.com/live_channels/programs.json?page="+page+"&get_online=1")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String s = response.body();
                            try {
                                Gson gson = new Gson();
                                MPLiverBean bean = gson.fromJson(s, MPLiverBean.class);
                                if (bean.getPrograms() != null && !bean.getPrograms().isEmpty()) {
                                    for (int i = 0; i < bean.getPrograms().size(); i++) {
                                        String title = bean.getPrograms().get(i).getRecommend_caption();
                                        String image = bean.getPrograms().get(i).getRecommend_cover_pic();
                                        String href = bean.getPrograms().get(i).getLive().getVideo_stream().getHttp_flv_url();
                                        data.add(new DataBean(title, image, href, ""));
                                    }
                                    adapter.notifyDataSetChanged();
                                    errorView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerView.setVisibility(View.INVISIBLE);
                                    errorView.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                recyclerView.setVisibility(View.INVISIBLE);
                                errorView.setVisibility(View.VISIBLE);
                                showToast("获取数据失败");
                            }
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

    public void getHJData(int page){
        page = page * 20 -20;
        //showToast(page+"");
        if (NetWorkUtils.isAvailable(APP.get())) {
            OkGo.<String>get("http://dianxiumei.com/zb/huajiao.php?id=801&p="+ page)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String s = response.body();
                            try {
                                String pattern = "<a  href=\"(.*?)\">";
                                String pattern1  = "250px\"  src=\"(.*?)\"/>";
                                String pattern2 = "<dd class=\"d-t\">(.*?)</a></dd>";
                                List<String> hrefs = Reg_GetAllString(s, pattern);
                                List<String> images = Reg_GetAllString(s, pattern1);
                                List<String> titles = Reg_GetAllString(s, pattern2);
                                if (hrefs != null && !hrefs.isEmpty()){
                                    for (int i = 0; i < hrefs.size(); i++) {
                                        String title = titles.get(i);
                                        String image = images.get(i);
                                        String href = hrefs.get(i);
                                        data.add(new DataBean(title, image, href, ""));
                                    }
                                    adapter.notifyDataSetChanged();
                                    errorView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }else {
                                    recyclerView.setVisibility(View.INVISIBLE);
                                    errorView.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                recyclerView.setVisibility(View.INVISIBLE);
                                errorView.setVisibility(View.VISIBLE);
                                showToast("获取数据失败");
                            }
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

    private List<String> Reg_GetAllString(String source, String reg){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(source);
        ArrayList<String> list = new ArrayList<String>();
        while(matcher.find()){
            list.add(matcher.group(1));
        }
        return list;
    }

    private void getPurl(String href, final Bundle bundle){
        if (NetWorkUtils.isAvailable(APP.get())) {
            OkGo.<String>get(href)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String s = response.body();
                            try {
                               String pattern = "<source src=\"(.*?)\" type=\"video/mp4\">";
                               String purl = Reg_GetAllString(s, pattern).get(0);
                               bundle.putString("purl", purl);
                               toNextActivity(ProPlayerActivity.class, bundle);
                            } catch (Exception e) {
                                showToast("获取播放地址失败");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            showToast("获取播放地址失败 err(" + response.code() + ")");
                        }
                    });
        }else {
            showToast("获取播放地址失败,网络不可用");
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage++;
        getHJData(mPage);
        refreshLayout.finishLoadmore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 1;
        data.clear();
        getHJData(mPage);
        refreshLayout.finishRefresh();
    }
}
