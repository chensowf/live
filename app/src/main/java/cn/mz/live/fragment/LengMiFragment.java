package cn.mz.live.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.mz.live.R;
import cn.mz.live.adapter.LengMiVideoAdapter;
import cn.mz.live.api.VideoApi;
import cn.mz.live.base.BaseFragment;
import cn.mz.live.bean.VideoBean;
import cn.mz.live.event.VideoEvent;
import cn.mz.live.toast.Ts;
import cn.mz.live.utils.ThreadPoolUtils;
import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class LengMiFragment extends BaseFragment implements OnRefreshLoadmoreListener,
    OnItemClickListener<VideoBean> {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.comm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.errorView)
    LinearLayout errorView;

    private int mPage = 1;

    private String mVideoType;

    private List<VideoBean> data = new ArrayList<>();
    private CommonAdapter<VideoBean> adapter;


    public LengMiFragment() {
        // Required empty public constructor
    }

    public static LengMiFragment  newInstance(){

        LengMiFragment lengMiFragment = new LengMiFragment();

        return lengMiFragment;
    }

    public void setVideoType(String mVideoType)
    {
        this.mVideoType = mVideoType;
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

    private void initAdapter(){
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter = new LengMiVideoAdapter(getContext(),new ArrayList<VideoBean>());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);


        refreshLayout.autoRefresh();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

        mPage++;

        getVideoDatas(true);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage=1;
        getVideoDatas(false);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallBackDataEvent(VideoEvent event){



        List<VideoBean> datas = event.getDatas();

      if (event.isRefresh()&& (datas==null|| datas.size()==0)){

          Ts.longToast(getContext(),"数据加载为空");
      }else {
          Ts.longToast(getContext(),"收到信息数据:"+datas.size());
      }

        adapter.addDatas(datas);

      adapter.notifyDataSetChanged();

      if (refreshLayout.isLoading()){

          refreshLayout.finishLoadmore();
      }

      if (refreshLayout.isRefreshing()){

          refreshLayout.finishRefresh();
      }
    }

    private void getVideoDatas(final boolean isRefresh){


        ThreadPoolUtils.getThreadPool().submit(new Runnable() {
            @Override
            public void run() {

                List<VideoBean> videoList = VideoApi.getVideoList(mPage,mVideoType);

                VideoEvent event=new VideoEvent(videoList);
                event.setRefresh(isRefresh);

                EventBus.getDefault().post(event);

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    private ProgressDialog progressDialog;

    @Override
    public void onItemClick(ViewGroup viewGroup, View view, final VideoBean videoBean, int i) {

        final String nextPage = videoBean.getNextPage();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("正在加载");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                String videoUrl = VideoApi.getVideoUrl(nextPage);

                emitter.onNext(videoUrl);
            }
        }).subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Consumer<String>() {
                      @Override
                      public void accept(String s) throws Exception {


                          progressDialog.dismiss();

                          Log.e(TAG,"视频url："+s);

                          JZVideoPlayerStandard.startFullscreen(getActivity(),
                                                                JZVideoPlayerStandard.class,
                                                                s, videoBean.getTitle());


                      }
                  });
    }

    @Override
    public boolean onItemLongClick(ViewGroup viewGroup, View view, VideoBean videoBean, int i) {
        return false;
    }


}
