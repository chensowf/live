package cn.mz.live.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */

public class LiveDataBean {

    public List<Bean> data;

    public static class Bean {
        public String platform, image, name;
        public String anchor;

    }

}
