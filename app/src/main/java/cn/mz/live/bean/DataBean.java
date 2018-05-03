package cn.mz.live.bean;

/**
 * Created by Administrator on 2018/2/3.
 */

public class DataBean {
    private String title, image, href, tag;

    public DataBean(String title, String image, String href) {
        this.title = title;
        this.image = image;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public DataBean(String title, String image, String href, String tag) {
        this.title = title;
        this.image = image;
        this.href = href;
        this.tag = tag;
    }
}
