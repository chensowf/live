package cn.mz.live.bean;

/**
 * Created by molu_ on 2018/4/22.
 */

public class VideoBean {


    private String title;

    private String nextPage;

    private String videoUrl;

    private String imageUrl;

    public VideoBean(String title, String nextPage, String imageUrl) {
        this.title = title;
        this.nextPage = nextPage;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "title='" + title + '\'' +
                ", nextPage='" + nextPage + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
