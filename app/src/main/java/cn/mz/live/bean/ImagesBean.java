package cn.mz.live.bean;

/**
 * Created by Admin on 2018/5/5.
 */

public class ImagesBean {

    private String title;
    private String mImagesUrl;
    private String referer;
    private String cookie;
    private String mNextPage;

    public ImagesBean(String title,
                      String mImagesUrl,
                      String referer,
                      String cookie,
                      String mNextPage)
    {
        this.title = title;
        this.mImagesUrl = mImagesUrl;
        this.referer = referer;
        this.cookie = cookie;
        this.mNextPage = mNextPage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getmImagesUrl() {
        return mImagesUrl;
    }

    public void setmImagesUrl(String mImagesUrl) {
        this.mImagesUrl = mImagesUrl;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getmNextPage() {
        return mNextPage;
    }

    public void setmNextPage(String mNextPage) {
        this.mNextPage = mNextPage;
    }
}
