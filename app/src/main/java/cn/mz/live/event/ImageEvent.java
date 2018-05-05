package cn.mz.live.event;

import java.util.List;

import cn.mz.live.bean.ImagesBean;
import cn.mz.live.bean.VideoBean;

/**
 * Created by molu_ on 2018/4/22.
 */

public class ImageEvent {

    private List<ImagesBean> datas;

    private boolean isRefresh;

    private String mAction;

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public ImageEvent(List<ImagesBean> datas) {
        this.datas = datas;
    }

    public List<ImagesBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ImagesBean> datas) {
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
