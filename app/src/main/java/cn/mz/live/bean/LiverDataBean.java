package cn.mz.live.bean;
import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */

public class LiverDataBean {

    public List<Bean> data;

    public class Bean {
        public String liverName, image, rtmp;
        public int watchCount;

    }

}
