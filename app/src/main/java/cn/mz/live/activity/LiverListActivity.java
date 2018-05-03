package cn.mz.live.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.imageview.BGAImageView;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QQ;
import cn.mz.live.APP;
import cn.mz.live.R;
import cn.mz.live.base.BaseActivity;
import cn.mz.live.bean.DataBean;
import cn.mz.live.bean.LiverDataBean;
import cn.mz.live.utils.MD5;
import cn.mz.live.utils.NetWorkUtils;
import cn.mz.live.utils.SP;

/**
 * Created by Administrator on 2018/2/3.
 */

public class LiverListActivity extends BaseActivity {

    @BindView(R.id.refreshLayout1)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.comm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.errorView)
    LinearLayout errorView;

    private String liveName, ico;
    private CommonAdapter<DataBean> adapter;
    private List<DataBean> data = new ArrayList<>();
    private int count;
    private AlertDialog dialog;
    private Bundle bundle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_liver_list;
    }

    @Override
    public void initViews() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setEnableFooterTranslationContent(true);
        refreshLayout.setEnableHeaderTranslationContent(true);
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //infos.clear();
                //getData();
                refreshlayout.finishLoadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                data.clear();
                getData(liveName);
                refreshlayout.finishRefresh();
            }
        });
        title_tv.setText(getIntent().getStringExtra("title"));
        liveName = getIntent().getStringExtra("name");
        ico = getIntent().getStringExtra("ico");

        initAdapter();
        refreshLayout.autoRefresh();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initToolbar() {

    }

    private void initAdapter(){
        GridLayoutManager manager = new GridLayoutManager(this,2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter = new CommonAdapter<DataBean>(this,data,R.layout.item_liver_list) {
            @Override
            public void convert(ViewHolder viewHolder, final DataBean dataBean) {
                viewHolder.setText(R.id.item_title_liver_list,dataBean.getTitle());
                Glide.with(LiverListActivity.this)
                        .load(dataBean.getImage())
                        .placeholder(R.drawable.ic_loading)
                        .into((BGAImageView) viewHolder.getView(R.id.item_img_liver_list));
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle = new Bundle();
                        bundle.putString("ico", ico);
                        bundle.putString("title", dataBean.getTitle());
                        bundle.putString("purl", dataBean.getHref());
                        bundle.putBoolean("isLive", true);

                        ShareToQQ();
                        //toNextActivity(ProPlayerActivity.class, bundle, false);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

    }

    private void getData(String liveName){
        long time =  System.currentTimeMillis()/1000 ;
        String md5 = MD5.getMd5("KDSFNHVJvDVocmx#cvv" + String.valueOf(time));
        if (NetWorkUtils.isAvailable(APP.get())) {
            OkGo.<String>get("http://image.200917.top/api.php?user=lv&platform=" + liveName + "&timestamp=" + time + "&token=" + md5)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String s = response.body();
                            try {
                                Gson gson = new Gson();
                                LiverDataBean bean = gson.fromJson(s, LiverDataBean.class);
                                if (bean.data != null && !bean.data.isEmpty()) {
                                    for (int i = 0; i < bean.data.size(); i++) {
                                        String title = bean.data.get(i).liverName;
                                        String image = bean.data.get(i).image;
                                        String tag = bean.data.get(i).watchCount + "";
                                        String href = bean.data.get(i).rtmp;
                                        data.add(new DataBean(title, image, href, tag));
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

    @OnClick(R.id.btn_back)
    void OnClick(){
        finish();
    }

    private void ShareToQQ(){
        count = SP.getInt("xgm","share");
        final String shareUrl = SP.getString("xgm", "shareUrl");
        if (count<2){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LiverListActivity.this);
                    builder.setTitle("分享");
                    builder.setMessage("分享2次到QQ群当天即可免费观看!");
                    builder.setPositiveButton("分享(已分享:"+count+"次)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            ShareParams shareParams = new ShareParams();
                            shareParams.setUrl(shareUrl);
                            shareParams.setTitle(getString(R.string.app_name));
                            shareParams.setText("聚合大量直播平台，再也不需要到处找平台了，点击下载");
                            shareParams.setShareType(Platform.SHARE_WEBPAGE);
                            shareParams.setImageUrl("http://fastdfs.jpush.cn:8080/push01/M00/02/E8/eS4ULlp1lD6ATfYkAAD5vL9jBI0700.png");
                            JShareInterface.share(QQ.Name, shareParams, mPlatActionListener);
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
            });
        }else {
            toNextActivity(ProPlayerActivity.class, bundle, false);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String toastMsg = (String) msg.obj;
            showToast(toastMsg);
        }
    };
    private PlatActionListener mPlatActionListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享成功";
                handler.sendMessage(message);
                dialog.cancel();
                count++;
                SP.putInt("xgm","share",count);
                //toNextActivity(ProPlayerActivity.class, bundle, false);
            }
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            Log.e("shareError", "error:" + errorCode + ",msg:" + error);
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享失败:" + error.getMessage();
                handler.sendMessage(message);
                dialog.cancel();

            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享取消";
                handler.sendMessage(message);
                dialog.cancel();
            }
        }
    };
}
