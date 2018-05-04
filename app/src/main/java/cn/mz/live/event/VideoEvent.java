package cn.mz.live.event;

import java.util.List;

import cn.mz.live.bean.VideoBean;

/**
 * Created by molu_ on 2018/4/22.
 */

public class VideoEvent {

    private List<VideoBean> datas;

    private boolean isRefresh;

    private String mAction;

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public VideoEvent(List<VideoBean> datas) {
        this.datas = datas;
    }

    public List<VideoBean> getDatas() {
        return datas;
    }

    public void setDatas(List<VideoBean> datas) {
        this.datas = datas;
    }

    public void setAction(String mAction)
    {
        this.mAction = mAction;
    }

    public String getAction()
    {
        return mAction;
    }


}
